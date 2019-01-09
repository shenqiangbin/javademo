package dbmgr.kbaseAccess;

import com.zaxxer.hikari.HikariConfig;
import common.P;
import dbmgr.mySqlAccess.MySqlHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.SimpleFormatter;

/*
* 将kbase中的数据同步到mysql中
* */
public class KBaseToMySql {
    public static void main(String[] args){

        String jdbc = "jdbc:kbase://192.168.100.92";
        jdbc = "jdbc:kbase://192.168.105.89";

        KBaseHelper kBaseHelper = new KBaseHelper(jdbc,"DBOWN","");

        String sql = "SELECT 县（区） as area,GPS定位坐标（经度） as lng,GPS定位坐标（纬度） as lat FROM GUIZHOU500";
        List<SyncItem> list = kBaseHelper.query(sql,null,SyncItem.class,new String[]{"area","lng","lat"});

        StringBuilder builder = new StringBuilder();
        String now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        now = "2019-01-09 21:46:04";

        MySqlHelper mySqlHelper = new MySqlHelper(getConfig());

        for(SyncItem item : list){
            String insertSql = String.format("insert region(name,FullName,RegionLevel,LocationX,LocationY,CreateOn,ParentId,RegionCode,ParentRegionCode,Suffix,UpdateOn) values('%s','',-9,%s,%s,'%s',-9,'','','','%s');", item.getArea(),item.getLng(),item.getLat(),now,now);
            //builder.append(insertSql);

            try {
                mySqlHelper.insert(insertSql);
                P.print(insertSql);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        P.print(builder.toString());

    }

    public static HikariConfig getConfig(){

        HikariConfig config = new HikariConfig();

//        JdbcUrl: jdbc:mysql://192.168.100.92:3306/bd?useSSL=false&serverTimezone=UTC #开发
//      #JdbcUrl: jdbc:mysql://192.168.105.89:33061/bd?useSSL=false #测试
//      #JdbcUrl: jdbc:mysql://192.168.25.171:3306/bd?useSSL=false
//        DriverClassName: com.mysql.cj.jdbc.Driver

        config.setJdbcUrl("jdbc:mysql://192.168.100.92:3306/bd?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=UTC");
        config.setUsername("root");
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setPassword("123456");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmptCacheSqlLimit", "2048");

        return config;
    }
}
