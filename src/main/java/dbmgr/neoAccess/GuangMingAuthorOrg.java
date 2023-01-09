package dbmgr.neoAccess;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import common.P;
import dbmgr.mySqlAccess.MySqlHelper;
import dbmgr.mySqlAccess.model.Item;
import fileDemo.FileHelper;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.*;

public class GuangMingAuthorOrg {

    private static String wordContent;
    private static HashMap<String, Boolean> wordBelongNodes = new HashMap<>();

    static String authorInfoFile = "D:\\code\\TPI\\大数据产品\\光明国际\\数据处理\\机构作者\\author.txt";

    /**
     * 导入机构、作者数据。
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {

        // 一级二级三级节点
        String wordsFile = "D:\\code\\TPI\\大数据产品\\光明国际\\数据处理\\机构作者\\word.txt";
        wordContent = FileHelper.readTxtFile(wordsFile);


        //String content = FileHelper.readTxtFile(authorInfoFile);

        String path = "D:\\code\\TPI\\大数据产品\\光明国际\\数据处理\\机构作者\\2023-01";
        String time = "20230104";

        String filePath = path + "\\farming.author.organization";
        //guangming(filePath,"framing", time, path);

        filePath = path + "\\fishery.author.organization";
        guangming(filePath,"fishery", time, path);

        filePath = path + "\\food.author.organization";
        guangming(filePath,"food", time, path);

        filePath = path + "\\livestock.author.organization";
        guangming(filePath,"livestock", time, path);

        filePath = path + "\\merge.author.organization";
        guangming(filePath, "merge", time, path);

        filePath = path + "\\sp_co.author.organization";
        guangming(filePath, "sp_co", time, path);

        P.print("all over");

        // 测试根据用户编号获取用户名
//        String author = getAuthorName("000019282249");
//        System.out.println(author);

//        String author2 = getAuthorNameByFile(authorInfoFile, "000019282249");
//        System.out.println(author2);

    }

    static HikariDataSource dataSource = new HikariDataSource(getOnlineConfig());
    static HikariDataSource midDataSource = new HikariDataSource(getMidDataConfig());
    static Connection connection;
    static Connection midConnection;

    static {
        try {
            connection = dataSource.getConnection();
            midConnection = midDataSource.getConnection();
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

    public static void guangming(String filePath, String db, String time, String path) throws Exception {


//        HikariDataSource dataSource = new HikariDataSource(getOnlineConfig());
//        MySqlHelper mySqlHelper = new MySqlHelper(dataSource);
        List<List<Object>> paramsList = new ArrayList<>();

        StringBuilder builder = new StringBuilder();

        String dbFlag = db + time;
        String file = path + dbFlag + ".sql";

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

                    //β-阿朴-8'-胡萝卜素酸乙酯
                    if (jsonObject.get("name").toString().length() > 500) {
                        continue;
                    }
                    String name = jsonObject.get("name").toString();
                    String type = jsonObject.get("type").toString();

                    if(!belong1Or2Or3LevelNode(jsonObject.get("word").toString())){
                        continue;
                    }

                    // 判断数据库中是否已经存在了
                    if (wordExists(jsonObject.get("word").toString())) {
                        continue;
                    }



                    if (type.equalsIgnoreCase("0")) {
                        //name = getAuthorName(name);
                        name = getAuthorNameByFile(name);
                    }

                    Object[] arr = new Object[]{
                            jsonObject.get("word").toString(),
                            name,
                            jsonObject.get("score").toString(),
                            1,
                            type,
                            db
                    };

                    String sql = "INSERT INTO `dwd_expert_ins_rel` (`word`, `name`, `score`, `type`, `master`, `db`, `rel_id`, `createTime`, `moifyTime`) " +
                            "VALUES ('" + jsonObject.get("word").toString() + "', '" + name + "', '" + jsonObject.get("score").toString() + "', " + jsonObject.get("type").toString() + "," +
                            " 0, '" + dbFlag + "', NULL, '2022-11-23 10:03:00', '2022-11-23 10:03:00');";
                    if (arr[1].toString().contains("'") || arr[0].toString().contains("'")) {
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

    /**
     * 是否属于1、2、3级图谱节点
     * @param word
     * @return
     */
    public static boolean belong1Or2Or3LevelNode(String word) {
        if(wordBelongNodes.containsKey(word)){
            return wordBelongNodes.get(word);
        }

        boolean result = false;
        String[] wordArr = wordContent.split("\r\n");
        for (int i = 0; i < wordArr.length; i++) {
            if (wordArr[i].toString().equals(word)) {
                result = true;
                break;
            }
        }

        wordBelongNodes.put(word, result);
        return result;
    }

    public static String getAuthorName(String authorCode) throws SQLException {
        String sql = "select author,authorCode from gm_articles_has_author where authorCode like '%000019282249%' limit 10";
        sql = sql.replace("000019282249", authorCode);


        Statement statement = midConnection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        if (resultSet != null && resultSet.next()) {
            String names = resultSet.getString(1);
            String codes = resultSet.getString(2);

            String[] codeArr = codes.split(";");
            String[] nameArr = names.split(";");

            for (int i = 0; i < codeArr.length; i++) {
                if (codeArr[i].equals(authorCode)) {
                    return nameArr[i];
                }
            }

            throw new RuntimeException("not found:" + authorCode);
        }

        return "";
    }

    public static String getAuthorNameByFile(String authorCode) throws Exception {

        String result = null;
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;
        try {
            File fileName = new File(authorInfoFile);
            fileReader = new FileReader(fileName);
            bufferedReader = new BufferedReader(fileReader);
            try {
                String read = null;
                while ((read = bufferedReader.readLine()) != null) {
                    if (read.contains(authorCode)) {
                        return read.split(":::")[1];
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
        //System.out.println("读取出来的文件内容是：" + "\r\n" + result);
        return "";
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


    public static HikariConfig getMidDataConfig() {

        HikariConfig config = new HikariConfig();

        config.setJdbcUrl("jdbc:mysql://192.168.20.154:13306/gmgj_mid_data?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=UTC");
        config.setUsername("root");
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setPassword("123456");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmptCacheSqlLimit", "2048");

        return config;
    }
}
