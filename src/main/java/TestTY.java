import MyDate.DateUtil;
//import sun.misc.BASE64Decoder;
//import sun.misc.BASE64Encoder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterOutputStream;

public class TestTY {

    static int HOUR = 10;
    static int MINUTE = 0;
    static int QUAN = 5;

    // 研究文本压缩
    public static void main(String[] args) throws Exception {

        List<String> result = new ArrayList<>();
        result.add("1");
        result.add("2");

        List<String> noExperts = new ArrayList<>();
        noExperts.add("a");
        noExperts.add("b");
        noExperts.add("a1");
        noExperts.add("b2");
        noExperts.add("c3");

        int otherCount = 5 - result.size();
        List<String> dwdExpertInsRels = noExperts.subList(0, Math.min(otherCount, noExperts.size()));
        result.addAll(dwdExpertInsRels);

        System.out.println(result);


        //String content = fileDemo.FileHelper.readTxtFile("d:/1.txt");
        String content = fileDemo.FileHelper.readTxtFile("d:/1017.txt");

        String zipContent = DeflaterUtils.zipString(content);
        String unzipContent =DeflaterUtils.unzipString(zipContent);

        zipContent = fileDemo.FileHelper.readTxtFile("d:/1017-encode.txt");
        unzipContent =DeflaterUtils.unzipString(zipContent);


        if(content.startsWith("{")){
            System.out.println("is json 1");
        }
        if(zipContent.startsWith("{")){
            System.out.println("is json 2");
        }
        if(unzipContent.startsWith("{")){
            System.out.println("is json 3");
        }

//        String zipContent = zipBase64(content);
//        String unzipContent = unzipBase64(zipContent);

        System.out.println("ok");

    }


    public static String zipBase64(String text) throws IOException {
        ByteArrayOutputStream out = null;
        DeflaterOutputStream deflaterOutputStream = null;
        try {
            out = new ByteArrayOutputStream();
            deflaterOutputStream = new DeflaterOutputStream(out);
            deflaterOutputStream.write(text.getBytes("UTF-8"));
//            BASE64Encoder encoder = new BASE64Encoder();
//            return encoder.encode(out.toByteArray());
            // 从JKD 9开始rt.jar包已废除，从JDK 1.8开始使用java.util.Base64.Encoder
            Base64.Encoder encoder = Base64.getEncoder();
            return encoder.encodeToString(out.toByteArray());
        } finally {
            if (out != null) {
                out.close();
            }
            if(deflaterOutputStream != null){
                deflaterOutputStream.close();
            }
        }
    }

    /**
     * 解压字符串,默认utf-8
     *
     * @param text
     * @return
     */
    public static String unzipBase64(String text) throws IOException {
//        BASE64Decoder decoder = new BASE64Decoder();
        // 从JKD 9开始rt.jar包已废除，从JDK 1.8开始使用java.util.Base64.Encoder
        Base64.Decoder decoder = Base64.getDecoder();

        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            try (InflaterOutputStream outputStream = new InflaterOutputStream(os)) {
//                outputStream.write(decoder.decodeBuffer(text));
                outputStream.write(decoder.decode(text));
            }
            return new String(os.toByteArray(), "UTF-8");
        }
    }

    public static void go() throws IOException, InterruptedException {
        String leftButton = "d:/adb/adb.exe shell input tap 471.2  729.2";
        click(leftButton);

        String q1 = "d:/adb/adb.exe shell input tap 699  346.7";
        String q2 = "d:/adb/adb.exe shell input tap 699  575.5";
        String q3 = "d:/adb/adb.exe shell input tap 699  807.4";
        String q4 = "d:/adb/adb.exe shell input tap 699  1039";
        String q5 = "d:/adb/adb.exe shell input tap 353.5  536.5";

        String str =
                QUAN == 1 ? q1 :
                        QUAN == 2 ? q2 :
                                QUAN == 3 ? q3 :
                                        QUAN == 4 ? q4 :
                                                QUAN == 5 ? q5 : "";

        System.out.println(str);

        for (int i = 0; i < 20; i++) {
            System.out.println("click-" + DateUtil.format(new Date()));
            click(str);
            Thread.sleep(10);
        }
    }

    public static void click(String cmd) throws IOException, InterruptedException {
        CmdUtil.execCmdSync(cmd, (success, exitVal, error, output, originalCmd) -> {
            if (!success) {
                throw new IOException(error);
            }
        });
    }

    public static void connect() throws IOException, InterruptedException {
        String cmd = "d:/adb/adb.exe connect 127.0.0.1:7555";
        CmdUtil.execCmdSync(cmd, (success, exitVal, error, output, originalCmd) -> {
            System.out.println(output);
            System.out.println(error);
        });
    }
}
