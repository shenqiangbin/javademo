package ExcelDemo;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import dbmgr.microsoftAccess.ResultSetHelper;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFComment;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.bson.Document;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExcelHandle {

    private static String parent = "";

    public static void main(String[] args) throws Exception {

        String fileName = "e:/数据铁笼知识图谱-0813 - 副本.xlsx";
        InputStream stream = new FileInputStream(fileName);

        Workbook wb = null;
        boolean isExcel2003 = false;
        if (isExcel2003) {
            wb = new HSSFWorkbook(stream);
        } else {
            wb = new XSSFWorkbook(stream);
        }

        Sheet sheet = wb.getSheetAt(0);
        if (sheet == null || sheet.getLastRowNum() == 0)
            return;

        //Row headerRow = sheet.getRow(0);
        execute("TRUNCATE searchcategory");
        execute("INSERT INTO `searchCategory` VALUES (NULL, '造价监督', '001', '0', '1', '0', '1',NULL,NULL,'amdin', '2018-04-28 00:00:00', NULL, NULL)");

        String sql = "";


        /*第一列*/
        int col = 0;
        int num = 1;
        for(int i=2; i<1000; i++){
            Row row = sheet.getRow(i);
            if(row==null)
                break;
            String val = row.getCell(col).getStringCellValue();

            if(StringUtils.isNotBlank(val)){

                System.out.println(val);
                sql = "INSERT INTO `searchCategory` VALUES (NULL, '造价监督', 'num', '1', '2', '0', '1',NULL,NULL,'amdin', '2018-04-28 00:00:00', NULL, NULL)"
                        .replace("造价监督",val);
               Object id = execute(sql);

               setFont(wb,String.valueOf(id),col,row);

            }
        }


        System.out.println("\r\n\r\n");

        /*第二列*/
        col = 1;
        for(int i=2; i<1000; i++){
            Row row = sheet.getRow(i);
            if(row==null)
                break;
            String val = row.getCell(col).getStringCellValue();

            if(StringUtils.isNotBlank(val)){
                String parent = findParent(col,row);
                System.out.println(parent + " - " + val);

                String parentId = getParentId(parent);

                sql = "INSERT INTO `searchCategory` VALUES (NULL, '造价监督', '', 'parentId', '3', '0', '1',NULL,NULL,'amdin', '2018-04-28 00:00:00', NULL, NULL)"
                        .replace("parentId",parentId)
                        .replace("造价监督",val);
                Object id = execute(sql);

                setFont(wb,String.valueOf(id),col,row);
            }

        }

        System.out.println("\r\n\r\n");

        /*第3列*/
        col = 2;
        for(int i=2; i<1000; i++){
            Row row = sheet.getRow(i);
            if(row==null)
                break;
            String val = row.getCell(col).getStringCellValue();

            if(StringUtils.isNotBlank(val)){
                String parent = findParent(col,row);
                System.out.println(parent + " - " + val);

                String parentId = getParentId(parent);

                sql = "INSERT INTO `searchCategory` VALUES (NULL, '造价监督', '', 'parentId', '4', '0', '1',NULL,NULL,'amdin', '2018-04-28 00:00:00', NULL, NULL)"
                        .replace("parentId",parentId)
                        .replace("造价监督",val);
                Object id = execute(sql);

                setFont(wb,String.valueOf(id),col,row);
            }

        }

        System.out.println("\r\n\r\n 第4列 \r\n" +
                "\n");

        /*第4列*/
        col = 3;
        for(int i=2; i<1000; i++){
            Row row = sheet.getRow(i);
            if(row==null)
                break;
            String val = row.getCell(col).getStringCellValue();

            if(StringUtils.isNotBlank(val)){
                String parent = findParent(col,row);
                System.out.println(parent + " - " + val);

                String parentId = getParentId(parent);

                sql = "INSERT INTO `searchCategory` VALUES (NULL, '造价监督', '', 'parentId', '5', '0', '1',NULL,NULL,'amdin', '2018-04-28 00:00:00', NULL, NULL)"
                        .replace("parentId",parentId)
                        .replace("造价监督",val);
                Object id = execute(sql);

                setFont(wb,String.valueOf(id),col,row);
            }

        }

        System.out.println("\r\n\r\n 第5列 \r\n" +
                "\n");


        /*第5列*/
        col = 4;
        for(int i=2; i<1000; i++){
            Row row = sheet.getRow(i);
            if(row==null)
                break;
            String val = row.getCell(col).getStringCellValue();

            if(StringUtils.isNotBlank(val)){
                String parent = findParent(col,row);
                System.out.println(parent + " - " + val);

                String parentId = getParentId(parent);

                sql = "INSERT INTO `searchCategory` VALUES (NULL, '造价监督', '', 'parentId', '6', '0', '1',NULL,NULL,'amdin', '2018-04-28 00:00:00', NULL, NULL)"
                        .replace("parentId",parentId)
                        .replace("造价监督",val);
                Object id = execute(sql);

                setFont(wb,String.valueOf(id),col,row);
            }

        }

        System.out.println("\r\n\r\n 第7列 \r\n" +
                "\n");


        /*第7列 表名称*/
        col = 6;
        StringBuilder builder = new StringBuilder();
        for(int i=2; i<300; i++){
            Row row = sheet.getRow(i);
            if(row==null)
                break;

            Cell cell = row.getCell(col);
            if(cell==null)
            {
                continue;
            }

            String val = row.getCell(col).getStringCellValue();
            String field = row.getCell(col+1).getStringCellValue();

            if(StringUtils.isNotBlank(val)){
                String parent = findParentLoop(col-1,row);
                System.out.println(parent + " - " + val + " - " + field);

                if(field.contains("未找到相应字段") || field.contains("是否可以直接") || field.contains("不确定")){
                    field = "";
                }

                sql = String.format("update searchCategory set tablename = '%s',tablefield= '%s',isleaf=1 where categoryid = '%s'",val,field,parent.split(";")[1]);
                execute(sql);

//                Object numObj = query(String.format("select count(0) from searchCategory where categoryname = '%s'",parent));
//                Integer num = Integer.parseInt(String.valueOf(numObj));
//                if(num>1){
//                    System.out.println("没有处理");
//                    builder.append(String.format("update searchCategory set tablename = '%s',tablefield= '%s',isleaf=1 where categoryname = '%s' and categoryid = ?;\r\n",val,field,parent));
//                }else{
//
//                }


            }


        }

        System.out.println(builder.toString());

//        for (int i = 0; i < wb.getNumberOfSheets(); i++) {
//
//
//            Row row = null;
//            System.out.println(sheet.getLastRowNum());
////            for (int r = 1; r <= sheet.getLastRowNum(); r++) {
////                row = sheet.getRow(r);
////                if (row == null)
////                    continue;
////
////                short minColIx = row.getFirstCellNum();
////                short maxColIx = row.getLastCellNum();
////            }
//
//        }
    }

    static String findParent(int col,Row row){
        String val = row.getCell(col-1).getStringCellValue();

        if(StringUtils.isNotBlank(val)){
            parent = val;
            System.out.println("\r\n");
        }else{
           val = parent;
        }

        return val;
    }

    static String findParentLoop(int col,Row row){
        String val = row.getCell(col-1).getStringCellValue();

        if(StringUtils.isNotBlank(val)){
            String id = getFont(col-1,row);
            return val + ";" + id;
        }else{
            return findParentLoop(col-1,row);
        }
    }

    private static HikariDataSource dataSource = new HikariDataSource(getConfig());
    private  static  Connection connection;

    private static HikariConfig getConfig() {
        HikariConfig config = new HikariConfig();

        //config.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/test?useUnicode=true&characterEncoding=utf8&useSSL=false");

        config.setJdbcUrl("jdbc:mysql://192.168.100.92:3306/bd?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=UTC");
        //config.setJdbcUrl("jdbc:mysql://10.170.128.56:3306/bd?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=UTC");
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setUsername("root");
        config.setPassword("123456");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmptCacheSqlLimit", "2048");

        return config;
    }
    public static Object execute(String sql) throws  Exception {

        if(connection==null)
        connection  = dataSource.getConnection();

        PreparedStatement statement = connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
        statement.execute();

        Object val = null;
        ResultSet resultSet = statement.getGeneratedKeys();
        if (resultSet != null) {
            if (resultSet.next())
                val = resultSet.getInt(1);
        }
        System.out.println(val);
        return val;

    }

    public static Object query(String sql) throws  Exception {

        if(connection==null)
            connection  = dataSource.getConnection();

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);

        Object val = null;
        int num = 0;
        while (resultSet.next()) {
            num++;
            val = resultSet.getObject(1);
        }

        if(num>1)
            throw  new Exception("多个结果" + sql);

        return val;
    }


    public static String getParentId(String parentName) throws Exception {
        Object id = query("select * from searchCategory where categoryName = '"+parentName+"'");
        return id.toString();
    }

    public static void setFont(Workbook wb,String val,int col,Row row){
        XSSFCellStyle style = (XSSFCellStyle) wb.createCellStyle();
        XSSFFont font = (XSSFFont) wb.createFont();
        font.setFontName(val);
        style.setFont(font);
        row.getCell(col).setCellStyle(style);
    }

    public static String getFont(int col,Row row){
        String name = ((XSSFCellStyle)row.getCell(col).getCellStyle()).getFont().getFontName();
        return name;
    }

    //手续不全预警标段 -> 标段
    //实施项目信息 -> 项目信息
    //计量情况汇总变更 -> 变更

    /*

update searchCategory set tablename = '实施项目信息(D_SSGCXM)',tablefield= '项目名称',isleaf=1 where categoryname = '项目名称' and categoryid = 5;
update searchCategory set tablename = '实施项目信息(D_SSGCXM)',tablefield= '工可批复单位',isleaf=1 where categoryname = '批复单位' and categoryid = ?;
update searchCategory set tablename = '实施项目信息(D_SSGCXM)',tablefield= '工可批复文号',isleaf=1 where categoryname = '批复文号' and categoryid = ?;
update searchCategory set tablename = '实施项目信息(D_SSGCXM)',tablefield= '工可批复金额',isleaf=1 where categoryname = '估算批复金额(万元)' and categoryid = ?;
update searchCategory set tablename = '实施项目信息(D_SSGCXM)',tablefield= '概算批复单位',isleaf=1 where categoryname = '批复单位' and categoryid = ?;
update searchCategory set tablename = '实施项目信息(D_SSGCXM)',tablefield= '概算批复文号',isleaf=1 where categoryname = '批复文号' and categoryid = ?;
update searchCategory set tablename = '实施项目信息(D_SSGCXM)',tablefield= '概算批复金额',isleaf=1 where categoryname = '估算批复金额(万元)' and categoryid = ?;
update searchCategory set tablename = '设计项目信息(D_SJGCXM)',tablefield= '项目名称',isleaf=1 where categoryname = '项目名称' and categoryid = ?;
update searchCategory set tablename = '设计项目信息(D_SJGCXM)',tablefield= '项目名称',isleaf=1 where categoryname = '项目简称' and categoryid = ?;
update searchCategory set tablename = '工可项目信息',tablefield= '项目简称',isleaf=1 where categoryname = '项目简称' and categoryid = ?;
update searchCategory set tablename = '财务项目建设基本情况(D_CW_JSXMJBXM)',tablefield= '建设单位',isleaf=1 where categoryname = '建设单位' and categoryid = ?;
update searchCategory set tablename = '工可项目信息',tablefield= '主线全长',isleaf=1 where categoryname = '主线全长(km)' and categoryid = ?;
update searchCategory set tablename = '设计项目信息(D_SJGCXM)',tablefield= '项目简称',isleaf=1 where categoryname = '项目简称' and categoryid = ?;
update searchCategory set tablename = '财务项目建设基本情况(D_CW_JSXMJBXM)',tablefield= '建设单位',isleaf=1 where categoryname = '建设单位' and categoryid = ?;
update searchCategory set tablename = '设计项目信息(D_SJGCXM)',tablefield= '主线全长',isleaf=1 where categoryname = '主线全长(km)' and categoryid = ?;
update searchCategory set tablename = '实施项目信息(D_SSGCXM)',tablefield= '项目简称',isleaf=1 where categoryname = '项目简称' and categoryid = ?;
update searchCategory set tablename = '合同资料(D_HTZL)',tablefield= '签约合同金额',isleaf=1 where categoryname = '签约合同金额' and categoryid = ?;
update searchCategory set tablename = '合同清单(D_HTQD)',tablefield= '合同清单金额',isleaf=1 where categoryname = '合同清单金额' and categoryid = ?;
update searchCategory set tablename = '施工图复核(D_SGTFH)',tablefield= '施工图复核金额',isleaf=1 where categoryname = '施工图复核' and categoryid = ?;
update searchCategory set tablename = '中期支付证书（D_ZQZFZS）',tablefield= '变更后金额（是否准确）',isleaf=1 where categoryname = '变更后金额' and categoryid = ?;
update searchCategory set tablename = '合同资料(D_HTZL)',tablefield= '工程项目',isleaf=1 where categoryname = '标段简称' and categoryid = ?;
update searchCategory set tablename = '合同资料(D_HTZL)',tablefield= '签约合同金额',isleaf=1 where categoryname = '签约合同金额' and categoryid = ?;
update searchCategory set tablename = '标段信息(D_SGBD)',tablefield= '工程项目',isleaf=1 where categoryname = '项目简称' and categoryid = ?;
update searchCategory set tablename = '标段信息(D_SGBD)',tablefield= '标段简称',isleaf=1 where categoryname = '标段简称' and categoryid = ?;
update searchCategory set tablename = '合同资料(D_HTZL)',tablefield= '签约合同金额',isleaf=1 where categoryname = '签约合同金额' and categoryid = ?;
update searchCategory set tablename = '合同清单(D_HTQD)',tablefield= '合同清单金额',isleaf=1 where categoryname = '合同清单金额' and categoryid = ?;
update searchCategory set tablename = '合同资料(D_HTZL)',tablefield= '签约合同金额',isleaf=1 where categoryname = '签约合同金额' and categoryid = ?;
update searchCategory set tablename = '工程变更计量一览表(D_GCBBJJYLB)',tablefield= '工程项目',isleaf=1 where categoryname = '项目简称／业主单位' and categoryid = ?;
update searchCategory set tablename = '清单支付报表(D_QDZFBB)',tablefield= '工程项目',isleaf=1 where categoryname = '项目简称／业主单位' and categoryid = ?;
update searchCategory set tablename = '合同资料(D_HTZL)',tablefield= '签约合同金额',isleaf=1 where categoryname = '签约合同金额' and categoryid = ?;
update searchCategory set tablename = '合同清单(D_HTQD)',tablefield= '合同清单金额',isleaf=1 where categoryname = '清单合同金额' and categoryid = ?;
update searchCategory set tablename = '中期支付证书（D_ZQZFZS）',tablefield= '工程项目',isleaf=1 where categoryname = '项目简称' and categoryid = ?;
update searchCategory set tablename = '中期支付证书（D_ZQZFZS）',tablefield= '合同金额',isleaf=1 where categoryname = '签约合同金额' and categoryid = ?;
update searchCategory set tablename = '合同清单(D_HTQD)',tablefield= '合同清单金额',isleaf=1 where categoryname = '清单合同金额' and categoryid = ?;
update searchCategory set tablename = '中期支付证书（D_ZQZFZS）',tablefield= '变更后金额',isleaf=1 where categoryname = '变更后金额' and categoryid = ?;
update searchCategory set tablename = '中期支付证书（D_ZQZFZS）',tablefield= '',isleaf=1 where categoryname = '累计计量' and categoryid = ?;
update searchCategory set tablename = '中期支付证书（D_ZQZFZS）',tablefield= '到本期末完成金额',isleaf=1 where categoryname = '累计支付' and categoryid = ?;
update searchCategory set tablename = '财务项目建设基本情况(D_CW_JSXMJBXM)',tablefield= '建筑安装费用审批概算',isleaf=1 where categoryname = '审批概算' and categoryid = ?;
update searchCategory set tablename = '财务项目建设基本情况(D_CW_JSXMJBXM)',tablefield= '建筑安装费用合同金额',isleaf=1 where categoryname = '合同金额' and categoryid = ?;
update searchCategory set tablename = '财务项目建设基本情况(D_CW_JSXMJBXM)',tablefield= '建筑安装费用已发生金额',isleaf=1 where categoryname = '已发生金额' and categoryid = ?;
update searchCategory set tablename = '财务项目建设基本情况(D_CW_JSXMJBXM)',tablefield= '设备购置费批准概算',isleaf=1 where categoryname = '审批概算' and categoryid = ?;
update searchCategory set tablename = '财务项目建设基本情况(D_CW_JSXMJBXM)',tablefield= '设备购置费合同金额',isleaf=1 where categoryname = '合同金额' and categoryid = ?;
update searchCategory set tablename = '财务项目建设基本情况(D_CW_JSXMJBXM)',tablefield= '设备购置费已发生金额',isleaf=1 where categoryname = '已发生金额' and categoryid = ?;
update searchCategory set tablename = '财务项目建设基本情况(D_CW_JSXMJBXM)',tablefield= '其他费用批准概算',isleaf=1 where categoryname = '审批概算' and categoryid = ?;
update searchCategory set tablename = '财务项目建设基本情况(D_CW_JSXMJBXM)',tablefield= '其他费用合同金额',isleaf=1 where categoryname = '合同金额' and categoryid = ?;
update searchCategory set tablename = '财务项目建设基本情况(D_CW_JSXMJBXM)',tablefield= '其他费用已发生金额',isleaf=1 where categoryname = '已发生金额' and categoryid = ?;
update searchCategory set tablename = '财务项目建设基本情况(D_CW_JSXMJBXM)',tablefield= '建设项目管理费金额',isleaf=1 where categoryname = '金额' and categoryid = ?;
update searchCategory set tablename = '财务项目建设基本情况(D_CW_JSXMJBXM)',tablefield= '预付工程款金额',isleaf=1 where categoryname = '金额' and categoryid = ?;
update searchCategory set tablename = '财务项目建设基本情况(D_CW_JSXMJBXM)',tablefield= '固定资产金额',isleaf=1 where categoryname = '金额' and categoryid = ?;
update searchCategory set tablename = '财务项目建设基本情况(D_CW_JSXMJBXM)',tablefield= '货币资金余额',isleaf=1 where categoryname = '金额' and categoryid = ?;
update searchCategory set tablename = '财务项目建设基本情况(D_CW_JSXMJBXM)',tablefield= '预付备料款金额',isleaf=1 where categoryname = '金额' and categoryid = ?;
update searchCategory set tablename = '综合查询_计量情况汇总表(D_CX_JLQKHZB)',tablefield= '工程项目',isleaf=1 where categoryname = '项目简称／业主单位' and categoryid = ?;
update searchCategory set tablename = '综合查询_计量情况汇总表(D_CX_JLQKHZB)',tablefield= '复核金额',isleaf=1 where categoryname = '施工图复核' and categoryid = ?;
update searchCategory set tablename = '施工图复核(D_SGTFH)',tablefield= '施工图复核金额',isleaf=1 where categoryname = '施工图复核' and categoryid = ?;
update searchCategory set tablename = '综合查询_计量情况汇总表(D_CX_JLQKHZB)',tablefield= '标段编号',isleaf=1 where categoryname = '标段简称' and categoryid = ?;
update searchCategory set tablename = '综合查询_计量情况汇总表(D_CX_JLQKHZB)',tablefield= '合同金额',isleaf=1 where categoryname = '签约合同金额' and categoryid = ?;
update searchCategory set tablename = '合同清单(D_HTQD)',tablefield= '合同清单金额',isleaf=1 where categoryname = '清单合同金额' and categoryid = ?;

     */
}
