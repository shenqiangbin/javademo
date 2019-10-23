package dbmgr.microsoftAccess;

import common.P;
import dbmgr.microsoftAccess.model.MyUser;
import dbmgr.microsoftAccess.model.PageResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Program {

    public static void main(String[] args) {

        try {
            String file = "D:/BGKDB_new.MDB";
            file="D:/BGKDB.MDB;keepMirror=d:/mirrorName";
            //file = "Ammache-Fawzi-database.mdb";
            AccessHelper accessHelper = new AccessHelper(file);

            query(accessHelper);

            String sql = "INSERT INTO user(name) values (?)";
            ArrayList<Object> params = new ArrayList<Object>(Arrays.asList("tom"));
            //int addid = accessHelper.add(sql,params);

            //P.print("addid:" + addid);

            String coutSql = "select max(id) from user";
            String val = accessHelper.executeScalar(coutSql, null);

            P.print("countVal:" + val);

            String updateSql = "update user set name = ? where id = ?";
            int updateResult = accessHelper.update(updateSql, new ArrayList<Object>(Arrays.asList("updatetom", 3)));
            P.print("udpateResult:" + updateResult);

            query(accessHelper);

            PageResult<MyUser> pageResult = accessHelper.queryPage("select * from user", null, 2, 2, MyUser.class);

            P.print(pageResult.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void query(AccessHelper accessHelper) {

        List<MyUser> list = accessHelper.query("select * from user", null, MyUser.class);
        for (MyUser item : list) {
            P.print(item);
        }
    }



}
