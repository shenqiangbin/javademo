package dbmgr.neoAccess;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import dbmgr.mySqlAccess.MySqlHelper;
import fileDemo.FileHelper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;


public class GuangMingOnlineMysqlToFile {

    static HikariDataSource dataSource = new HikariDataSource(getConfig());
    static MySqlHelper mySqlHelper = new MySqlHelper(dataSource);
    static List<List<Object>> params = new ArrayList<>();
    static List<String> existsList = new ArrayList<>();
    static StringBuilder mergeBuilder = new StringBuilder();
    static int existsCount = 0;

    /*
    SELECT COUNT(*) FROM gm_articles_has_author WHERE business = 'food' --882081
SELECT COUNT(*) FROM gm_articles_has_author WHERE business = 'farming' --221824
SELECT COUNT(*) FROM gm_articles_has_author WHERE business = 'livestock' --74049
SELECT COUNT(*) FROM gm_articles_has_author WHERE business = 'fishery' --28240
    * */
    public static void main(String[] args) throws Exception {
        // 将 MySQL 中的数据导入到文件中，供他们分析使用。（分批获取 MySQL 表中的数据，然后写入到文件中）
        String[] dbs = new String[]{"food","livestock","farming","fishery"};
        //String[] dbs = new String[]{"fishery"};
        for (String db : dbs) {
            mysqlToFile(db);
        }
    }

    static void mysqlToFile(String db) throws Exception {
        String format = "select * from gm_articles_has_author where business = ? ";
        String sumsql = "select count(*) as val from gm_articles_has_author where business = ?";

        String countStr = mySqlHelper.executeScalar(sumsql, Arrays.asList(new String[]{db}));
        Long totalCount = Long.parseLong(countStr);

        int pageSize = 5000;
        Long totalPage = totalCount / pageSize + (totalCount % pageSize == 0 ? 0 : 1);

        List<LinkedHashMap<String, Object>> list = null;
        String sql = "";

        StringBuilder builder = new StringBuilder();

        for (int pageIndex = 1; pageIndex <= totalPage; pageIndex++) {

            System.out.println(String.format("%s/%s",pageIndex,totalPage));
            int startIndex = (pageIndex - 1) * pageSize;
            sql = String.format("%s limit %s,%s", format, startIndex, pageSize);
            list = mySqlHelper.simpleQuery(sql,  new String[]{db});

            builder = new StringBuilder();
            handleWordsResult(list, builder);

            FileHelper.writeTxtFile(builder.toString(),"D:\\code\\TPI\\大数据产品\\光明国际\\数据处理\\数据去重后得MySQL导出到文件\\2023\\"+ db + ".txt", false, true);
            FileHelper.writeTxtFile(builder.toString(),"D:\\code\\TPI\\大数据产品\\光明国际\\数据处理\\数据去重后得MySQL导出到文件\\2023\\merge.txt", false, true);

        }

    }

    private static void handleWordsResult(List<LinkedHashMap<String, Object>> list, StringBuilder builder) {
        for(LinkedHashMap<String, Object> item : list){
            String year = toEmptyIfNULL(item.get("year"));
            String keyword = toEmptyIfNULL(item.get("keyword"));
            String title = toEmptyIfNULL(item.get("title"));
            String author = toEmptyIfNULL(item.get("author"));
            String org = toEmptyIfNULL(item.get("org"));
            String authorCode = toEmptyIfNULL(item.get("authorCode"));
            String business = toEmptyIfNULL(item.get("business"));
            String file = toEmptyIfNULL(item.get("file"));

            JSONObject obj = new JSONObject();
            obj.put("title",title);
            obj.put("author",author);
            obj.put("authorCode",authorCode);
            obj.put("organization",org);
            obj.put("keyword",keyword);
            obj.put("category",business);
            obj.put("year",year);
            obj.put("id",file);

            builder.append(obj.toString()).append("\r\n");
        }
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
        //config.setJdbcUrl("jdbc:mysql://192.168.52.63:13306/gmgj?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=UTC");
        config.setJdbcUrl("jdbc:mysql://10.120.130.175:13306/gmgj_mid_data?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=UTC");
        config.setUsername("root");
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setPassword("123456");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmptCacheSqlLimit", "2048");

        return config;
    }

}
