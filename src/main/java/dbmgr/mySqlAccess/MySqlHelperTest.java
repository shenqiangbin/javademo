package dbmgr.mySqlAccess;

import com.zaxxer.hikari.HikariConfig;
import common.P;
import dbmgr.kbaseAccess.KbaseTestItem;

import java.util.List;

public class MySqlHelperTest {

    public static void main(String[] args) throws Exception{

        MySqlHelper mySqlHelper = new MySqlHelper(getConfig());
//        String sql = "select * from resourceitem";
//
//        List<MySqlTestItem> list = mySqlHelper.simpleQuery(sql,MySqlTestItem.class);
//
//        for(MySqlTestItem item : list){
//            P.print(item.getName());
//        }
//
//        mySqlHelper.insert("insert resourceitem(name,url) values('my1','my1')");

        String sql2 = "select * from t137619";

        mySqlHelper.simpleQuery(sql2,MySqlTestItem.class);

        int i=137619;
        while (i>0){
            String sql = "CREATE TABLE `t1` (\n" +
                    "  `id` int(11) NOT NULL AUTO_INCREMENT,\n" +
                    "  PRIMARY KEY (`id`),\n" +
                    "  UNIQUE KEY `id_UNIQUE` (`id`)\n" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8;";
            sql = sql.replace("t1","t"+i);
            mySqlHelper.insert(sql);
            i++;
            System.out.println(i);
        }


    }

    public static HikariConfig getConfig(){

        HikariConfig config = new HikariConfig();

        //config.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/test?useUnicode=true&characterEncoding=utf8&useSSL=false");

        config.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/test?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=UTC");
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");

        config.setUsername("root");
        config.setPassword("123456");

        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmptCacheSqlLimit", "2048");

        return config;
    }
}
