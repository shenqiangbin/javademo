package dbmgr.kbaseAccess;

import common.P;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class KBaseHelperTest {
    public static void main(String[] args) throws SQLException {
        P.print("ok");
        //search();
        //search2();
        //testSearch();
        //testSort();
        //testSort2();
        //guangmingAuthor();
        //testKbase();
        testKbaseSort();
    }

    public static void testKbase() throws SQLException {
        KBaseHelper kBaseHelper = new KBaseHelper("jdbc:kbase://10.31.68.44", "DBOWN", "");
        String sql = "select 公开号 from EKR_BIBLIOGRAPHIC2018 limit 10 ";
        String[] fields = new String[]{"公开号"};
        List<LinkedHashMap<String, Object>> list = kBaseHelper.query(sql, fields);
        for (LinkedHashMap<String, Object> item : list) {
            P.print(item.toString());
        }
    }

    public static void testKbaseSort() throws SQLException {
        KBaseHelper kBaseHelper = new KBaseHelper("jdbc:kbase://10.31.68.44", "DBOWN", "");
        List<String> sqlList = new ArrayList<String>() {{
            //add("dbum make sortcol by CPCCLASSNUMBER (EKR_BIBLIOGRAPHIC2018.CPC分类号)");
//            add("dbum make sortcol by CPCCLASSNUMBER (EKR_BIBLIOGRAPHIC2018.CPC其他分类号)");
//            add("dbum make sortcol by CPCCLASSXZ (EKR_BIBLIOGRAPHIC2018.IPC分类号小组)");
//            add("dbum make sortcol by CPCCLASSHEAD (EKR_BIBLIOGRAPHIC2018.IPC主分类号部)");
//            add("dbum make sortcol by CPCCLASSDL (EKR_BIBLIOGRAPHIC2018.IPC主分类号大类)");
//            add("dbum make sortcol by CPCCLASSXL (EKR_BIBLIOGRAPHIC2018.IPC主分类号小类)");
//            add("dbum make sortcol by CPCCLASSDZ (EKR_BIBLIOGRAPHIC2018.IPC主分类号大组)");
//            add("dbum make sortcol by CPCCLASSXZ (EKR_BIBLIOGRAPHIC2018.IPC主分类号小组)");
//            add("dbum make sortcol by CPCCLASSNUMBER (EKR_BIBLIOGRAPHIC2018.FI主分类号)");
//            add("dbum make sortcol by CPCCLASSHEAD (EKR_BIBLIOGRAPHIC2018.FI主分类号部)");
            add("dbum make sortcol by CPCCLASSDL (EKR_BIBLIOGRAPHIC2018.CPC分类号大类)");
            add("dbum make sortcol by INTEGER (EKR_BIBLIOGRAPHIC2018.简单同族国家_地区数量)");
//            add("dbum make sortcol by CPCCLASSXL (EKR_BIBLIOGRAPHIC2018.FI主分类号小类)\nGO");
//            add("dbum make sortcol by CPCCLASSDZ (EKR_BIBLIOGRAPHIC2018.FI主分类号大组)\nGO");
//            add("dbum make sortcol by CPCCLASSXZ (EKR_BIBLIOGRAPHIC2018.FI主分类号小组)\r\nGO");
            add("DBUM REFRESH SORTFILE OF TABLE EKR_BIBLIOGRAPHIC2018");
        }};
        for (String sql : sqlList) {
            kBaseHelper.executeNoQueryWithBack(sql);
        }
    }

    public static void guangmingAuthor() throws SQLException {

        KBaseHelper kBaseHelper = new KBaseHelper("jdbc:kbase://10.120.150.189", "DBOWN", "kbase189");

        String authors = "励建荣;谢晶;孔保华;江连洲;赵谋明;熊善柏;徐岩;王兴国;白卫东;杨贤庆;姜绍通;李琳;李杨;陈卫;陈复生;李来好;赵改名;吴燕燕;徐幸莲;张和平;孙宝国;李洪军;王锡昌;张灏;任发政;赵思明;李崎;金青哲;薛长湖;潘思轶";
        authors = "谢晶;孔保华;江连洲;赵谋明;熊善柏;徐岩;王兴国;白卫东;杨贤庆;姜绍通;李琳;李杨;陈卫;陈复生;李来好;赵改名;吴燕燕;徐幸莲;张和平;孙宝国;李洪军;王锡昌;张灏;任发政;赵思明;李崎;金青哲;薛长湖;潘思轶";

        String format = "select 专家姓名,学者,H指数,G指数,当前职称,学者职称,职称级别,研究方向,研究领域,第一作者篇数,第一学者篇数,是否专家数据 from newauthor1,newauthor2,newauthor3,newauthor4,newauthor5,newauthor6,newauthor7, newauthorl where 专家姓名 = '励建荣' limit 10";

        for (String author : authors.split(";")) {
            String sql = format.replace("励建荣", author);

            String[] args = new String[]{"专家姓名", "学者", "H指数", "G指数", "当前职称", "学者职称", "职称级别", "研究方向", "研究领域", "第一作者篇数", "第一学者篇数",
                    "是否专家数据"};
            args = new String[]{"专家姓名", "学者", "H指数", "G指数", "当前职称", "学者职称", "是否专家数据"};
            List<LinkedHashMap<String, Object>> list = kBaseHelper.query(sql, args);
            for (LinkedHashMap<String, Object> item : list) {
                P.print(item.toString());
            }
        }


    }

    public static void search2() throws SQLException {

        KBaseHelper kBaseHelper = new KBaseHelper("jdbc:kbase://10.120.130.25", "DBOWN", "");

        String sql = "select SYS_FLD_SYSID from PERIOD_METADATA where MYSQLID = '14612'";
        List<LinkedHashMap<String, Object>> list = kBaseHelper.query(sql, new String[]{"SYS_FLD_SYSID"});
        for (LinkedHashMap<String, Object> item : list) {
            P.print(item.get("SYS_FLD_SYSID"));
        }
    }

    public static void search() throws SQLException {

        KBaseHelper kBaseHelper = new KBaseHelper("jdbc:kbase://192.168.26.200", "SJQB", "sjqb");

        String sql = "SELECT 文件名,编号,条目题名,年鉴中文名,年鉴年份,父亲节点,印刷页码,卷,拼音刊名,专辑代码,光盘号 " +
                "FROM CSYD1949,CSYD2006,CSYD2007,CSYD2008,CSYD2009,CSYD2010,CSYD2011,CSYD2012,CSYD2013,CSYD2014,CSYD2015,CSYD2016,CSYD2017,CSYD2018,CSYD2019,CSYD2020,CSYD2021,CSYD2022,CSYDTEMP where 编号 = 'N2012120027' and 文件名 = 'N2012120027000005'";
        //List<KbaseTestItem> list = kBaseHelper.query(sql,null,KbaseTestItem.class,new String[]{"modelname"});
        List<LinkedHashMap<String, Object>> list = kBaseHelper.query(sql, new String[]{"文件名", "编号"});
        for (LinkedHashMap<String, Object> item : list) {
            P.print(item.get("文件名"));
        }
    }

    public static void testSearch() {

        KBaseHelper kBaseHelper = new KBaseHelper("jdbc:kbase://192.168.100.92", "DBOWN", "");

        String sql = "SELECT DATAID FROM PARTNERSEARCHDATA_KBASE";
        List<KbaseTestItem> list = kBaseHelper.query(sql, null, KbaseTestItem.class, new String[]{"DATAID"});

        System.out.println(list.size());

        StringBuilder builder = new StringBuilder("select * from tbeg_main_event where \"EVENT_ID\" IN (");

        for (KbaseTestItem item : list) {
            //P.print(item.getDataid());
            builder.append(item.getDataid()).append(",");
        }

        builder.append(")");

        System.out.println(builder.toString());
    }

    public static void testSort() {
        KBaseHelper kBaseHelper = new KBaseHelper("jdbc:kbase://192.168.105.89", "DBOWN", "");
        try {
            kBaseHelper.showRs();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void testSort2() {
        KBaseHelper kBaseHelper = new KBaseHelper("jdbc:kbase://10.120.130.89", "DBOWN", "");
        try {
            String sql = "SELECT * FROM MODEL_KBASE where modelname = '经济经济'";
            List<LinkedHashMap<String, Object>> list = kBaseHelper.query(sql, new String[]{"modelname"});
            for (LinkedHashMap<String, Object> item : list) {
                P.print(item.get("modelname"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


