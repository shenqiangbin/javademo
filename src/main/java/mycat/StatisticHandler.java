package mycat;

import com.zaxxer.hikari.HikariConfig;
import dbmgr.mySqlAccess.MySqlHelper;

import java.sql.SQLException;

public class StatisticHandler {
    public static void main(String[] args) throws SQLException {
        //loop();
        //loopAndHandle();
        //createDb();
        loopAndBack();

    }

    static void createDb() {
        StringBuilder builder = new StringBuilder();
        for (int i = 1; i <= 30; i++) {
            String prefix = "statistic_19_";
            builder.append("create database " + prefix + i + ";\r\n");
        }
        System.out.println(builder.toString());
    }

    static void loop() {
        for (int i = 1; i <= 10; i++) {
            String prefix = "statistic1";
            String table = "nv_excel_pickup";
            String sql = countSql(prefix, i, table);
            System.out.println(sql);
        }
    }

    static String countSql(String prefix, int i, String table) {
        return String.format("select count(0) from %s%s.`%s`;", prefix, i, table);
    }

    static void loopAndBack() {
        StringBuilder builder = new StringBuilder();
        StringBuilder reBuilder = new StringBuilder();
        for (int i = 1; i <= 30; i++) {
            String prefix = "statistic_41_";
            String db = prefix + i;
            String format = "\"C:\\Program Files\\MySQL\\MySQL Server 8.0\\bin\\mysqldump.exe\"  -uroot -p!@#$1q2w3e4r --port=3306 --compress --skip-add-drop-table --databases %s > E:/back-data/shengji/%s.sql";
            format = format.replace("\"C:\\Program Files\\MySQL\\MySQL Server 8.0\\bin\\mysqldump.exe\"","\"E:\\bigtable\\mysql-8.0.23-winx64\\bin\\mysqldump.exe\"");
            String s = String.format(format, db, db);
            builder.append(s + "\r\n");

            format = "\"C:\\Program Files\\MySQL\\MySQL Server 8.0\\bin\\mysql.exe\"  -uroot -p!@#$1q2w3e4r --port=33061 < E:/back-data/shengji/%s.sql";
            format = format.replace("\"C:\\Program Files\\MySQL\\MySQL Server 8.0\\bin\\mysql.exe\"","\"E:\\bigtable\\mysql-8.0.23-winx64\\bin\\mysql.exe\"");
            s = String.format(format, db, db);
            reBuilder.append(s + "\r\n");
        }


        System.out.println(builder.toString());
        System.out.println(reBuilder.toString());
    }


    static void loopAndHandle() throws SQLException {
        MySqlHelper mySqlHelper = new MySqlHelper(getConfig());
        for (int i = 1; i <= 30; i++) {
            String prefix = "statistic_42_";
            String table = "nv_excel_pickup";
            String sql = countSql(prefix, i, table);
            //String count = mySqlHelper.executeScalar(sql, null);
            //System.out.println(String.format("%s : %s", sql, count));
            System.out.println(String.format("%s", sql));
        }
        /**
         select count(0) from statistic11.`nv_excel_pickup`; : 94
         select count(0) from statistic12.`nv_excel_pickup`; : 96
         select count(0) from statistic13.`nv_excel_pickup`; : 82
         select count(0) from statistic14.`nv_excel_pickup`; : 114
         select count(0) from statistic15.`nv_excel_pickup`; : 82
         select count(0) from statistic16.`nv_excel_pickup`; : 86
         select count(0) from statistic17.`nv_excel_pickup`; : 118
         select count(0) from statistic18.`nv_excel_pickup`; : 76
         select count(0) from statistic19.`nv_excel_pickup`; : 112
         select count(0) from statistic110.`nv_excel_pickup`; : 80
         select count(0) from statistic21.`nv_excel_pickup`; : 90
         select count(0) from statistic22.`nv_excel_pickup`; : 126
         select count(0) from statistic23.`nv_excel_pickup`; : 128
         select count(0) from statistic24.`nv_excel_pickup`; : 86
         select count(0) from statistic25.`nv_excel_pickup`; : 114
         select count(0) from statistic26.`nv_excel_pickup`; : 100
         select count(0) from statistic27.`nv_excel_pickup`; : 106
         select count(0) from statistic28.`nv_excel_pickup`; : 90
         select count(0) from statistic29.`nv_excel_pickup`; : 112
         select count(0) from statistic210.`nv_excel_pickup`; : 108
         */
    }

    public static HikariConfig getConfig() {

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://192.168.20.152:3306/mysql?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=UTC");
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setUsername("root");
        config.setPassword("123456");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmptCacheSqlLimit", "2048");

        return config;
    }
}
