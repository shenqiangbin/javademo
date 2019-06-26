package dbmgr.kbaseAccess;

import com.kbase.jdbc.ResultSetImpl;
import com.kbase.jdbc.StatementImpl;
import com.mysql.cj.util.StringUtils;
import com.zaxxer.hikari.HikariConfig;
import common.P;
import dbmgr.microsoftAccess.ResultSetHelper;
import dbmgr.mySqlAccess.MySqlHelper;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class KBaseHelper {

    private String jdbcUrl;
    private String userName;
    private String password;

    private Connection connection = null;

    public KBaseHelper(String jdbcUrl, String userName, String password) {

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

    public <T> List<T> query(String sql, List<Object> params, Class<T> type, String[] dbfields) {
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

            List<T> list = ResultSetHelper.toKbaseList(resultSet, type, dbfields);

            return list;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null)
                    resultSet.close();
                if (statement != null)
                    statement.close();
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public void tmpSync(String sql, String[] dbfields, HikariConfig config) {

        MySqlHelper mySqlHelper = new MySqlHelper(config);

        Statement statement = null;
        ResultSet resultSet = null;
        try {

            Connection connection = getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {

                StringBuilder builder = new StringBuilder();
                builder.append("insert guizhou500 values(");
                for (int i = 0; i < 55; i++) {

                    String area = resultSet.getString(i);// 获取字段值
                    if (StringUtils.isEmptyOrWhitespaceOnly(area))
                        area = "0";
                    area = area.replace("######", "");
                    if (StringUtils.isEmptyOrWhitespaceOnly(area))
                        area = "0";


                    if (i == 54)
                        builder.append("'").append(area).append("'");
                    else
                        builder.append("'").append(area).append("',");
                }

                builder.append(")");
                P.print(builder.toString());


                mySqlHelper.insert(builder.toString());


            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null)
                    resultSet.close();
                if (statement != null)
                    statement.close();
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


//        StringBuilder builder = new StringBuilder();
//        String now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
//        now = "2019-01-09 21:46:04";

    }

    public void showRs() throws SQLException {

        Statement statement = null;
        ResultSet resultSet = null;
        String sql = "SELECT MODEL_KBASEID,MODELNAME,MODELMEMO,SOURCEID,STATUS,SYNCSTATUS,IMAGEURl,CREATEUSER,CREATEON,ISPUBLISH FROM MODEL_KBASE where  status = 1  and createUser = guizhoujiaotong and isboat = 1 order by createon asc";

        Connection connection = getConnection();
        statement = connection.createStatement();
        ResultSetImpl krs = (ResultSetImpl) statement.executeQuery(sql);

        resultSet = krs;

        int rows = ((StatementImpl) statement).getRows();

        int row = 0;
        boolean isSuccess = resultSet.absolute(row);
        int totalRow = resultSet.getRow();

        int pageSize = 9;
        int m = 0;
        while (m < pageSize && !resultSet.isAfterLast()) {
            resultSet.next();
            String value1 = resultSet.getString(0);
            String value2 = resultSet.getString(1);
            String value3 = resultSet.getString(2);
        }
    }
}
