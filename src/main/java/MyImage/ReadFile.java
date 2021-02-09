package MyImage;

import MyHttpClient.HttpHelper;
import com.alibaba.fastjson.JSONObject;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReadFile {
    public static void main(String[] args) throws IOException {
        //readFile("D:\\资料\\新建文件夹\\北京_intermed_results.txt");
        //readFile("d:/test/ttest/dic.txt");
        //readFile("d:/更新/107XT02_intermed_results.txt");

        test_jiaojianxin();

        // base64 对 byte 数组加密后的内容
        String content2 = fileToByteConent("e:/html2.txt");
        String strVal = byteContentToString(content2);

        String content23 = content2.replace("\r","\\r").replace("\n","\\n");
        String strVal2 = byteContentToString(content23);

        // base64 对 byte 数组加密后的内容
        String content = fileToByteConent("e:/2.xlsx");
        System.out.println(content);
        byteContentToFile("e:/22.xlsx", content);

    }


    public static void test_jiaojianxin() throws IOException {
        String content2 = fileToByteConent("e:/html2.txt");
        JSONObject param = new JSONObject();
        param.put("html_str", content2);
        String val = HttpHelper.httpPostJSON("http://10.120.146.10:8088/HtmlExtractRes/", param, null);
        System.out.println(val);
    }

    public static String fileToPlainConent(String path) throws IOException {
        FileInputStream fileInputStream = null;
        fileInputStream = new FileInputStream(path);
        //ByteArrayInputStream inputStream = new ByteArrayInputStream(fileInputStream);

        StringBuilder builder = new StringBuilder();
        int ch = 0;
        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
        while ((ch = fileInputStream.read()) != -1) {
            arrayOutputStream.write(ch);
        }
        String val = arrayOutputStream.toString();
       return  val;
    }

    public static String fileToByteConent(String path) throws IOException {
        FileInputStream fileInputStream = null;
        fileInputStream = new FileInputStream(path);
        //ByteArrayInputStream inputStream = new ByteArrayInputStream(fileInputStream);

        StringBuilder builder = new StringBuilder();
        int ch = 0;
        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
        while ((ch = fileInputStream.read()) != -1) {
            arrayOutputStream.write(ch);
        }
        String val = arrayOutputStream.toString();
        BASE64Encoder encoder = new BASE64Encoder();
        String str = encoder.encode(arrayOutputStream.toByteArray());
        return str;
    }

    public static void byteContentToFile(String path, String content) throws IOException {
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] newBy = decoder.decodeBuffer(content);
        FileOutputStream fileOutputStream = new FileOutputStream(path, false);
        fileOutputStream.write(newBy);
    }

    public static String byteContentToString(String content) throws IOException {
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] newBy = decoder.decodeBuffer(content);
        String val = new String(newBy, 0, newBy.length);
        return val;
    }

    public static void readFile(String path) throws IOException {
        FileInputStream fileInputStream = null;
        BufferedReader bufferedReader = null;
        try {
            fileInputStream = new FileInputStream(path);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));

//            StringBuilder builder = new StringBuilder();
//
//            int word;
//            while ((word = bufferedReader.read()) != -1) {
//                builder.append((char)word);
//            }
//
//            System.out.println(builder.toString());

            String dir = "d:/更新/";
            int i = 0;
            int fileNum = 1;
            String fileName = "";
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                i++;
                if (i % 10000 == 0) {
                    fileName = fileNum + "107XT02_intermed_results.txt";
                    createIfNotExist(dir + fileName, true);
                }
                if (!line.contains("测试")) {
                    System.out.println(i + " " + line);

                    writeFile(dir + fileName, line + "\n");
                }
            }

        } finally {
            bufferedReader.close();
            fileInputStream.close();
        }
    }

    public static void createIfNotExist(String path, boolean isFile) throws IOException {
        File file = new File(path);
        //如果文件不存在则创建
        if (!file.exists()) {
            if (!isFile) {
                //如果是目录
                file.mkdirs();
            } else {
                //如果是文件
                file.createNewFile();
            }
        }
    }

    public static void writeFile(String path, String content) throws IOException {
        FileOutputStream fileOutputStream = null;
        BufferedWriter bufferedWriter = null;
        try {
            fileOutputStream = new FileOutputStream(path, true);
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(fileOutputStream));

            bufferedWriter.write(content);
        } finally {
            bufferedWriter.flush();
            bufferedWriter.close();
            fileOutputStream.flush();
            fileOutputStream.close();
        }
    }
}
