package dbmgr.mongoDbAccess;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import common.P;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/*
 * http://www.runoob.com/mongodb/mongodb-tutorial.html
 * http://mongodb.github.io/mongo-java-driver/3.10/driver/getting-started/quick-start-pojo/#update-a-single-person
 */
public class MongoDBHelperTest {
    public static void main(String[] args){

        P.print("MongoDBHelperTest");

        //init2();
        //client.close();

    }

    public static void init1(){
        //String connectionString = "mongodb://127.0.0.1:27017/?gssapiServiceName=mongodb";
        String uri = "mongodb://exceldbmgr:123456@192.168.100.92:27017/exceldb";
        MongoClientURI connectionString = new MongoClientURI(uri);
        MongoClient client = new MongoClient(connectionString);

        MongoDatabase database = client.getDatabase("exceldb");

        MongoCollection<Document> collection = database.getCollection("demo");

        collection.drop();

        add(collection);
        update(collection);

        //遍历表中所有记录
        FindIterable<Document> doc = collection.find();
        MongoCursor<Document> cursor = doc.iterator();
        while (cursor.hasNext()){
            Document docment = cursor.next();
            P.print(docment);
            P.print(new Date());
        }
    }

    public static void add(MongoCollection<Document> collection){

        List<Document> documents = new ArrayList<Document>();

        documents.add(
                new Document("tablename", "2016年山西省功能食品原料产量与全国对比-e71cabafdd7b445f88560b506613755a")
                        .append("rownum","1")
                        .append("rowval",
                                new Document()
                                        .append("学生","1")
                                        .append("成绩","2")
                                        .append("科目","3")
                        ).append("status",1)
        );

        documents.add(
                new Document("tablename", "2016年山西省功能食品原料产量与全国对比-e71cabafdd7b445f88560b506613755a")
                        .append("rownum","2")
                        .append("rowval",
                                new Document()
                                        .append("学生","11")
                                        .append("成绩","22")
                                        .append("科目","33")
                        ).append("status",1)
        );

        collection.insertMany(documents);
    }

    //更新（且是文档中的文档）
    public static void update(MongoCollection<Document> collection){

        Bson filter = Filters.and(
                Filters.eq("tablename", "2016年山西省功能食品原料产量与全国对比-e71cabafdd7b445f88560b506613755a"),
                Filters.eq("rownum", "1")
        );

        Document record = collection.find(filter).first();
        Document rowVal = (Document)record.get("rowval");
        rowVal.put("科目",33);

        collection.updateOne(filter,new Document("$set", new Document("rowval",rowVal)));

    }
}
