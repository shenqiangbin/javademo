import common.P;
import org.apache.commons.lang3.StringUtils;


import java.text.SimpleDateFormat;
import java.util.Date;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;


public class TestJava {

    public static void main(String[] args) throws Exception {

        System.out.println(containHanZi("AZ1234142"));
        System.out.println(containHanZi("1234142"));
        System.out.println(containHanZi("中12342134"));
        System.out.println(containHanZi("id"));
//
//        P.print("ok");
//
//        String str = "1,2,3,4,5,6,76,1,2,3,4,5,6,76,1,2,3,4,5,6,76,1,2,3,4,5,6,76,1,2,3,4,5,6,76,1,2,3,4,5,6,76,1,2,3,4,5,6,76,1,2,3,4,5,6,76";
//        String[] arr =  str.split(",");
//        String[] arr2 =  str.split(",", -1);
//
//        String resetSyncEndTime = "2020-01-02 08:12:30";
        //Date d = new Date(resetSyncEndTime);

//        Date d = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(resetSyncEndTime);
//
//        P.print(padLeftZeros1("123",5));
//        P.print(padLeftZeros2("123",5));
//
//        testImg();
        //P.print(builder.toString());
        test2();
    }

    public static void  test2(){
        String txt = "";
        StringBuilder result = new StringBuilder();
        String[] arr = txt.split("\n");
        for(int i =0; i<arr.length; i++){
            String item = arr[i];
            String[] itemArr = item.split(" ");
            String first = itemArr[0];
            if(!first.startsWith("SYS"))
                result.append(first).append(",");
        }
        System.out.println(result.toString());
    }

    public static boolean containHanZi(String str){
        return  str.length() != str.getBytes().length;
    }

    public static void testImg() throws IOException {
        String pic = "I:\\pic\\00bdae0d0dda17ab2c4f6a8e85c56ddd_720w.jpg";
        BufferedImage bi = ImageIO.read(new FileInputStream(pic));
        Color wColor = new Color(255, 255, 255);
        for (int i = 0; i < bi.getWidth(); i++) {
            for (int j = 0; j < bi.getHeight(); j++) {
                System.out.println(bi.getRGB(i, j));

                int color = bi.getRGB(i, j);
                Color oriColor = new Color(color);
                int red = oriColor.getRed();
                int green = oriColor.getGreen();
                int blue = oriColor.getBlue();
                if(red == 255 && green == 255){
                    bi.setRGB(i, j, wColor.getRGB());
                }
            }
        }
        String type = pic.substring(pic.lastIndexOf(".") + 1, pic.length());
        OutputStream outputStream = new FileOutputStream("I:\\pic\\1." + type);
        ImageIO.write(bi, type, outputStream);
    }

    public static void test() {
        P.print(padLeftZeros1("123", 5));
        P.print(padLeftZeros2("123", 5));

        StringBuilder builder = new StringBuilder();

        for (Integer i = 1; i < 53; i++) {
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

            String code = padLeftZeros(i.toString(), 3);
            //String sql = String.format(format,code,code,code);
            String sql = String.format(format, code);
            builder.append(sql + "\r\n");

        }

        P.print(builder.toString());


        builder = new StringBuilder();

        for (Integer i = 54; i < 105; i++) {
            String format = "INSERT INTO `bd`.`userrole` (`UserID`, `RoleID`, `Status`, `CreateUser`, `CreateTime`, `ModifyUser`, `ModifyTime`) VALUES \n" +
                    "('%s', '2', '1', 'duanfeihu', '2019-03-13 09:11:54', 'duanfeihu', '2019-03-13 09:11:54');";
            String sql = String.format(format, i);
            builder.append(sql + "\r\n");
        }
    }

    public static String padLeftZeros(String str, int n) {
        return String.format("%1$" + n + "s", str).replace(' ', '0');
    }

    public static String padLeftZeros1(String str, int n) {
        return String.format("%0" + n + "d", Integer.parseInt(str));
    }

    public static String padLeftZeros2(String str, int n) {
        return StringUtils.leftPad(str, n, "0");
    }


}

class Person {
    private String name;
    private int age;

    private static String height = "";

    public void say(String msg) {
        String other = "";
        System.out.println("say");

        height = "asdf";
    }

}



