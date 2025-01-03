package dbmgr.kbaseAccess;

import com.kbase.jdbc.ConnectionImpl;
import com.kbase.jdbc.ResultSetImpl;
import com.kbase.jdbc.StatementImpl;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import common.P;
import dbmgr.microsoftAccess.ResultSetHelper;
import dbmgr.mySqlAccess.MySqlHelper;
import kbase.KBaseClient;
import kbase.struct.TPI_RETURN_RESULT;
import org.apache.commons.lang3.StringUtils;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
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

    public void dispose()  {
        try {
            if (this.connection != null && !this.connection.isClosed()) {
                this.connection.close();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public String execute(String sql, List<Object> params) throws SQLException {
        Connection connection = null;
        try {
            connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            if (params != null) {
                for (int i = 0; i < params.size(); i++) {
                    statement.setString(i + 1, params.get(i).toString());
                }
            }

            statement.execute();

        } finally {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        }
        return null;
    }

    public boolean execute(String sql) throws SQLException {
        Connection connection = null;
        try {
            connection = getConnection();
            Statement statement = connection.createStatement();
            return statement.execute(sql);
        } finally {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        }
    }

    /**
     * 提交到后台执行
     * @param sql
     * @return
     * @throws SQLException
     */
    public int executeNoQueryWithBack(String sql) throws SQLException {
        int rs = 0;
        Connection conn = null;
        Statement pst = null;
        try {

            conn = getConnection();
            ConnectionImpl kbaseconnect = (ConnectionImpl) conn;
            KBaseClient client = kbaseconnect.getKbaseClient();
            int connectHSet = kbaseconnect.getConnectionHset();
            boolean bUnicode = false;
            int pEventHandle = 0;
            TPI_RETURN_RESULT sresult = client.KBase_ExecMgrSqlEx(connectHSet, sql, pEventHandle, bUnicode);
//            int nRetCode = KBaseJNA.TPI_ERR_EVENTNOEND;
//            while (nRetCode == KBaseJNA.TPI_ERR_EVENTNOEND) {
//                Thread.sleep(2000);//2秒监听一次
//                nRetCode = client.KBase_QueryEvent(connectHSet, sresult.rtnInt);
//            }
            //rs = nRetCode;
        } finally {
            conn.close();
        }
        return rs;
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

    public List<LinkedHashMap<String, Object>> query(String sql, String[] dbfields) throws SQLException {
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

            List<LinkedHashMap<String, Object>> list = ResultSetHelper.toKbaseLinkedList(resultSet, dbfields);
            return list;

        } finally {
//            try {
//                if (resultSet != null)
//                    resultSet.close();
//                if (statement != null)
//                    statement.close();
//                if (connection != null)
//                    connection.close();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
        }
    }


    public void query(String sql, IResultHandler resultHandler) throws SQLException {

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

            if(resultHandler!=null)
                resultHandler.handle(resultSet);

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
    }

    public void tmpSync(String sql, String[] dbfields, HikariConfig config) {

        HikariDataSource dataSource = new HikariDataSource(config);
        MySqlHelper mySqlHelper = new MySqlHelper(dataSource);

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
                    if (StringUtils.isEmpty(area))
                        area = "0";
                    area = area.replace("######", "");
                    if (StringUtils.isEmpty(area))
                        area = "0";


                    if (i == 54)
                        builder.append("'").append(area).append("'");
                    else
                        builder.append("'").append(area).append("',");
                }

                builder.append(")");
                P.print(builder.toString());


                mySqlHelper.add(builder.toString(),null);


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
