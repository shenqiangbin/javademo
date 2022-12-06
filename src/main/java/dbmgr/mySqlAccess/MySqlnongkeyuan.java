package dbmgr.mySqlAccess;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import common.P;
import dbmgr.mySqlAccess.model.Item;
import fileDemo.FileHelper;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.jcodings.util.Hash;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

public class MySqlnongkeyuan {

    public static void main(String[] args) throws Exception {
//        String filePath = "C:\\Users\\cnki52\\Desktop\\光明\\jsons\\farming.author.organization";
//        guangming(filePath,"framing");
//
//        filePath = "C:\\Users\\cnki52\\Desktop\\光明\\jsons\\fishery.author.organization";
//        guangming(filePath,"fishery");
//
//        filePath = "C:\\Users\\cnki52\\Desktop\\光明\\jsons\\food.author.organization";
//        guangming(filePath,"food");
//
//        filePath = "C:\\Users\\cnki52\\Desktop\\光明\\jsons\\livestock.author.organization";
//        guangming(filePath,"livestock");

        String filePath = "C:\\Users\\cnki52\\Desktop\\光明\\jsons\\merge.author.organization";
        guangming(filePath, "merge1123");
    }

    static HikariDataSource dataSource = new HikariDataSource(getOnlineConfig());
    static Connection connection;

    static {
        try {
            connection = dataSource.getConnection();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    static HashMap<String, Boolean> words = new HashMap<>();

    /**
     * 判断词是否已经存在
     *
     * @param word
     * @return
     * @throws SQLException
     */
    public static boolean wordExists(String word) throws SQLException {

        if (words.containsKey(word)) {
            return words.get(word);
        }

        String sql = "select count(0) from dwd_expert_ins_rel where  word = ? AND TYPE = 0";

        List<Object> params = Arrays.asList(new String[]{word});

        PreparedStatement statement = connection.prepareStatement(sql);

        if (params != null) {
            for (int i = 0; i < params.size(); i++) {
                statement.setObject(i + 1, params.get(i));
            }
        }

        ResultSet resultSet = statement.executeQuery();
        if (resultSet != null && resultSet.next()) {
            int count = resultSet.getInt(1);
            if (count > 0) {
                words.put(word, true);
                System.out.println("count2:" + count);
                return true;
            } else {
                words.put(word, false);
                System.out.println("count1:" + count);
                return false;
            }
        }
        return false;
    }

    public static void guangming(String filePath, String db) throws Exception {


//        HikariDataSource dataSource = new HikariDataSource(getOnlineConfig());
//        MySqlHelper mySqlHelper = new MySqlHelper(dataSource);
        List<List<Object>> paramsList = new ArrayList<>();

        StringBuilder builder = new StringBuilder();

        String dbFlag = "merge1124";
        String file = "d:/merge" + dbFlag + ".sql";

        int i = 0;
        String result = null;
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;
        try {
            File fileName = new File(filePath);
            fileReader = new FileReader(fileName);
            bufferedReader = new BufferedReader(fileReader);
            try {
                String read = null;
                while ((read = bufferedReader.readLine()) != null) {
                    //result = result + read + "\r\n";
                    System.out.println("current i:" + (i++));
                    JSONObject jsonObject = JSON.parseObject(read);
                    Object[] arr = new Object[]{
                            jsonObject.get("word").toString(),
                            jsonObject.get("name").toString(),
                            jsonObject.get("score").toString(),
                            1,
                            jsonObject.get("type").toString(),
                            db
                    };

                    String sql = "INSERT INTO `dwd_expert_ins_rel` (`word`, `name`, `score`, `type`, `master`, `db`, `rel_id`, `createTime`, `moifyTime`) " +
                            "VALUES ('" + jsonObject.get("word").toString() + "', '" + jsonObject.get("name").toString() + "', '" + jsonObject.get("score").toString() + "', " + jsonObject.get("type").toString() + "," +
                            " 0, '" + dbFlag + "', NULL, '2022-11-23 10:03:00', '2022-11-23 10:03:00');";
                    if (arr[1].toString().contains("'") || arr[0].toString().contains("'")) {
                        continue;
                    }
                    //β-阿朴-8'-胡萝卜素酸乙酯
                    if (jsonObject.get("name").toString().length() > 500) {
                        continue;
                    }
                    // 判断数据库中是否已经存在了
                    if (wordExists(jsonObject.get("word").toString())) {
                        continue;
                    }

                    builder.append(sql + "\r\n");

                    paramsList.add(Arrays.asList(arr));
                    if (paramsList.size() % 100 == 0) {
                        //mySqlHelper.executeSqlBatch("INSERT dwd_expert_ins_rel(word,`NAME`,score,`master`,`type`,`db`) values(?,?,?,?,?,?)",paramsList);
                        paramsList.clear();

                        FileHelper.writeTxtFile(builder.toString(), file, false, true);
                        builder = new StringBuilder();
                    }


                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (fileReader != null) {
                fileReader.close();
            }
        }

        if (paramsList.size() > 0) {
            FileHelper.writeTxtFile(builder.toString(), file, false, true);

            //mySqlHelper.executeSqlBatch("INSERT dwd_expert_ins_rel(word,`NAME`,score,`master`,`type`,`db`) values(?,?,?,?,?,?)", paramsList);
        }


        P.print("over");
    }

    public static void test() throws Exception {
        P.print("abc");


        String content = FileHelper.readTxtFile("d:/time.txt");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        content = content.substring(4);

        String[] split1 = content.split("\r\n");
        for (String item : split1) {
            Date parse = sdf.parse(item);
            String format = sdf2.format(parse);
            System.out.println(format);
        }
        System.out.println(content);

        List<Item> list = new ArrayList<>();
        list.add(new Item("1", 2));
        list.add(new Item("2", 3));


//        String content = FileHelper.readTxtFile("C:\\Users\\cnki52\\Desktop\\未重复统计.txt");
        content = content.replace("\r", "").replace("\n", "").replace("\t", "").substring(4);

        List<Item> items = JSON.parseArray(content, Item.class);

        HikariDataSource dataSource = new HikariDataSource(getConfig());
        MySqlHelper mySqlHelper = new MySqlHelper(dataSource);
        String sql = "select * from tmp2";
        List<LinkedHashMap<String, Object>> linkedHashMaps = mySqlHelper.simpleQuery(sql, null);

        for (LinkedHashMap<String, Object> item : linkedHashMaps) {
            //System.out.println(item);
            String repeatmsg = item.get("repeatmsg").toString();
            String id = item.get("id").toString();
            String title = item.get("title").toString();

            //System.out.println(repeatmsg);
            String[] split = repeatmsg.split(";");
            String first = split[0];
            String number = first.substring(first.indexOf("未重复[") + 4).replace("]条", "");
            //System.out.println(number);

            compareNum(items, id, number);
        }
    }

    public static void compareNum(List<Item> items, String excelId, String theNum) {
        for (Item item : items) {
            if (item.getKey().equals(excelId)) {
                if (!item.doc_count.toString().equals(theNum)) {
                    System.out.printf(String.format("not equal: %s, %s, %s \r\n", excelId, theNum, item.getDoc_count()));
                }
                break;
            }
        }
    }

    public static long getRecordCount() {

        long result = 0;

        try {
            String sql = "select count(0) from bd.data_custom";
            HikariDataSource dataSource = new HikariDataSource(getConfig());
            Connection connection = dataSource.getConnection();

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet != null && resultSet.next())
                result = resultSet.getLong(1);

            dataSource.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public static void sync(long cursor, MongoCollection<Document> coll, HikariDataSource dataSource) throws Exception {

        String sql = String.format("SELECT * FROM bd.data_custom limit %s,10000", cursor);

        Date startDate = new Date();
        P.print(new SimpleDateFormat("yyyyMMdd HH:mm:ss").format(new Date()));

        long count = cursor;

        Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);

        if (resultSet != null) {
            while (resultSet.next()) {

                count++;

                String tablename = resultSet.getString("tablename");
                String colName = resultSet.getString("colname");
                String rowNum = resultSet.getString("rownum");
                String cellVal = resultSet.getString("cellval");

                //String msg = String.format("%s %s %s %s",tablename,colName,rowNum,cellVal);
                //P.print(msg);

                P.print("处理了" + count);

                if (StringUtils.isEmpty(colName))
                    continue;

                Bson findBson = Filters.and(
                        Filters.eq("tablename", tablename),
                        Filters.eq("rownum", rowNum)
                );

                //FindIterable<Document> findIterable = coll.find(findBson);

                long number = coll.count(findBson);
                if (number == 0) {
                    Document newDocument = new Document()
                            .append("tablename", tablename)
                            .append("rownum", rowNum)
                            .append("status", 1)
                            .append("createtime", new Date())
                            .append(colName, cellVal);
                    coll.insertOne(newDocument);
                } else {
                    coll.updateOne(findBson, new Document("$set", new Document(colName, cellVal)));
                }


            }
        }

        if (connection != null && !connection.isClosed())
            connection.close();

        Date endDate = new Date();
        P.print(new SimpleDateFormat("yyyyMMdd HH:mm:ss").format(startDate));
        P.print(new SimpleDateFormat("yyyyMMdd HH:mm:ss").format(endDate));
    }

    public static HikariConfig getConfig() {

        HikariConfig config = new HikariConfig();

        //config.setJdbcUrl("jdbc:mysql://:3306/bd?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=UTC");
        config.setJdbcUrl("jdbc:mysql://192.168.52.64:3306/gmgj_release_202211?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=UTC");
        config.setUsername("root");
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setPassword("123456");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmptCacheSqlLimit", "2048");

        return config;
    }

    public static HikariConfig getOnlineConfig() {

        HikariConfig config = new HikariConfig();

        //config.setJdbcUrl("jdbc:mysql://:3306/bd?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=UTC");
        //config.setJdbcUrl("jdbc:mysql://192.168.52.64:3306/gmgj_release_202211?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=UTC");
        config.setJdbcUrl("jdbc:mysql://10.122.109.124:3306/gmgj_release_20221123?useSSL=false&serverTimezone=Asia/Shanghai&allowMultiQueries=true");
        config.setUsername("root");
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setPassword("ABCDabcd1234@");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmptCacheSqlLimit", "2048");

        return config;
    }
}
