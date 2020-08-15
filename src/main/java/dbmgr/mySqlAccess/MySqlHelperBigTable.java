package dbmgr.mySqlAccess;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.lang3.StringUtils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class MySqlHelperBigTable {

    public static List<Field> getFields() {
        List<Field> list = new ArrayList<>();
        list.add(new Field("country", "国家名称"));
        list.add(new Field("areaZone", "地区/板块"));
        list.add(new Field("province", "省"));
        list.add(new Field("city", "市/州"));
        list.add(new Field("area", "区/县"));
        list.add(new Field("areaLevel", "地区等级"));
        list.add(new Field("areaCode", "地区代码"));
        list.add(new Field("valueDate", "数值对应日期"));
        list.add(new Field("year", "年"));
        list.add(new Field("month", "月"));
        list.add(new Field("day", "日"));
        list.add(new Field("oneLevel", "一级指标"));
        list.add(new Field("twoLevel", "二级指标"));
        list.add(new Field("threeLevel", "三级指标"));
        list.add(new Field("fourLevel", "四级指标"));
        list.add(new Field("ontology", "本体"));
        list.add(new Field("indicatorName", "指标名称"));
        list.add(new Field("knowledgeElement", "知识元"));
        list.add(new Field("unitName", "单位名称"));
        list.add(new Field("dataSource", "数据来源"));
        list.add(new Field("yearbookChineseName", "年鉴中文名"));
        list.add(new Field("yearbookYear", "年鉴年份"));
        list.add(new Field("parentNode", "父亲节点"));
        list.add(new Field("productName", "产品名称"));
        list.add(new Field("productCategory", "产品分类"));
        return list;
    }

    /**
     * 表格大数据 - 规范字段调整（包括建表）
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {

        //initField();
        //initTableOrigi();
        initTable();
        System.out.println("完");
    }

    private static void initTableOrigi() throws SQLException {

        HikariDataSource dataSource = new HikariDataSource(getConfig());
        Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement();

        StringBuilder sqlBuilder = new StringBuilder();
        List<Field> fields = getFields();
        for (Field f : fields) {
            String sql = String.format("`%s` varchar(350) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '' COMMENT '%s',\n", f.getName(), f.getDisplay());
            System.out.println(sql);
            sqlBuilder.append(sql);
        }

        String format = getMetadataOrigi();
        String sql = String.format(format, sqlBuilder.toString());

        statement.execute("DROP TABLE IF EXISTS `nv_excel_pickup_origi`");
        statement.execute(sql);
    }

    private static void initTable() throws SQLException {

        HikariDataSource dataSource = new HikariDataSource(getConfig());
        Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement();

        StringBuilder sqlBuilder = new StringBuilder();
        List<Field> fields = getFields();
        for (Field f : fields) {
            String sql = String.format("`%s` varchar(350) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '' COMMENT '%s',\n", f.getName(), f.getDisplay());
            String sql2 = String.format("`%s` varchar(350) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '' COMMENT '%s',\n", f.getName() + "Normal", f.getDisplay() + "(已规范)");
            System.out.println(sql);
            sqlBuilder.append(sql + sql2);
        }

        String format = getMetadata();
        String sql = String.format(format, sqlBuilder.toString());

        System.out.println(sql);
        statement.execute("DROP TABLE IF EXISTS `nv_excel_pickup`");
        statement.execute(sql);
    }

    private static void initField() throws SQLException {

        HikariDataSource dataSource = new HikariDataSource(getConfig());
        Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement();

        List<Field> fields = getFields();
        for (Field f : fields) {
            String sql = String.format("INSERT INTO `nv_field_pickup` (`DatabaseCode`, `FieldName`, `DisplayName`, `DataType`, `DataLength`, `NeedNormal`, `JoinCal`, `Sort`) VALUES \n" +
                    "('default', '%s', '%s', 'varchar', '0', '1', '1', '50');", f.getName(), f.getDisplay());
            System.out.println(sql);
            statement.execute(sql);
        }

        connection.close();
    }

    private static String getMetadataOrigi() {
        return "CREATE TABLE `nv_excel_pickup_origi` (\n" +
                "  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '系统标识',\n" +
                "  `CreateUser` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '创建人',\n" +
                "  `CreateTime` datetime DEFAULT NULL COMMENT '创建时间',\n" +
                "  `UpdateUser` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '更新人',\n" +
                "  `UpdateTime` datetime DEFAULT NULL COMMENT '更新时间',\n" +
                "  `Value` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '数值',\n" +
                "  `Title` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '' COMMENT '表格名称',\n" +
                "  `TableSheetName` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '表格Sheet名称',\n" +
                "  `ValueAddress` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '单元格坐标',\n" +
                "  `MetadataSysID` int(11) NOT NULL COMMENT '元数据系统标识',\n" +
                "  `MetadataID` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '元数据唯一标识',\n" +
                "  %s" +
                "  PRIMARY KEY (`ID`)\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='提取数值数据表_EXCEL提取后的存储数据表';";
    }

    private static String getMetadata() {
        return  "CREATE TABLE `nv_excel_pickup` (\n" +
                "  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '系统标识',\n" +
                "  `CreateUser` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '创建人',\n" +
                "  `CreateTime` datetime DEFAULT NULL COMMENT '创建时间',\n" +
                "  `UpdateUser` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '更新人',\n" +
                "  `UpdateTime` datetime DEFAULT NULL COMMENT '更新时间',\n" +
                "  `Value` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '数值',\n" +
                "  `Title` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '' COMMENT '表格名称',\n" +
                "  `TableSheetName` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '表格Sheet名称',\n" +
                "  `ValueAddress` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '单元格坐标',\n" +
                "  `MetadataSysID` int(11) NOT NULL COMMENT '元数据系统标识',\n" +
                "  `MetadataID` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '元数据唯一标识',\n" +
                "  `DataState` int(5) DEFAULT '0' COMMENT '数据状态：0-默认 1-合并库数据',\n" +
                "  `RecordMd5` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '记录字符串集合的md5，用于判断是否已有相同记录存在',\n" +
                "  %s" +
                "  PRIMARY KEY (`ID`)\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='提取数值数据表_EXCEL提取后的存储数据表';";
    }


    public static HikariConfig getConfig() {

        HikariConfig config = new HikariConfig();

        config.setJdbcUrl("jdbc:mysql://192.168.100.92:3306/tablebigdata54?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=UTC");
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setUsername("root");
        config.setPassword("123456");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmptCacheSqlLimit", "2048");

        return config;
    }
}
