package dbmgr.microsoftAccess;

import dbmgr.microsoftAccess.model.PageResult;

import java.sql.*;
import java.text.MessageFormat;
import java.util.List;

/**
 * java jdk1.8 版本下 访问 access 数据库<br/>
 * 使用时，项目中需要引入 UCanAccess-4.0.4-bin 目录下的 jar 包文件<br/>
 * 测试支持*.mdb文件类型
 */
public class AccessHelper {

    private Connection connection = null;

    private String dbName;

    public AccessHelper(String dbName) {
        this.dbName = dbName;

        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
        } catch (ClassNotFoundException cnfex) {
            System.out.println("Problem in loading MS Access JDBC driver");
            cnfex.printStackTrace();
        }
    }

    private Connection getConnection() {
        String dbURL = "jdbc:ucanaccess://" + this.dbName;
        try {

            if (connection == null)
                connection = DriverManager.getConnection(dbURL);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return connection;
    }

    public <T> List<T> query(String sql, List<Object> params, Class<T> type) {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {

            Connection connection = getConnection();

            statement = connection.prepareStatement(sql);

            if (params != null) {
                for (int i = 0; i < params.size(); i++) {
                    statement.setObject(i + 1, params.get(i));
                }
            }

            resultSet = statement.executeQuery();
            List<T> list = ResultSetHelper.toList(resultSet, type);

            return list;

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try{
                if(resultSet!=null)
                    resultSet.close();
                if(statement!=null)
                    statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public <T> PageResult<T> queryPage(String sql, List<Object> params, int currentPage, int pageSize, Class<T> type) {

        PageResult<T> result = new PageResult<T>();

        int startIndex = (currentPage - 1) * pageSize;

        String querySql = MessageFormat.format("{0} limit {1},{2}", sql, startIndex, pageSize);
        List<T> models = this.query(querySql, params, type);
        result.setData(models);

        // 或正则，将select 和 from 中间的字符串替换成 count(0);
        String countSql = MessageFormat.format("select count(0) from ({0})t ", sql);
        String countStr = this.executeScalar(countSql, params);
        int countVal = 0;
        if (!isBlank(countStr))
            countVal = Integer.parseInt(countStr);

        int totalPage = (int) ((countVal + pageSize - 1) / pageSize);

        result.setTotalCount(countVal);
        result.setTotalPage(totalPage);

        return result;
    }

    private boolean isBlank(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (Character.isWhitespace(str.charAt(i)) == false) {
                return false;
            }
        }
        return true;
    }

    public int add(String sql,List<Object> params){

        int result = 0;

        try {
            Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);

            if (params != null) {
                for (int i = 0; i < params.size(); i++) {
                    statement.setObject(i + 1, params.get(i));
                }
            }

            statement.execute();

            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet != null) {
                if (resultSet.next())
                    result = resultSet.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    public int update(String sql,List<Object> params) {
        try {
            Connection connection = getConnection();

            PreparedStatement statement = connection.prepareStatement(sql);

            if (params != null) {
                for (int i = 0; i < params.size(); i++) {
                    statement.setObject(i + 1, params.get(i));
                }
            }

            return statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public String executeScalar(String sql, List<Object> params) {
        String result = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {

            Connection connection = getConnection();

            statement = connection.prepareStatement(sql);

            if (params != null) {
                for (int i = 0; i < params.size(); i++) {
                    statement.setObject(i + 1, params.get(i));
                }
            }

            resultSet = statement.executeQuery();
            if(resultSet!=null && resultSet.next())
                result = resultSet.getString(1);

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try{
                if(resultSet!=null)
                    resultSet.close();
                if(statement!=null)
                    statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return result;
    }


}
