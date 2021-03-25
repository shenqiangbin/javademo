import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import common.P;
import org.apache.commons.lang3.StringUtils;

import javax.sound.midi.SysexMessage;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HikariTest {

    private static HikariDataSource ds = null;

    public static void main(String[] args) throws Exception{
        P.print("hello");

        List<String> list = new ArrayList();
        list.add("abc");
        list.add("abc");

        Set<String> list2 = new HashSet<>();
        list2.add("abc");
        list2.add("abc");
        list2.add("1");
        System.out.println(StringUtils.join(list2,","));


        String sql = "select * from resourceitem";
//        for (int i = 0; i < 280000; i++) {
//            //Thread.sleep(1000);
//
//            P.print(i);
//            simpleQuery(sql);
//        }

    }

    private static HikariDataSource getDataSource() throws SQLException {

        HikariConfig config = new HikariConfig();

        config.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/javablog?useUnicode=true&characterEncoding=utf8&useSSL=false");
        config.setUsername("root");
        config.setPassword("123456");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmptCacheSqlLimit", "2048");

        //if(ds == null)
           // ds = new HikariDataSource(config);
        return new HikariDataSource(config);
    }

    public static ResultSet simpleQuery(String sql) throws  Exception {

            HikariDataSource dataSource = getDataSource();
            Connection connection = dataSource.getConnection();

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            //printResultSet(resultSet);

            if (connection != null && !connection.isClosed())
                connection.close();
            if (dataSource != null && !dataSource.isClosed())
                dataSource.close();




        return null;
    }

    public static void printResultSet(ResultSet resultSet) throws SQLException {
        if (resultSet != null) {
            ResultSetMetaData md = resultSet.getMetaData();// 获取键名
            int columnCount = md.getColumnCount();// 获取行的数量

            while (resultSet.next()) {
                StringBuilder builder = new StringBuilder();

                for (int i = 1; i <= columnCount; i++) {
                    String colName = md.getColumnName(i);
                    Object val = resultSet.getObject(i);

                    //builder.append(MessageFormat.format("{0}:{1}", colName, val));
                    builder.append(new StringBuilder().append(colName).append(":").append(val));
                }

                P.print(builder.toString());
            }
        }
    }

}
