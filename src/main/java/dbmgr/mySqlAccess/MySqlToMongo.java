package dbmgr.mySqlAccess;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import common.P;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MySqlToMongo {
    public static void main(String[] args){
        P.print("abc");

        MongoCollection<Document> coll = getMongoTable();

        HikariDataSource dataSource = new HikariDataSource(getConfig());

        long totalNumber = getRecordCount();
        long loop = (totalNumber / 10000) + 1;

        for(long i=0; i<=loop; i++){
            try {
                sync(i*10000,coll,dataSource);
            } catch (Exception e) {
                e.printStackTrace();
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
        config.setJdbcUrl("jdbc:mysql://10.170.2.135:3306/bd?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=UTC");
        config.setUsername("root");
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setPassword("123456");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmptCacheSqlLimit", "2048");

        return config;
    }

    public static MongoCollection<Document> getMongoTable(){

        String uri = "mongodb://exceldbmgr:123456@192.168.100.92:27017/exceldb";
        uri = "mongodb://10.170.2.135:3000/exceldb";
        MongoClientURI connectionString = new MongoClientURI(uri);
        MongoClient client = new MongoClient(connectionString);

        MongoDatabase database = client.getDatabase("exceldb");
        MongoCollection<Document> collection = database.getCollection("myexcel");

        return collection;
    }
}
