package dbmgr.mySqlAccess;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import dbmgr.mySqlAccess.model.Col;

import java.sql.BatchUpdateException;
import java.util.List;
import java.util.stream.Collectors;

public class MyBatisBuilder {

    static HikariDataSource dataSource = new HikariDataSource(getConfig());
    static MySqlHelper mySqlHelper = new MySqlHelper(dataSource);

    private static String mapperPakcage = "com.cnki.mapper";
    private static String modelPakcage = "com.cnki.model";

    public static void main(String[] args) throws Exception {

        String dbName = "medicine_mining";
        String tableName = "validatecode";
        List<Col> cols = getCols(dbName, tableName);

        String resultMap = buildResultMap(tableName, cols);
        System.out.println(resultMap);

        String insertRegion = buildInsert(tableName, cols);
        System.out.println(insertRegion);

        String updateRegion = buildUpdate(tableName, cols);
        System.out.println(updateRegion);

        String modelClass = buildModel(tableName, cols);
        System.out.println(modelClass);
    }

    public static List<Col> getCols(String dbName, String talbeName) throws Exception {
        String sql = "select column_name as name,data_type as dataType,column_comment as display from information_schema.columns \n" +
                "where  table_name = ? and table_schema = ? order by ordinal_position";
        return mySqlHelper.simpleQuery(sql, new Object[]{talbeName, dbName}, Col.class);
    }

    public static String build(String tableName){
        String sql = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" +
                "<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\" >\n" +
                "<mapper namespace=\"{{}}\" >".replace("{{}}", mapperPakcage + "." + tableName + "Mapper");
        return sql;
    }

    public static String buildResultMap(String tableName, List<Col> cols) throws Exception {

        StringBuilder builder = new StringBuilder();
        for (Col col : cols){
            builder.append(generateResultTypeCol(col));
        }

        String sql = "        <resultMap id=\"BaseResultMap\" type=\"{{1}}\" >\n" +
                builder.toString() +
                "        </resultMap>";
        sql = sql.replace("{{1}}", modelPakcage + "." + toUpperCaseFirstOne(tableName));
        return sql;
    }

    public static String getModelName(String tableName){
        return modelPakcage + "." + toUpperCaseFirstOne(tableName);
    }

    private static String generateResultTypeCol(Col col) throws Exception {
        String batisType = getBatisType(col.getDataType());
        String sql = "    <result column=\"role_id\" property=\"roleId\" jdbcType=\"INTEGER\" />\n"
                .replace("role_id", col.getName())
                .replace("roleId", col.getName())
                .replace("INTEGER", batisType);
        return sql;
    }

    public static String buildInsert(String tableName, List<Col> cols){
        StringBuilder builder = new StringBuilder();
        builder.append("<insert id=\"insert\" parameterType=\"modelName\" useGeneratedKeys=\"true\" keyProperty=\"id\" keyColumn=\"id\"> \n"
        .replace("modelName",getModelName(tableName)));
        builder.append("insert into " + tableName + "(");

        String fieldStr = String.join(",", cols.stream().filter(m -> !isPrimaryKey(m)).map(m-> m.getName()).collect(Collectors.toList()));
        builder.append(fieldStr);

        builder.append(")\n");
        builder.append("values ( \n");


        String fieldInertStr = String.join(",\n", cols.stream().filter(m -> !isPrimaryKey(m)).map(m-> getInsertField(m)).collect(Collectors.toList()));
        builder.append(fieldInertStr);

        builder.append("\n</insert>");
        return builder.toString();

    }

    public static String buildUpdate(String tableName, List<Col> cols){
        StringBuilder builder = new StringBuilder();
        builder.append("<update id=\"updateById\" parameterType=\"modelName\">\n"
                .replace("modelName",getModelName(tableName)));
        builder.append("update " + tableName + " set \n");


        String str = String.join(",\n", cols.stream().filter(m -> !isPrimaryKey(m)).map(m-> getUpdateField(m)).collect(Collectors.toList()));
        builder.append(str);

        Col primaryKey = getPrimaryKey(cols);
        builder.append("\n where \n").append(getUpdateField(primaryKey));


        builder.append("\n</update>");
        return builder.toString();

    }

    public static String buildModel(String tableName, List<Col> cols){
        StringBuilder builder = new StringBuilder();
        builder.append("public class ").append(toUpperCaseFirstOne(tableName)).append("{\n");
        builder.append(getModelFieldStr(cols)).append("\n");
        builder.append("}\n");
        return builder.toString();
    }

    private static String getModelFieldStr(List<Col> cols) {
        String str = String.join("\n", cols.stream().map(m-> getModelField(m)).collect(Collectors.toList()));
        return str;
    }

    private static boolean isPrimaryKey(Col m) {
        return m.getName().equalsIgnoreCase("id");
    }

    private static Col getPrimaryKey(List<Col> cols){
        for (Col col : cols){
            if(isPrimaryKey(col))
                return col;
        }
        return null;
    }

    private static String getInsertField(Col col){
        return "        #{indicatorsourceid,jdbcType=INTEGER}"
                .replace("indicatorsourceid",col.getName())
                .replace("INTEGER", getBatisType(col.getDataType()));
    }

    private static String getUpdateField(Col col){
        return "        indicatorsourceid = #{indicatorsourceid,jdbcType=INTEGER}"
                .replace("indicatorsourceid",col.getName())
                .replace("INTEGER", getBatisType(col.getDataType()));
    }

    private static String getModelField(Col col){
        return " private String name;"
                .replace("name",col.getName())
                .replace("String", getJavaType(col.getDataType()));
    }

    private static String toUpperCaseFirstOne(String s) {
        if (Character.isUpperCase(s.charAt(0)))
            return s;
        else
            return (new StringBuilder()).append(Character.toUpperCase(s.charAt(0))).append(s.substring(1)).toString();
    }

    private static String getBatisType(String dataType) {
        switch (dataType){
            case "int": return "INTEGER";
            case "bigint": return "INTEGER";
            case "tinyint": return "INTEGER";
            case "datetime": return "TIMESTAMP";
            default:
                throw new RuntimeException("未知类型：" + dataType);
        }
    }

    private static String getJavaType(String dataType) {
        switch (dataType){
            case "int": return "Integer";
            case "bigint": return "Integer";
            case "tinyint": return "Integer";
            case "datetime": return "Date";
            default:
                throw new RuntimeException("未知类型：" + dataType);
        }
    }

    public static HikariConfig getConfig() {

        HikariConfig config = new HikariConfig();

        config.setJdbcUrl("jdbc:mysql://192.168.52.64:3306/medicine_mining?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=UTC");
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setUsername("root");
        config.setPassword("123456");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmptCacheSqlLimit", "2048");

        return config;
    }
}
