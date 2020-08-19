import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.Statement;

public class InsertData {

//    public static void main(String[] args) throws Exception{
//
//        String sql = "iNSERT INTO `HBJ`.`irp_report` (`ReportCode`, `Provice`, `City`, `County`, `DetailAddress`, `IndustryName`, `LocationX`, `LocationY`, `ReportDeptName`, `ReportFrom`, `RegionArea`, `RegionCode`, `WhetherTrue`, `CreateOn`, `UpdateOn`, `IndustryCode`) VALUES\n" +
//                "( '3f01c0a61ebe4d299f35f773232b901c', '陕西省', '铜川市', '耀州区', '桃曲坡水库', '其他', '107.768721', '32.994748', '个人', '电话 ', '耀州区', '610204', NULL, '2006-03-13 01:04:00', '2006-04-01 00:00:00', '20');";
//
//        HikariDataSource ds = new HikariDataSource(getConfig());
//        Connection connection = ds.getConnection();
//
//        Statement statement = connection.createStatement();
//
//        for(int i=0; i<80*10000; i++)
//            statement.execute(sql);
//
//    }
//
//
//
//    public static HikariConfig getConfig(){
//
//        HikariConfig config = new HikariConfig();
//
//
//        //config.setJdbcUrl("jdbc:mysql://:3306/bd?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=UTC");
//        config.setJdbcUrl("jdbc:mysql://192.168.103.90:3306/hbj?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=UTC");
//        config.setUsername("root");
//        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
//        config.setPassword("123456");
//        config.addDataSourceProperty("cachePrepStmts", "true");
//        config.addDataSourceProperty("prepStmtCacheSize", "250");
//        config.addDataSourceProperty("prepStmptCacheSqlLimit", "2048");
//
//        return config;
//    }

}
