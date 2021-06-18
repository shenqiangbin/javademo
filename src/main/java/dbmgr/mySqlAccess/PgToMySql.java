package dbmgr.mySqlAccess;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mysql.cj.protocol.a.MysqlBinaryValueDecoder;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import common.P;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PgToMySql {

    static List<List<Object>> paramsList = new ArrayList<>();

    public static void main(String[] args) throws SQLException {
        P.print("abc");

        HikariDataSource pgdataSource = new HikariDataSource(getPgConfig());
        MySqlHelper pgHelper = new MySqlHelper(pgdataSource);

        HikariDataSource mysqlDataSource = new HikariDataSource(getConfig());
        MySqlHelper mySqlHelper = new MySqlHelper(mysqlDataSource);

        String str = pgHelper.executeScalar("select count(*) from tjj_resource_data", null);
        long totalNumber = Long.parseLong(str);
        long pageSize = 5000;
        long loop = (totalNumber / pageSize) + 1;

        for (long i = 0; i <= loop; i++) {
            try {
                sync(i * pageSize, pageSize, pgHelper, mySqlHelper);
            } catch (Exception e) {
                e.printStackTrace();
                break;

            }
        }
    }

    public static long getRecordCount(HikariDataSource dataSource) {

        long result = 0;

        try {
            String sql = "select count(0) from bd.data_custom";
            Connection connection = dataSource.getConnection();

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet != null && resultSet.next())
                result = resultSet.getLong(1);

            dataSource.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public static void sync(long start, long pageSize, MySqlHelper pgHelper, MySqlHelper mySqlHelper) throws Exception {

        String sql = String.format("SELECT * FROM \"tjj_resource_data\" limit %s offset %s", pageSize, start);

        pgHelper.simpleQuery(sql, null, new IResultHandler() {
            @Override
            public void handle(ResultSet resultSet) throws Exception {
                while (resultSet.next()) {
                    String col1 = resultSet.getString("scatalog_id");
                    String col2 = resultSet.getString("area_id");
                    String col3 = resultSet.getString("index_id");
                    String col4 = resultSet.getString("index_name");
                    String col5 = resultSet.getString("index_data");
                    String col6 = resultSet.getString("stat_dt");
                    String col7 = resultSet.getString("insert_dt");
                    String col8 = resultSet.getString("parents_index_id");

                    //String msg = String.format("%s %s %s %s",tablename,colName,rowNum,cellVal);
                    //P.print(msg);
                    List<Object> pList = new ArrayList<>();
                    pList.add(col1);
                    pList.add(col2);
                    pList.add(col3);
                    pList.add(col4);
                    pList.add(col5);
                    pList.add(col6);
                    pList.add(col7);
                    pList.add(col8);

                    paramsList.add(pList);
                }
            }

            @Override
            public boolean isSuccess() {
                return false;
            }
        });

        go(mySqlHelper);

        System.out.println("end");
    }

    public static void go(MySqlHelper mySqlHelper) throws SQLException {
        String sql = "insert tjj_resource_data values(?,?,?,?,?,?,?,?)";
        mySqlHelper.executeSqlBatch(sql, paramsList);
        paramsList.clear();
    }

    public static HikariConfig getConfig() {

        HikariConfig config = new HikariConfig();

        //config.setJdbcUrl("jdbc:mysql://:3306/bd?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=UTC");
        config.setJdbcUrl("jdbc:mysql://172.29.1.114:3306/economy?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=UTC");
        config.setUsername("root");
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setPassword("123456");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmptCacheSqlLimit", "2048");

        return config;
    }

    public static HikariConfig getPgConfig() {

        HikariConfig config = new HikariConfig();

        //config.setJdbcUrl("jdbc:mysql://:3306/bd?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=UTC");
        //config.setJdbcUrl("jdbc:postgresql://59.215.186.232:8081/database_cds_ysjsl?currentSchema=database_dm_macroeconomy");
        config.setJdbcUrl("jdbc:postgresql://172.29.1.88:3306/database_cds_ysjsl?currentSchema=database_dm_macroeconomy");
        config.setUsername("ysjslr");
        config.setDriverClassName("org.postgresql.Driver");
        config.setPassword("Ysjslr123!");

        return config;
    }


}
