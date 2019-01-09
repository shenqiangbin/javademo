package dbmgr.mySqlAccess;

import com.zaxxer.hikari.HikariConfig;
import common.P;
import dbmgr.kbaseAccess.KbaseTestItem;

import java.util.List;

public class MySqlHelperTest {

    public static void main(String[] args) throws Exception{

        MySqlHelper mySqlHelper = new MySqlHelper(getConfig());
        String sql = "select * from resourceitem";

        List<MySqlTestItem> list = mySqlHelper.simpleQuery(sql,MySqlTestItem.class);

        for(MySqlTestItem item : list){
            P.print(item.getName());
        }

        mySqlHelper.insert("insert resourceitem(name,url) values('my1','my1')");

    }

    public static HikariConfig getConfig(){

        HikariConfig config = new HikariConfig();

        config.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/javablog?useUnicode=true&characterEncoding=utf8&useSSL=false");
        config.setUsername("root");
        config.setPassword("123456");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmptCacheSqlLimit", "2048");

        return config;
    }
}
