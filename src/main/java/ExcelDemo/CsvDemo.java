package ExcelDemo;

import MyDate.DateUtil;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import dbmgr.mySqlAccess.MySqlHelper;
import fileDemo.FileHelper;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class CsvDemo {
    public static void main(String[] args) throws IOException {
//        String[] a = split("\"123\", \"jack jona\", \"New York, NY\"");
//        String[] b = split("\"234\", \"Lee Jack\", \"Fort myers, FL\"");

        //readCsv("F:\\云南大学\\爬取的数据\\世界银行\\压缩包2\\API_AG.CON.FERT.ZS_DS2_zh_csv_v2_2168080.csv");

        List<File> list = FileHelper.findFile("E:\\老子项目","一手数据.xlsx");

        System.out.println(list);


        Collection<File> files = FileUtils.listFiles(new File("F:\\云南大学\\爬取的数据\\世界银行\\压缩包2"), new IOFileFilter() {
            @Override
            public boolean accept(File file) {
                return file.getName().startsWith("API_");
            }

            @Override
            public boolean accept(File file, String s) {
                return false;
            }
        }, null);
        for (File file : files) {
            if (file.getName().startsWith("API_")) {
                readCsv(file.getPath());
            }
        }
        System.out.println("over");

    }

    public static void readCsv(String filePath) throws IOException {
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;
        try {
            File fileName = new File(filePath);
            fileReader = new FileReader(fileName);
            bufferedReader = new BufferedReader(fileReader);
            try {
                int lineNum = 0;
                String read = null;
                String[] yearArr = null;
                while ((read = bufferedReader.readLine()) != null) {
                    lineNum++;
                    if (lineNum == 5) {
                        String yearStr = read.replace("\"Country Name\",\"Country Code\",\"Indicator Name\",\"Indicator Code\",", "");
                        yearArr = split(yearStr);
                    }
                    if (lineNum > 5) {
                        String[] lineVal = split(read);
                        handleLine(lineVal, yearArr);
                    }
                    System.out.println(read);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (fileReader != null) {
                fileReader.close();
            }
        }
    }

    public static void handleLine(String[] lineVal, String[] yearArr) throws SQLException {
        String countryName = lineVal[0];
        String countryCode = lineVal[1];
        String indicatorName = lineVal[2];
        String indicatorCode = lineVal[3];

        for (int i = 0; i < yearArr.length; i++) {
            String yearVal = lineVal[i + 4];
            // 年份不是空的（分隔出来的最后的一个年份是空的）
            if (StringUtils.hasText(yearArr[i])) {
                addRecord(countryName, countryCode, indicatorName, indicatorCode, yearArr[i], yearVal);
            }
        }

        System.out.println(countryCode);
        System.out.println(countryName);
    }

    public static MySqlHelper mySqlHelper = null;

    public static void addRecord(String countryName, String countryCode, String indicatorName, String indicatorCode, String year, String val) throws SQLException {
        if (mySqlHelper == null) {
            HikariDataSource dataSource = new HikariDataSource(getConfig());
            mySqlHelper = new MySqlHelper(dataSource);
        }

        String time = DateUtil.format(new Date());
        String sql = "INSERT INTO `overseaindicator` (`countryCode`, `countryName`, `indicatorCode`, `indicatorName`, `unit`, `year`, `value`, `datasource`, `createUser`, `createTime`, `updateUser`, `updateTime`, `status`) " +
                "VALUES (?, ?, ?, ?, '', ?, ?, '世界银行', '', ?, '', now(), '1');";
        List<Object> paras = Arrays.asList(countryCode, countryName, indicatorCode, indicatorName, year, val, time);
        mySqlHelper.execute(sql, paras);
    }

    public static HikariConfig getConfig() {

        HikariConfig config = new HikariConfig();

        config.setJdbcUrl("jdbc:mysql://192.168.52.64:3306/yunnan?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=UTC");
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setUsername("root");
        config.setPassword("123456");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmptCacheSqlLimit", "2048");

        return config;
    }

    public static String[] split(String str) {
        //双引号内的逗号不分割  双引号外的逗号进行分割
        String[] arr = str.trim().split(",(?=([^\\\"]*\\\"[^\\\"]*\\\")*[^\\\"]*$)", -1);
        // 去重前后的空格，和前后的双引号
        return trim(arr);
    }

    public static String[] trim(String[] array) {
        String[] result = new String[array.length];

        for (int i = 0; i < array.length; ++i) {
            String element = array[i];
            String abc = element != null ? element.trim() : null;
            abc = StringUtils.trimLeadingCharacter(abc, '"');
            abc = StringUtils.trimTrailingCharacter(abc, '"');
            result[i] = abc;
        }

        return result;
    }
}
