package bigdata;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import dbmgr.mySqlAccess.MySqlHelper;

import java.util.LinkedHashMap;
import java.util.List;

public class hiveDemo01 {
public static void main(String[] args) throws Exception {
    HikariDataSource dataSource = new HikariDataSource(getConfig());
    MySqlHelper mySqlHelper = new MySqlHelper(dataSource);

    List<LinkedHashMap<String, Object>> list = mySqlHelper.simpleQuery("select * from t_user", null);

    System.out.println(list);
}

public static HikariConfig getConfig() {

    HikariConfig config = new HikariConfig();

    //config.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/test?useUnicode=true&characterEncoding=utf8&useSSL=false");

    // config.setJdbcUrl("jdbc:mysql://192.168.100.92:3306/bd?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=UTC");
    config.setJdbcUrl("jdbc:hive2://192.168.255.143:10000/db_blog");
    config.setDriverClassName("org.apache.hive.jdbc.HiveDriver");
    config.setUsername("");
    config.setPassword("");

    return config;
}

/**
 *  jdbc:hive2://192.168.255.143:10000/db_blog;transportMode=http;httpPath=cliservice 不用加后面的
 *  这样写即可：jdbc:hive2://192.168.255.143:10000/db_blog
 *
 *  查询语句要加上要查询的字段，用 * 的话，返回的结果中字段是 表名+字段名 组合成的。
 */
}
