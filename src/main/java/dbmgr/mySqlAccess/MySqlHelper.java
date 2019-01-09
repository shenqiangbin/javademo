package dbmgr.mySqlAccess;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import common.P;
import dbmgr.microsoftAccess.ResultSetHelper;

import java.sql.*;
import java.util.List;

public class MySqlHelper {
    private HikariDataSource ds = null;
    private HikariConfig config = null;

//    public static void main(String[] args) throws Exception{
//        P.print("hello");
//
//        String sql = "select * from resourceitem";
//        for (int i = 0; i < 280000; i++) {
//            //Thread.sleep(1000);
//
//            P.print(i);
//            simpleQuery(sql);
//        }
//
//    }

    public MySqlHelper(HikariConfig ds){
        this.config = ds;
    }

    private HikariDataSource getDataSource() throws SQLException {

//        HikariConfig config = new HikariConfig();
//
//        config.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/javablog?useUnicode=true&characterEncoding=utf8&useSSL=false");
//        config.setUsername("root");
//        config.setPassword("123456");
//        config.addDataSourceProperty("cachePrepStmts", "true");
//        config.addDataSourceProperty("prepStmtCacheSize", "250");
//        config.addDataSourceProperty("prepStmptCacheSqlLimit", "2048");

        if(ds == null)
            ds = new HikariDataSource(config);

        return ds;
    }

    public <T> List<T> simpleQuery(String sql,Class<T> type) throws  Exception {

        HikariDataSource dataSource = getDataSource();
        Connection connection = dataSource.getConnection();

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);

        //printResultSet(resultSet);
        List<T> list = ResultSetHelper.toList(resultSet, type);

        if (connection != null && !connection.isClosed())
            connection.close();

        return list;
    }

    public void insert(String sql)throws  Exception{

        HikariDataSource dataSource = getDataSource();
        Connection connection = dataSource.getConnection();

        Statement statement = connection.createStatement();
        statement.execute(sql);

        if (connection != null && !connection.isClosed())
            connection.close();
    }

}
