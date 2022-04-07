package dbmgr.mySqlAccess;

import com.alibaba.fastjson.JSON;
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

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

public class MySqlnongkeyuan {

    public static void main(String[] args) throws Exception {
        P.print("abc");

        List<Item> list = new ArrayList<>();
        list.add(new Item("1",2));
        list.add(new Item("2",3));


        String content = FileHelper.readTxtFile("C:\\Users\\cnki52\\Desktop\\未重复统计.txt");
        content = content.replace("\r","").replace("\n","").replace("\t","").substring(4);

        //content = JSON.toJSONString(list);

        List<Item> items = JSON.parseArray(content, Item.class);

        HikariDataSource dataSource = new HikariDataSource(getConfig());
        MySqlHelper mySqlHelper = new MySqlHelper(dataSource);
        String sql = "select * from tmp2";
        List<LinkedHashMap<String, Object>> linkedHashMaps = mySqlHelper.simpleQuery(sql, null);

        for(LinkedHashMap<String, Object> item : linkedHashMaps){
            //System.out.println(item);
            String repeatmsg = item.get("repeatmsg").toString();
            String id = item.get("id").toString();
            String title =item.get("title").toString();

            //System.out.println(repeatmsg);
            String[] split = repeatmsg.split(";");
            String first = split[0];
            String number = first.substring(first.indexOf("未重复[")+4).replace("]条","");
            //System.out.println(number);

            compareNum(items, id, number);
        }

    }

    public static void compareNum(List<Item> items, String excelId, String theNum){
        for(Item item : items){
            if(item.getKey().equals(excelId)){
                if(!item.doc_count.toString().equals(theNum)){
                    System.out.printf(String.format("not equal: %s, %s, %s \r\n",excelId,theNum,item.getDoc_count()));
                }
                break;
            }
        }
    }

    public static long getRecordCount(){

        long result = 0;

        try{
            String sql = "select count(0) from bd.data_custom";
            HikariDataSource dataSource = new HikariDataSource(getConfig());
            Connection connection = dataSource.getConnection();

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            if(resultSet!=null && resultSet.next())
                result = resultSet.getLong(1);

            dataSource.close();

        }catch (Exception e){
            e.printStackTrace();
        }

        return result;
    }

    public static void sync(long cursor, MongoCollection<Document> coll,HikariDataSource dataSource) throws Exception{

        String sql = String.format("SELECT * FROM bd.data_custom limit %s,10000",cursor);

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

                    P.print("处理了"+count);

                    if(StringUtils.isEmpty(colName))
                        continue;

                    Bson findBson = Filters.and(
                            Filters.eq("tablename",tablename),
                            Filters.eq("rownum",rowNum)
                    );

                    //FindIterable<Document> findIterable = coll.find(findBson);

                    long number = coll.count(findBson);
                    if(number==0){
                        Document newDocument = new Document()
                                .append("tablename",tablename)
                                .append("rownum",rowNum)
                                .append("status",1)
                                .append("createtime",new Date())
                                .append(colName,cellVal);
                        coll.insertOne(newDocument);
                    }else{
                        coll.updateOne(findBson,new Document("$set",new Document(colName,cellVal)));
                    }


                }
            }

            if (connection != null && !connection.isClosed())
                connection.close();

        Date endDate = new Date();
        P.print(new SimpleDateFormat("yyyyMMdd HH:mm:ss").format(startDate));
        P.print(new SimpleDateFormat("yyyyMMdd HH:mm:ss").format(endDate));
    }

    public static HikariConfig getConfig(){

        HikariConfig config = new HikariConfig();

        //config.setJdbcUrl("jdbc:mysql://:3306/bd?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=UTC");
        config.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/test?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=UTC");
        config.setUsername("root");
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setPassword("123456");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmptCacheSqlLimit", "2048");

        return config;
    }


}
