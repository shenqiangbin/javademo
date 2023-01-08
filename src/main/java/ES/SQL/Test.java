package ES.SQL;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import dbmgr.mySqlAccess.MySqlHelper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.LinkedHashMap;
import java.util.List;


public class Test {
    public static void main(String[] args) throws Exception {

        MySqlHelper mySqlHelper = new MySqlHelper();

        String jdbcUrl = "jdbc:es://http://127.0.0.1:9200/?timezone=UTC&page.size=250";
        DriverManager.getDriver("org.elasticsearch.xpack.sql.jdbc.EsDriver");
        Connection connection = DriverManager.getConnection(jdbcUrl);
        List<LinkedHashMap<String, Object>> linkedHashMaps = mySqlHelper.simpleQuery(connection, "select * from user");

        System.out.println(linkedHashMaps.size());
    }

    public static HikariConfig getConfig() {

        HikariConfig config = new HikariConfig();

        String jdbcUrl = "jdbc:es://http://127.0.0.1:9200/?timezone=UTC&page.size=250";
        //config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setJdbcUrl(jdbcUrl);

        return config;

        //Connection connection = DriverManager.getConnection();
    }

}
