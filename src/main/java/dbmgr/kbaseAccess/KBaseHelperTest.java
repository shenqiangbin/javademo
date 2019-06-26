package dbmgr.kbaseAccess;

import common.P;

import java.sql.SQLException;
import java.util.List;

public class KBaseHelperTest {
    public static void main(String[] args){
        P.print("ok");
        //search();
        //testSearch();
        testSort();
    }

    public static void search(){

        KBaseHelper kBaseHelper = new KBaseHelper("jdbc:kbase://192.168.100.92","DBOWN","");

        String sql = "SELECT modelname FROM MODEL_KBASE limit 0,10";
        List<KbaseTestItem> list = kBaseHelper.query(sql,null,KbaseTestItem.class,new String[]{"modelname"});

        for(KbaseTestItem item : list){
            P.print(item.getModelname());
        }
    }

    public static void testSearch(){

        KBaseHelper kBaseHelper = new KBaseHelper("jdbc:kbase://192.168.100.92","DBOWN","");

        String sql = "SELECT DATAID FROM PARTNERSEARCHDATA_KBASE";
        List<KbaseTestItem> list = kBaseHelper.query(sql,null,KbaseTestItem.class,new String[]{"DATAID"});

        System.out.println(list.size());

        StringBuilder builder = new StringBuilder("select * from tbeg_main_event where \"EVENT_ID\" IN (");

        for(KbaseTestItem item : list){
            //P.print(item.getDataid());
            builder.append(item.getDataid()).append(",");
        }

        builder.append(")");

        System.out.println(builder.toString());
    }

    public static void testSort(){
        KBaseHelper kBaseHelper = new KBaseHelper("jdbc:kbase://192.168.105.89","DBOWN","");
        try {
            kBaseHelper.showRs();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}


