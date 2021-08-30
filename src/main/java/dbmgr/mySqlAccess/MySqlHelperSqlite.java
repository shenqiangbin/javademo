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
import java.util.LinkedHashMap;
import java.util.List;

public class MySqlHelperSqlite {


    // Sqlite 的访问和普通的数据库是一样的
    public static void main(String[] args) throws Exception {

        HikariDataSource dataSource = new HikariDataSource(getConfig());
        MySqlHelper mySqlHelper = new MySqlHelper(dataSource);
        List<LinkedHashMap<String, Object>> list = mySqlHelper.simpleQuery("select * from Article", null);

        System.out.println("完");
    }

    public static HikariConfig getConfig() {

        HikariConfig config = new HikariConfig();

        config.setJdbcUrl("jdbc:sqlite:/Users/adminqian/git/picSpider/blog.db3");
        config.setDriverClassName("org.sqlite.JDBC");
        config.setUsername("");
        config.setPassword("");

        return config;
    }
}
