package dbmgr.kbaseAccess;

import com.kbase.jdbc.ResultSetImpl;
import common.P;
import dbmgr.microsoftAccess.ResultSetHelper;

import java.sql.*;
import java.util.List;

public class KBaseHelper {

    private String jdbcUrl;
    private String userName;
    private String password;

    private Connection connection = null;

    public KBaseHelper(String jdbcUrl,String userName,String password) {

        this.jdbcUrl = jdbcUrl;
        this.userName = userName;
        this.password = password;

        try {
            Class.forName("com.kbase.jdbc.Driver");
        } catch (ClassNotFoundException cnfex) {
            System.out.println("Problem in loading kbase JDBC driver");
            cnfex.printStackTrace();
        }
    }

    private Connection getConnection() {
        try {
            if (connection == null)
                connection = DriverManager.getConnection(jdbcUrl, userName, password);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public <T> List<T> query(String sql, List<Object> params, Class<T> type,String[] dbfields) {
        Statement statement = null;
        ResultSet resultSet = null;
        try {

            Connection connection = getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);

//            while (resultSet.next()) {
//                String area = resultSet.getString(0);// 获取字段值
//                P.print("area:" + area);
//            }

            List<T> list = ResultSetHelper.toKbaseList(resultSet, type,dbfields);

            return list;

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try{
                if(resultSet!=null)
                    resultSet.close();
                if(statement!=null)
                    statement.close();
                if(connection!=null)
                    connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return null;
    }
}
