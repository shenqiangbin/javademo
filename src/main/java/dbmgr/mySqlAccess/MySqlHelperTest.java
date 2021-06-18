package dbmgr.mySqlAccess;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import common.P;
import dbmgr.kbaseAccess.KbaseTestItem;
import org.apache.commons.lang3.StringUtils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MySqlHelperTest {

    public static void main(String[] args) throws Exception {

        System.out.println("abc");

        String text1 = "\nabc";
        System.out.println(text1);

        String text2 = text1.replace("\n", "\\n");
        System.out.println(text2);

        String text3 = text2.replace("\\n", "\n");
        System.out.println(text3);

        HikariDataSource dataSource = new HikariDataSource(getConfig());
        MySqlHelper mySqlHelper = new MySqlHelper(dataSource);

        String sql = "select * from file where name like ?";
        List<Object> paraList = new ArrayList<>();
        paraList.add("妹子%");
        List<MySqlTestItem> list = mySqlHelper.simpleQuery(sql, paraList.toArray(), MySqlTestItem.class);

        List<List<Object>> paraBatch = new ArrayList<>();
        paraBatch.add(paraList);

        paraList = new ArrayList<>();
        paraList.add("妹子");
        paraBatch.add(paraList);


        paraList = new ArrayList<>();
        paraList.add(3);
        paraBatch.add(paraList);

        List<MySqlTestItem> listBatch = mySqlHelper.simpleQueryBatch(sql, paraBatch, MySqlTestItem.class, 2);
        for (MySqlTestItem item : list) {
            P.print(item.getName());
        }
//
//        mySqlHelper.insert("insert resourceitem(name,url) values('my1','my1')");

//        String sql2 = "select * from t137619";
//
//        mySqlHelper.simpleQuery(sql2,MySqlTestItem.class);
//
//        int i=137619;
//        while (i>0){
//            String sql = "CREATE TABLE `t1` (\n" +
//                    "  `id` int(11) NOT NULL AUTO_INCREMENT,\n" +
//                    "  PRIMARY KEY (`id`),\n" +
//                    "  UNIQUE KEY `id_UNIQUE` (`id`)\n" +
//                    ") ENGINE=InnoDB DEFAULT CHARSET=uprintResultSettf8;";
//            sql = sql.replace("t1","t"+i);
//            mySqlHelper.insert(sql);
//            i++;
//            System.out.println(i);
//        }

        /*
         * 执行前先备份
         * create table bd.model_udpate_layout SELECT *  FROM bd.model;
         * create table bd.modelconfig_udpate_layout SELECT *  FROM bd.modelconfig;
         * */

        //HikariDataSource dataSource = new HikariDataSource(getConfig());
        Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement();

        //String sql = "select modelid,jsondata from bd.model_udpate_layout where modelid = 488846 order by CreateTime desc";
        sql = "select modelid,jsondata from bd.model_udpate_layout2  where modelid  in (488979,488978) order by CreateTime desc";

        ResultSet resultSet = statement.executeQuery(sql);

        if (resultSet != null) {
            while (resultSet.next()) {
                String modelid = resultSet.getString("modelid");
                String jsondata = resultSet.getString("jsondata");

                System.out.println("modelid:" + modelid);
                System.out.println("jsondata:" + jsondata);

                if (StringUtils.isNotEmpty(jsondata) && StringUtils.isNotBlank(jsondata)) {
                    JSONArray arr = JSON.parseArray(jsondata);
                    for (int index = 0; index < arr.size(); index++) {
                        JSONObject obj = arr.getJSONObject(index);
                        Integer xVal = obj.getInteger("x");
                        Integer wVal = obj.getInteger("w");
                        Integer yVal = obj.getInteger("y");
                        Integer hVal = obj.getInteger("h");

                        Integer xValv = xVal / 50;
                        Integer wValv = wVal / 50;
                        Integer yValv = yVal / 10;
                        Integer hValv = hVal / 10;

                        arr.getJSONObject(index).put("x", xValv);
                        arr.getJSONObject(index).put("w", wValv);
                        arr.getJSONObject(index).put("y", yValv);
                        arr.getJSONObject(index).put("h", hValv);

                    }
                    String text = JSON.toJSONString(arr);
                    System.out.println("text:" + text);

                    updateModel(dataSource, text, modelid);
                }
            }
        }


        //resultSet = statement.executeQuery("select modelconfigid,Layout from bd.modelconfig_udpate_layout where modelid = 488846  order by CreateTime desc;");
        resultSet = statement.executeQuery("select modelconfigid,Layout from bd.modelconfig_udpate_layout2  where modelid  in (488979,488978) order by CreateTime desc;");

        if (resultSet != null) {
            while (resultSet.next()) {
                String modelconfigid = resultSet.getString("modelconfigid");
                String layout = resultSet.getString("Layout");

                System.out.println("modelconfigid:" + modelconfigid);
                System.out.println("layout:" + layout);

                if (StringUtils.isNotEmpty(layout) && StringUtils.isNotBlank(layout)) {
                    JSONObject obj = JSON.parseObject(layout);
                    Integer xVal = obj.getInteger("x");
                    Integer wVal = obj.getInteger("w");
                    Integer hVal = obj.getInteger("h");

                    if (xVal >= 0 && xVal >= 50) {
                        obj.put("x", xVal / 50);
                    } else {
                        obj.put("x", 0);
                    }

                    if (wVal != null && wVal >= 0 && wVal >= 50)
                        obj.put("w", wVal / 50);

//                    if (hVal >= 0 && hVal >= 10) {
//                        obj.put("h", hVal / 10);
//                    }else{
//                        obj.put("h", 10);
//                    }

                    String text = JSON.toJSONString(obj);
                    System.out.println("text:" + text);

                    updateModelConfig(dataSource, text, modelconfigid);
                }
            }
        }

        System.out.println("完");
    }

    public static void updateModel(HikariDataSource dataSource, String jsonData, String modelid) throws Exception {
        Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement();
        jsonData = jsonData.replace("\"", "\\\"");
        jsonData = jsonData.replace("\'", "\\\'");
        jsonData = jsonData.replace("\\n", "\\\\n");

        String sql = String.format("update model set  jsondata = '%s' where modelid = %s", jsonData, modelid);
        System.out.println(sql);
        statement.execute(sql);
        connection.close();


    }

    public static void updateModelConfig(HikariDataSource dataSource, String data, String modelconfigid) throws Exception {
        Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement();
        String sql = String.format("update modelconfig set layout = '%s' where modelconfigid = %s", data, modelconfigid);
        System.out.println(sql);
        statement.execute(sql);
        connection.close();
    }

    public static HikariConfig getConfig() {

        HikariConfig config = new HikariConfig();

        //config.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/test?useUnicode=true&characterEncoding=utf8&useSSL=false");

        // config.setJdbcUrl("jdbc:mysql://192.168.100.92:3306/bd?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=UTC");
        config.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/lab?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=UTC");
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setUsername("root");
        config.setPassword("123456");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmptCacheSqlLimit", "2048");

        return config;
    }
}
