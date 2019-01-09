package dbmgr.kbaseAccess;

import common.P;

import java.util.List;

public class KBaseHelperTest {
    public static void main(String[] args){
        P.print("ok");
        //search();

    }

    public static void search(){

        KBaseHelper kBaseHelper = new KBaseHelper("jdbc:kbase://192.168.100.92","DBOWN","");

        String sql = "SELECT modelname FROM MODEL_KBASE limit 0,10";
        List<KbaseTestItem> list = kBaseHelper.query(sql,null,KbaseTestItem.class,new String[]{"modelname"});

        for(KbaseTestItem item : list){
            P.print(item.getModelname());
        }
    }


}


