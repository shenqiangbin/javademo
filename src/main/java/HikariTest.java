import JSONDemo.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import common.P;
import org.apache.commons.lang3.StringUtils;

import javax.sound.midi.SysexMessage;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

public class HikariTest {

    private static HikariDataSource ds = null;

    public static void main(String[] args) throws Exception{

        //JSON.parse()
        int sourceId = 19;
        String tableCode = "中医呼吸专题数据集-20a03bccce1845d680224e023441f907-Sheet1";
        System.out.println(JSON.toJSONString(tableCode));
        String str = "[{\"action\":\"0\",\"childCateField\":\"\",\"createTimeFormat\":\"2018-09-11 20:33:53\",\"dataType\":0,\"dbCode\":\"model_kbase\",\"dbName\":\"数据模型\",\"detailUrlFormat\":\"modelandboat\",\"fullTextField\":\"知识元\",\"id\":1,\"isCorpus\":0,\"isDeleted\":0,\"isPublish\":1,\"isPublishFormat\":\"已发布\",\"isSystem\":1,\"parentCateField\":\"\",\"parentId\":0,\"sortCode\":1,\"sourceId\":1,\"tableId\":867,\"tableName\":\"model_kbase\",\"updateTime\":1557580004000,\"updateTimeFormat\":\"2019-05-11 21:06:44\"},{\"action\":\"0\",\"createTimeFormat\":\"2021-04-02 10:19:33\",\"dataType\":0,\"dbCode\":\"中医呼吸专题数据集-20a03bccce1845d680224e023441f907-Sheet1\",\"dbName\":\"中医呼吸专题数据集\",\"dbTag\":\"\",\"detailUrlFormat\":\"\",\"id\":2596,\"isCorpus\":0,\"isDeleted\":0,\"isPublish\":1,\"isPublishFormat\":\"已发布\",\"isSystem\":0,\"parentId\":0,\"sortCode\":1,\"sourceId\":19,\"tableId\":3,\"tableName\":\"中医呼吸专题数据集-20a03bccce1845d680224e023441f907-Sheet1\",\"updateTime\":1617344003000,\"updateTimeFormat\":\"2021-04-02 14:13:23\"},{\"action\":\"0\",\"createTimeFormat\":\"2019-12-26 20:14:16\",\"dataType\":0,\"dbCode\":\"报纸\",\"dbName\":\"报纸\",\"detailUrlFormat\":\"https://kns.cnki.net/KCMS/detail/detail.aspx?dbname={dbcode}&filename={filename}\",\"id\":2513,\"isCorpus\":0,\"isDeleted\":0,\"isPublish\":1,\"isPublishFormat\":\"已发布\",\"isSystem\":1,\"parentId\":0,\"sortCode\":1,\"sourceId\":201,\"tableId\":2543,\"tableName\":\"报纸\",\"updateTime\":1577362772000,\"updateTimeFormat\":\"2019-12-26 20:19:32\"},{\"action\":\"0\",\"createTimeFormat\":\"2019-12-26 20:17:48\",\"dataType\":0,\"dbCode\":\"报纸\",\"dbName\":\"报纸\",\"detailUrlFormat\":\"https://kns.cnki.net/KCMS/detail/detail.aspx?dbname={dbcode}&filename={filename}\",\"id\":2514,\"isCorpus\":0,\"isDeleted\":0,\"isPublish\":1,\"isPublishFormat\":\"已发布\",\"isSystem\":1,\"parentId\":0,\"sortCode\":1,\"sourceId\":201,\"tableId\":2544,\"tableName\":\"报纸\",\"updateTime\":1577362772000,\"updateTimeFormat\":\"2019-12-26 20:19:32\"},{\"action\":\"0\",\"createTimeFormat\":\"2019-12-27 09:38:17\",\"dataType\":0,\"dbCode\":\"会议\",\"dbName\":\"会议\",\"detailUrlFormat\":\"https://kns.cnki.net/KCMS/detail/detail.aspx?dbname={dbcode}&filename={filename}\",\"id\":2515,\"isCorpus\":0,\"isDeleted\":0,\"isPublish\":1,\"isPublishFormat\":\"已发布\",\"isSystem\":1,\"parentId\":0,\"sortCode\":1,\"sourceId\":201,\"tableId\":2545,\"tableName\":\"会议\",\"updateTime\":1577410783000,\"updateTimeFormat\":\"2019-12-27 09:39:43\"},{\"action\":\"0\",\"createTimeFormat\":\"2019-12-27 10:37:38\",\"dataType\":0,\"dbCode\":\"期刊\",\"dbName\":\"期刊\",\"detailUrlFormat\":\"https://kns.cnki.net/KCMS/detail/detail.aspx?dbname={dbcode}&filename={filename}\",\"id\":2517,\"isCorpus\":0,\"isDeleted\":0,\"isPublish\":1,\"isPublishFormat\":\"已发布\",\"isSystem\":1,\"parentId\":0,\"sortCode\":1,\"sourceId\":201,\"tableId\":2547,\"tableName\":\"期刊\",\"updateTime\":1577414417000,\"updateTimeFormat\":\"2019-12-27 10:40:17\"},{\"action\":\"0\",\"createTimeFormat\":\"2019-12-27 11:02:59\",\"dataType\":0,\"dbCode\":\"博硕士\",\"dbName\":\"博硕士\",\"detailUrlFormat\":\"https://kns.cnki.net/KCMS/detail/detail.aspx?dbname={dbcode}&filename={filename}\",\"id\":2518,\"isCorpus\":0,\"isDeleted\":0,\"isPublish\":1,\"isPublishFormat\":\"已发布\",\"isSystem\":1,\"parentId\":0,\"sortCode\":1,\"sourceId\":201,\"tableId\":2548,\"tableName\":\"博硕士\",\"updateTime\":1577415848000,\"updateTimeFormat\":\"2019-12-27 11:04:08\"},{\"action\":\"0\",\"createTimeFormat\":\"2018-09-11 20:33:53\",\"dataType\":0,\"dbCode\":\"report_kbase\",\"dbName\":\"智能报告\",\"detailUrlFormat\":\"report\",\"fullTextField\":\"知识元\",\"id\":2,\"isCorpus\":0,\"isDeleted\":0,\"isPublish\":1,\"isPublishFormat\":\"已发布\",\"isSystem\":1,\"parentId\":0,\"sortCode\":3,\"sourceId\":2,\"tableId\":860,\"tableName\":\"report_kbase\",\"updateTime\":1557581756000,\"updateTimeFormat\":\"2019-05-11 21:35:56\"}]";
        //List<SearchDatabase> databaseList  = JSONUtil.toObject(str, SearchDatabase.class);
        List<SearchDatabase> databaseList  =      JSON.parseArray(str, SearchDatabase.class);

        List<SearchDatabase> filterHashItems = databaseList.stream().filter(n -> n.getSourceId() == sourceId && n.getDbCode().equals(tableCode)).collect(Collectors.toList());
        if (filterHashItems.size() > 0) {
            System.out.println("123");
        }
        //log.debug("filterHashItems = " + JSON.toJSONString(filterHashItems));

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
