import common.P;

public class TestJava {

    public static void main(String[] args) throws Exception{
        P.print("ok");

        StringBuilder builder = new StringBuilder();

        for(Integer i=1; i<53;i++){
            String format = "INSERT INTO `bd`.`user` (`UserCode`, `UserName`, `Password`, `SSOUserID`, `Status`, `CreateUser`, `CreateTime`, `ModifyUser`, `ModifyTime`, `NickName`, `Phone`, `Mailbox`, `Company`, `Department`, `ImagePath`) \n" +
                    "VALUES ('bigdata%s', 'bigdata%s', 'bigdata%s', NULL, '1', 'duanfeihu', '2019-03-13 09:11:54', 'duanfeihu', '2019-03-13 09:11:54', NULL, NULL, NULL, NULL, NULL, NULL);";

            format = "INSERT INTO MODEL_KBASE (MODELNAME,SOURCEID,STATUS,\n" +
                    "SYNCSTATE,MODELCATEGORYID,CREATEUSER,\n" +
                    "IMAGEURL,TIME,\n" +
                    "ISBOAT,ISPUBLISH)\n" +
                    "VALUES('合肥市经济运行监控平台','488846',1,\n" +
                    "2,1,'bigdata%s',\n" +
                    "'5c0b2799-a89d-4cd2-8b0a-f6b700f7dfe9.jpg','2018-09-12',\n" +
                    "1,1)\n" +
                    "GO";

            String code = padLeftZeros(i.toString(),3);
            //String sql = String.format(format,code,code,code);
            String sql = String.format(format,code);
            builder.append(sql+"\r\n");

        }

        P.print(builder.toString());


        builder = new StringBuilder();

        for(Integer i=54; i<105;i++){
            String format = "INSERT INTO `bd`.`userrole` (`UserID`, `RoleID`, `Status`, `CreateUser`, `CreateTime`, `ModifyUser`, `ModifyTime`) VALUES \n" +
                    "('%s', '2', '1', 'duanfeihu', '2019-03-13 09:11:54', 'duanfeihu', '2019-03-13 09:11:54');";
            String sql = String.format(format,i);
            builder.append(sql+"\r\n");
        }


        //P.print(builder.toString());
    }

    public static String padLeftZeros(String str, int n) {
        return String.format("%1$" + n + "s", str).replace(' ', '0');
    }
}
