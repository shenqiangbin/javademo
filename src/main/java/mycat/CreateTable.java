package mycat;

import java.sql.SQLSyntaxErrorException;

public class CreateTable {
    public static void main(String[] args) {

        dropDatabase();

//        String prefix = "a";
//        prefix = "read_b";
//        int nodenum = 20;
//
//        StringBuilder builder = new StringBuilder();
//        for (int i = 1; i < nodenum; i++) {
//            String dbName = "db_" + prefix + i;
//            builder.append("create database `").append(dbName).append("`;");
//            builder.append("use `").append(dbName).append("`;");
//            builder.append(getCreateTableSql());
//        }
//        System.out.println(builder.toString());
    }

    static void dropDatabase(){
        for (int i = 1; i <= 30; i++) {
            String prefix = "statistic_b";
            String db = prefix + i;
            String sql = "drop database " + db + ";";
            System.out.println(sql);
        }
    }

    public static String getCreateTableSql() {
        return "CREATE TABLE `primitive` (\n" +
                "  `Id` int(11) NOT NULL AUTO_INCREMENT,\n" +
                "  `NameEn` varchar(64) DEFAULT NULL,\n" +
                "  `NameZh` varchar(64) DEFAULT NULL,\n" +
                "  `Icon` varchar(164) DEFAULT NULL,\n" +
                "  `Category` varchar(64) DEFAULT NULL,\n" +
                "  `Setting` varchar(2000) DEFAULT NULL,\n" +
                "  `Pins` varchar(5000) DEFAULT NULL COMMENT '引脚个数，可以连出去的线',\n" +
                "  `Outputs` varchar(255) DEFAULT NULL COMMENT '输出变量的名称，用逗号或分号分隔',\n" +
                "  `Node` varchar(2000) DEFAULT NULL,\n" +
                "  `XmlData` text COMMENT '图元XML',\n" +
                "  `ModuleXml` text,\n" +
                "  `ModuleJson` text,\n" +
                "  `Enabled` int(11) DEFAULT NULL COMMENT '是否启用',\n" +
                "  `Status` int(11) DEFAULT NULL COMMENT '删除标识：0-已删除，1-未删除',\n" +
                "  `AddUser` varchar(64) DEFAULT NULL,\n" +
                "  `AddDateTime` datetime DEFAULT NULL,\n" +
                "  `EditUser` varchar(64) DEFAULT NULL,\n" +
                "  `EditDateTime` datetime DEFAULT NULL,\n" +
                "  PRIMARY KEY (`Id`)\n" +
                ") ENGINE=InnoDB AUTO_INCREMENT=74 DEFAULT CHARSET=utf8;";
    }
}
