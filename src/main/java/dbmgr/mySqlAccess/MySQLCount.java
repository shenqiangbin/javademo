package dbmgr.mySqlAccess;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import fileDemo.FileHelper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 *
 */
public class MySQLCount {

    static HikariDataSource dataSource = new HikariDataSource(getConfig());
    static MySqlHelper mySqlHelper = new MySqlHelper(dataSource);


    public static void main(String[] args) throws Exception {
        String db = "szrwdev";

        String sql = "SELECT TABLE_NAME as tname FROM information_schema.tables WHERE " +
                "TABLE_SCHEMA = 'szrwdev' ORDER  BY data_length desc";

        sql = sql.replace("szrwdev", db);

        List<String> sqlList = new ArrayList<>();

        List<LinkedHashMap<String, Object>> names = mySqlHelper.simpleQuery(sql, null);
        for(LinkedHashMap<String, Object> item : names){
            String tname = item.get("tname").toString();
            //System.out.println(tname);

            String oneSql = "SELECT 'taskversion' as name,count(*) as count from szrwdev.`taskversion`"
                    .replace("taskversion", tname)
                    .replace("szrwdev", db);

            sqlList.add(oneSql);//
        }

        String result = String.join("\r\n union all \r\n ",sqlList);
        System.out.println(result);
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
        config.setJdbcUrl("jdbc:mysql://192.168.52.64:3306/mysql?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=UTC");
        config.setUsername("root");
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setPassword("123456");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmptCacheSqlLimit", "2048");

        return config;
    }

}
