package dbmgr.neoAccess;

import ExcelDemo.Excel2007Utils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import dbmgr.mySqlAccess.MySqlHelper;
import fileDemo.FileHelper;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;

import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 处理从线上获取的四个库的文件
 */
public class GuangMingOnlineDataFile {

    static HikariDataSource dataSource = new HikariDataSource(getConfig());
    static MySqlHelper mySqlHelper = new MySqlHelper(dataSource);
    static List<List<Object>> params = new ArrayList<>();
    static List<String> existsList = new ArrayList<>();
    static StringBuilder msger = new StringBuilder();
    static int existsCount = 0;

    public static void main(String[] args) throws Exception {
        //处理从线上获取的四个库的文件，入到 mysql 数据库中
        //String[] dbs = new String[]{"food","livestock","farming","fishery"};
        String[] dbs = new String[]{"fishery"};
        for (String db : dbs) {
            dataToMysql(db);
        }
    }

    static void dataToMysql(String db) throws Exception {
        //String[] files = new String[]{"food_2018.txt","food_2019.txt","food_2020.txt","food_2021.txt","food_2022.txt"};
        String[] files = new String[]{"food_DM_2018.txt", "food_DM_2019.txt", "food_DM_2020.txt", "food_DM_2021.txt", "food_DM_2022.txt"};

        //String[] files = new String[]{"food_2018.txt","food_2019.txt","food_2020.txt","food_2021.txt","food_2022.txt"};

        for (String file : files) {
            file = file.replace("food", db);
            System.out.println("file:" + file);
            msger.append("file:" + file + "\r\n");
            existsCount = 0;
            dataToMysqlFile("D:\\code\\TPI\\大数据产品\\光明国际\\数据处理\\线上数据\\" + file);
        }
    }

    private static void dataToMysqlFile(String file) throws Exception {
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;
        int line = 1;

        params.clear();

        try {
            File fileName = new File(file);
            fileReader = new FileReader(fileName);
            bufferedReader = new BufferedReader(fileReader);
            String read = null;
            while ((read = bufferedReader.readLine()) != null) {
                handleOneLine(read);
                if (line % 10000 == 0) {
                    System.out.println(line);
                }
                line++;
            }
        } finally {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (fileReader != null) {
                fileReader.close();
            }
        }

        if (params.size() > 0) {
            String sql = "INSERT INTO gm_articles (publictime, db, year,issue,keyword,title, author, org, fromOrg, file,ffd,filetype,authorCode, business, pageIndex) VALUES (%s)";
            sql = String.format(sql, getParams(15));
            mySqlHelper.executeSqlBatch(sql, params);
            params.clear();
            existsList.clear();
        }

        msger.append("total line:" + (line - 1) + "," + file + "\r\n");
        msger.append("exists count:" + existsCount);
        FileHelper.writeTxtFile(msger.toString(), file + ".record.txt", false, false);
    }

    private static void handleOneLine(String line) throws IOException, SQLException {
        JSONObject jsonObject = JSON.parseObject(line);

        String title = jsonObject.get("题名").toString();
        String business = jsonObject.get("category").toString();

        if (!existRecord(title, business)) {

            List<Object> paraItem = new ArrayList<>();
            String[] fields = {"发表时间", "来源数据库", "年", "期", "关键词", "题名", "作者", "机构", "来源", "文件名", "ffd", "filetype", "新作者代码", "category", "pageIndex"};
            for (String field : fields) {
                paraItem.add(toEmptyIfNULL(jsonObject.get(field)));
            }

            String sql = "INSERT INTO gm_articles (publictime, db, year,issue,keyword,title, author, org, fromOrg, file,ffd,filetype,authorCode, business, pageIndex) VALUES (%s)";
            sql = String.format(sql, getParams(15));

            params.add(paraItem);
            existsList.add(title + "-" + business);

            if (params.size() > 0 && params.size() % 5000 == 0) {
                mySqlHelper.executeSqlBatch(sql, params);
                params.clear();
                existsList.clear();
            }
        } else {
            System.out.println("exists:" + title + ":" + business);
            msger.append("exists:" + title + ":" + business + "\r\n");
            existsCount++;
        }

    }

    private static boolean existRecord(String title, String business) throws SQLException {
        if(existsList.contains(title + "-" + business)){
            return true;
        }
        String sql = "select count(0) from gm_articles where business = ? and title = ? ";

        List<Object> paraItem = new ArrayList<>();
        paraItem.add(business);
        paraItem.add(title);

        String count = mySqlHelper.executeScalar(sql, paraItem);
        return Integer.parseInt(count) > 0;
    }

    public static String toEmptyIfNULL(Object str) {
        return str == null ? "" : str.toString();
    }

    public static String getParams(int size) {
        String[] arr = new String[size];
        for (int i = 0; i < size; i++) {
            arr[i] = "?";
        }
        return String.join(",", arr);
    }

    public static HikariConfig getConfig() {

        HikariConfig config = new HikariConfig();

        //config.setJdbcUrl("jdbc:mysql://:3306/bd?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=UTC");
        config.setJdbcUrl("jdbc:mysql://192.168.52.63:13306/gmgj?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=UTC");
        config.setUsername("root");
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setPassword("123456");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmptCacheSqlLimit", "2048");

        return config;
    }

}
