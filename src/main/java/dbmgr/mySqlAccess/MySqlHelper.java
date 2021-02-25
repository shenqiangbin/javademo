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

    public String executeScalar(String sql, List<Object> params) throws SQLException {
        Connection connection = null;
        try {
            HikariDataSource dataSource = getDataSource();
            connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            if (params != null) {
                for (int i = 0; i < params.size(); i++) {
                    statement.setObject(i + 1, params.get(i));
                }
            }

            ResultSet resultSet = statement.executeQuery();
            if (resultSet != null && resultSet.next()) {
                return resultSet.getString(1);
            }
        } finally {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        }
        return "";
    }

}
