package dbmgr.mySqlAccess;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.*;
import java.text.MessageFormat;
import java.util.LinkedHashMap;
import java.util.List;

public class MySqlHelper {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private DataSource dataSource;

    public MySqlHelper() {
    }

    public MySqlHelper(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public <T> List<T> simpleQuery(String sql, Object[] params, Class<T> type) throws Exception {

        Connection connection = null;
        List<T> list = null;
        try {
            connection = dataSource.getConnection();

            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    statement.setObject(i + 1, params[i] == null ? "" : params[i]);
                }
            }
            ResultSet resultSet = statement.executeQuery();

            list = ResultSetHelper.toList(resultSet, type);

        } finally {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        }

        return list;
    }

    public void simpleQuery(String sql, Object[] params, IResultHandler resultHandler) throws Exception {

        Connection connection = null;
        try {
            connection = dataSource.getConnection();

            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    statement.setObject(i + 1, params[i] == null ? "" : params[i]);
                }
            }
            ResultSet resultSet = statement.executeQuery();

            if (resultHandler != null) {
                resultHandler.handle(resultSet);
            }

        } finally {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        }

    }

    public void simpleQuery(Connection connection, String sql, Object[] params, IResultHandler resultHandler) throws Exception {

        try {
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    statement.setObject(i + 1, params[i] == null ? "" : params[i]);
                }
            }
            ResultSet resultSet = statement.executeQuery();

            if (resultHandler != null) {
                resultHandler.handle(resultSet);
            }

        } finally {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        }

    }

    public <T> List<T> simpleQuery(Connection connection, String sql, Object[] params, Class<T> type) throws Exception {

        List<T> list = null;
        try {
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    statement.setObject(i + 1, params[i] == null ? "" : params[i]);
                }
            }
            ResultSet resultSet = statement.executeQuery();

            list = ResultSetHelper.toList(resultSet, type);

        } finally {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        }
        return list;
    }

    public List<LinkedHashMap<String, Object>> simpleQuery(Connection connection, String sql, Object[] params) throws Exception {

        List<LinkedHashMap<String, Object>> list = null;
        try {
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    statement.setObject(i + 1, params[i] == null ? "" : params[i]);
                }
            }

            ResultSet resultSet = statement.executeQuery();

            list = ResultSetHelper.toLinkedList(resultSet);

        } finally {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        }

        return list;
    }

    public List<LinkedHashMap<String, Object>> simpleQuery(Connection connection, String sql) throws Exception {

        List<LinkedHashMap<String, Object>> list = null;
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            list = ResultSetHelper.toLinkedList(resultSet);

        } finally {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        }

        return list;
    }

    public List<LinkedHashMap<String, Object>> simpleQuery(String sql, Object[] params) throws Exception {

        Connection connection = null;
        List<LinkedHashMap<String, Object>> list = null;
        try {
            connection = dataSource.getConnection();

            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    statement.setObject(i + 1, params[i] == null ? "" : params[i]);
                }
            }

            ResultSet resultSet = statement.executeQuery();

            list = ResultSetHelper.toLinkedList(resultSet);

        } finally {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        }

        return list;
    }

    public int add(String sql, Object[] params) throws SQLException {
        Connection connection = null;
        int result = -1;
        try {
            connection = dataSource.getConnection();

            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    statement.setObject(i + 1, params[i] == null ? "" : params[i]);
                }
            }

            statement.execute();

            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet != null) {
                if (resultSet.next()) {
                    result = resultSet.getInt(1);
                }
            }

        } finally {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        }
        return result;
    }

    public int update(String sql, Object[] params) throws SQLException {

        Connection connection = null;
        try {
            connection = dataSource.getConnection();

            PreparedStatement statement = connection.prepareStatement(sql);

            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    statement.setObject(i + 1, params[i]);
                }
            }

            return statement.executeUpdate();
        } finally {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        }
    }

    public int update(Connection connection, String sql, Object[] params) throws SQLException {
        try {
            PreparedStatement statement = connection.prepareStatement(sql);

            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    statement.setObject(i + 1, params[i]);
                }
            }

            return statement.executeUpdate();
        } finally {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        }
    }

    public String executeScalar(String sql, List<Object> params) throws SQLException {
        Connection connection = null;
        try {
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
        return null;
    }

    public String executeScalar(Connection connection, String sql, List<Object> params) throws SQLException {
        try {
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

    public void execute(String sql, List<Object> params) throws SQLException {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            if (params != null) {
                for (int i = 0; i < params.size(); i++) {
                    statement.setObject(i + 1, params.get(i));
                }
            }

            statement.execute();
        } finally {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        }
    }

    public void execute(String sql) throws SQLException {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            Statement statement = connection.createStatement();
            statement.execute(sql);
        } finally {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        }
    }

    public void execute(Connection connection, String sql) throws SQLException {
        try {
            Statement statement = connection.createStatement();
            statement.execute(sql);
        } finally {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        }
    }

    public void executeSqlBatch(String sql, List<List<Object>> paramsList) throws SQLException {

        Connection connection = null;
        StringBuilder info = new StringBuilder(sql);
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.clearBatch();
            if (paramsList != null) {
                for (int i = 0; i < paramsList.size(); i++) {
                    info.append("\r\n");
                    List<Object> params = paramsList.get(i);
                    if (params != null) {
                        for (int j = 0; j < params.size(); j++) {
                            statement.setObject(j + 1, params.get(j));
                            info.append(params.get(j));
                        }
                        statement.addBatch();
                    }
                }
            }

            statement.executeBatch();

            connection.commit();
            connection.setAutoCommit(true);

        } finally {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        }

    }

    public void executeSqlBatch(Connection connection, String sql, List<List<Object>> paramsList) throws SQLException {
        StringBuilder info = new StringBuilder(sql);
        try {
            connection.setAutoCommit(false);
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.clearBatch();
            if (paramsList != null) {
                for (int i = 0; i < paramsList.size(); i++) {
                    info.append("\r\n");
                    List<Object> params = paramsList.get(i);
                    if (params != null) {
                        for (int j = 0; j < params.size(); j++) {
                            statement.setObject(j + 1, params.get(j));
                            info.append(params.get(j));
                        }
                        statement.addBatch();
                    }
                }
            }

            statement.executeBatch();

            connection.commit();
            connection.setAutoCommit(true);

        } finally {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        }

    }
//
//    public PagedResponse<LinkedHashMap<String, Object>> queryPage(String sql, String sumSql, int pageIndex, int pageSize) throws SQLException {
//        PagedResponse<LinkedHashMap<String, Object>> result = new PagedResponse<>();
//        result.setPageSize(pageSize);
//        result.setCurrentPage(pageIndex);
//        Connection conn = null;
//        ResultSet rs = null;
//        Statement pst = null;
//        try {
//
//            conn = dataSource.getConnection();
//            pst = conn.createStatement();
//
//            int startIndex = (pageIndex - 1) * pageSize;
//            String querySql = String.format("%s limit %s,%s", sql, startIndex, pageSize);
//
//            rs = pst.executeQuery(querySql);
//            if (rs == null) {
//                return null;
//            }
//
//            // 或正则，将select 和 from 中间的字符串替换成 count(0);
//            String countSql = MessageFormat.format("select count(0) from ({0})t ", sql);
//            if (!StringUtils.isEmpty(sumSql)) {
//                countSql = sumSql;
//            }
//            String countStr = executeScalar(countSql, null);
//            int rows = 0;
//            if (!StringUtils.isEmpty(countStr)) {
//                rows = Integer.parseInt(countStr);
//            }
//
//            result.setTotalCount(rows);
//
//            int totalPage = rows / pageSize + (rows % pageSize == 0 ? 0 : 1);
//            result.setTotalPage(totalPage);
//
//            List<LinkedHashMap<String, Object>> list = ResultSetHelper.toLinkedList(rs);
//            result.setList(list);
//
//        } finally {
//            try {
//                if (rs != null && !rs.isClosed()) {
//                    rs.close();
//                }
//                if (pst != null && !pst.isClosed()) {
//                    pst.close();
//                }
//                if (conn != null && !conn.isClosed()) {
//                    conn.close();
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        return result;
//    }
//
//    public PagedResponse<LinkedHashMap<String, Object>> queryPage(Connection connection,int dsType, String sql, String sumSql, int pageIndex, int pageSize) throws SQLException, ClassNotFoundException {
//        PagedResponse<LinkedHashMap<String, Object>> result = new PagedResponse<>();
//        result.setPageSize(pageSize);
//        result.setCurrentPage(pageIndex);
//        ResultSet rs = null;
//        Statement pst = null;
//        try {
//            pst = connection.createStatement();
//
//            int startIndex = (pageIndex - 1) * pageSize;
//            String querySql = "";
//            if(dsType == 1){ // mysql
//                querySql = String.format("%s limit %s,%s", sql, startIndex, pageSize);
//            }else if(dsType == 2){ // postgresql
//                querySql = String.format("%s limit %s offset %s", sql, pageSize, startIndex);
//            }
//
//            rs = pst.executeQuery(querySql);
//            if (rs == null) {
//                return null;
//            }
//
//            List<LinkedHashMap<String, Object>> list = ResultSetHelper.toLinkedList(rs);
//            result.setList(list);
//
//            // 或正则，将select 和 from 中间的字符串替换成 count(0);
//            String countSql = MessageFormat.format("select count(0) from ({0})t ", sql);
//            if (!StringUtils.isEmpty(sumSql)) {
//                countSql = sumSql;
//            }
//            String countStr = executeScalar(connection, countSql, null);
//            int rows = 0;
//            if (!StringUtils.isEmpty(countStr)) {
//                rows = Integer.parseInt(countStr);
//            }
//
//            result.setTotalCount(rows);
//
//            int totalPage = rows / pageSize + (rows % pageSize == 0 ? 0 : 1);
//            result.setTotalPage(totalPage);
//
//
//
//        } finally {
//            try {
//                if (rs != null && !rs.isClosed()) {
//                    rs.close();
//                }
//                if (pst != null && !pst.isClosed()) {
//                    pst.close();
//                }
//                if (connection != null && !connection.isClosed()) {
//                    connection.close();
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        return result;
//    }
//
//    public <T> PagedResponse<T> queryPage(String sql, String sumSql, int pageIndex, int pageSize, Class<T> type) throws SQLException, IllegalAccessException, NoSuchFieldException, InstantiationException {
//        PagedResponse<T> result = new PagedResponse<>();
//        result.setPageSize(pageSize);
//        result.setCurrentPage(pageIndex);
//        Connection conn = null;
//        ResultSet rs = null;
//        Statement pst = null;
//        try {
//
//            conn = dataSource.getConnection();
//            pst = conn.createStatement();
//
//            int startIndex = (pageIndex - 1) * pageSize;
//            String querySql = String.format("%s limit %s,%s", sql, startIndex, pageSize);
//
//            rs = pst.executeQuery(querySql);
//            if (rs == null) {
//                return null;
//            }
//
//            // 或正则，将select 和 from 中间的字符串替换成 count(0);
//            String countSql = MessageFormat.format("select count(0) from ({0})t ", sql);
//            if (!StringUtils.isEmpty(sumSql)) {
//                countSql = sumSql;
//            }
//            String countStr = executeScalar(countSql, null);
//            int rows = 0;
//            if (!StringUtils.isEmpty(countStr)) {
//                rows = Integer.parseInt(countStr);
//            }
//
//            result.setTotalCount(rows);
//
//            int totalPage = rows / pageSize + (rows % pageSize == 0 ? 0 : 1);
//            result.setTotalPage(totalPage);
//
//            List<T> list = ResultSetHelper.toList(rs, type);
//            result.setList(list);
//
//        } finally {
//            try {
//                if (rs != null && !rs.isClosed()) {
//                    rs.close();
//                }
//                if (pst != null && !pst.isClosed()) {
//                    pst.close();
//                }
//                if (conn != null && !conn.isClosed()) {
//                    conn.close();
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        return result;
//    }

}
