package dbmgr.kbaseAccess;

import common.P;

import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;

public class KBaseHelperTest {
    public static void main(String[] args) throws SQLException {
        P.print("ok");
        search();
        //testSearch();
        //testSort();
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

}


