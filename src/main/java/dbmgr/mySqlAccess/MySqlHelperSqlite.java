package dbmgr.mySqlAccess;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.lang3.StringUtils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class MySqlHelperSqlite {

    public static void main(String[] args) throws Exception {

        HikariDataSource dataSource = new HikariDataSource(getConfig());
        MySqlHelper mySqlHelper = new MySqlHelper();

        System.out.println("完");
    }

    public static void updateModel(HikariDataSource dataSource,String jsonData,String modelid) throws Exception{
        Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement();
        jsonData = jsonData.replace("\"","\\\"");
        jsonData = jsonData.replace("\'","\\\'");
        jsonData = jsonData.replace("\\n","\\\\n");

        String sql = String.format("update model set  jsondata = '%s' where modelid = %s",jsonData,modelid);
        System.out.println(sql);
        statement.execute(sql);
        connection.close();


    }

    public static void updateModelConfig(HikariDataSource dataSource,String data,String modelconfigid) throws Exception{
        Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement();
        String sql = String.format("update modelconfig set layout = '%s' where modelconfigid = %s",data,modelconfigid);
        System.out.println(sql);
        statement.execute(sql);
        connection.close();
    }

    public static HikariConfig getConfig() {

        HikariConfig config = new HikariConfig();

        //config.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/test?useUnicode=true&characterEncoding=utf8&useSSL=false");

       // config.setJdbcUrl("jdbc:mysql://192.168.100.92:3306/bd?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=UTC");
        config.setJdbcUrl("jdbc:mysql://10.170.2.135:3306/bd?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=UTC");
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setUsername("root");
        config.setPassword("123456");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmptCacheSqlLimit", "2048");

        return config;
    }
}
