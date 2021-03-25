package MyImage;

import JSONDemo.JSONUtil;
import MyHttpClient.HttpHelper;
import MyImage.Model.ParseModel;
import com.alibaba.fastjson.JSONObject;
import org.mozilla.universalchardet.UniversalDetector;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReadFile {
    public static void main(String[] args) throws IOException {
        //readFile("D:\\资料\\新建文件夹\\北京_intermed_results.txt");
        //readFile("d:/test/ttest/dic.txt");
        //readFile("d:/更新/107XT02_intermed_results.txt");


        String s = "";
        Charset.forName("UTF-8");
        String abc = new String(s.getBytes("GBK"), "UTF-8");


        String path = "D:\\datafortablebigdata\\extraExcel\\52\\统计公报\\2014年闽侯县国民经济和社会发展统计公报.txt"; // GB18030
        //path = "D:/code/TPI/大数据产品/表格大数据项目/功能/解析 html/file/html2.txt";
        path = "D:\\datafortablebigdata\\extraExcel\\52\\统计公报\\平潭综合实验区2019年国民经济和社会发展统计公报.txt";
        String encode = detector(path);
        // UTF-8


        // base64 对 byte 数组加密后的内容
//        String path = "D:\\datafortablebigdata\\extraExcel\\52\\统计公报\\2014年闽侯县国民经济和社会发展统计公报.txt";
//        //path = "D:/code/TPI/大数据产品/表格大数据项目/功能/解析 html/file/html2.txt";
//        path = "D:\\datafortablebigdata\\extraExcel\\52\\统计公报\\平潭综合实验区2019年国民经济和社会发展统计公报.txt";
//
//        String content = fileToByteConent(path);
//        System.out.println(content);
//        byteContentToFile("e:/abc44.pdf", content);

        test_jiaojianxin();

        // base64 对 byte 数组加密后的内容
        String content2 = fileToByteConent("e:/html2.txt");
        String strVal = byteContentToString(content2);

        String content23 = content2.replace("\r", "\\r").replace("\n", "\\n");
        String strVal2 = byteContentToString(content23);


    }


    public static void test_jiaojianxin() throws IOException {
        String path = "D:\\datafortablebigdata\\extraExcel\\52\\统计公报\\2014年闽侯县国民经济和社会发展统计公报.txt";
        //path = "D:/code/TPI/大数据产品/表格大数据项目/功能/解析 html/file/html2.txt";
        path = "D:\\datafortablebigdata\\extraExcel\\52\\统计公报\\平潭综合实验区2019年国民经济和社会发展统计公报.txt";
        path = "D:\\datafortablebigdata\\extraExcel\\52\\统计公报\\平潭综合实验区2019年国民经济和社会发展统计公报utf8.txt";
        String content2 = fileToByteConent(path);
        JSONObject param = new JSONObject();
        param.put("html_str", content2);

        BASE64Encoder encoder = new BASE64Encoder();
        String str = encoder.encode("平潭综合实验区2019年国民经济和社会发展统计公报".getBytes());
        param.put("title",str);

        String val = HttpHelper.httpPostJSON("http://10.120.146.10:8088/HtmlExtractRes/", param.toString(), null);
        System.out.println(val);

        String newVal = val.replace("\"", "").replace("'", "\"");

        if (!JSONUtil.isJson(newVal)) {
            throw new IOException("解析结果不是JSON格式：" + newVal);
        }

        ParseModel parseModel = JSONUtil.toObject(newVal, ParseModel.class);
        System.out.println("ok");

        parseModel.getIndicators().forEach(m -> {
            System.out.println(m.getName() + " " + m.getUnit() + " " + m.getValue());
        });

        parseModel.getExcels().forEach(m -> {
            try {
                byteContentToFile(String.format("e:/abc/%s.xlsx", m.getTitle()), m.getContent());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

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
        return val;
    }

    public static String fileToByteConent(String path) throws IOException {

        //String charsetName = getFileCharsetName(path);
        String charsetName = detector(path);

        FileInputStream fileInputStream = null;
        fileInputStream = new FileInputStream(path);
        //ByteArrayInputStream inputStream = new ByteArrayInputStream(fileInputStream);

        StringBuilder builder = new StringBuilder();
        int ch = 0;
        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
        while ((ch = fileInputStream.read()) != -1) {
            arrayOutputStream.write(ch);
        }
        String val = arrayOutputStream.toString(charsetName);
        //String abc = new String(arrayOutputStream.toByteArray(),"UTF-8");
        // abc.getBytes();

        BASE64Encoder encoder = new BASE64Encoder();
        //String str = encoder.encode(arrayOutputStream.toByteArray());
        String str = encoder.encode(val.getBytes());
        return str;
    }

    public static String detector(String fileName) {
        String encode = null;
        BufferedInputStream bis = null;

        try {
            bis = new BufferedInputStream(new FileInputStream(fileName));
            int readSize;
            byte[] buffer = new byte[8 * 4096];
            UniversalDetector detector = new UniversalDetector(null);
            while ((readSize = bis.read(buffer)) > 0 && !detector.isDone()) {
                detector.handleData(buffer, 0, readSize);
            }
            detector.dataEnd();
            encode = detector.getDetectedCharset();
            detector.reset();
        } catch (Exception e) {

        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        //return encode;
        return encode.equalsIgnoreCase("UTF-8") ? "UTF-8" : "GBK";
    }

    /**
     * 获取文件编码格式
     *
     * @param file 文件
     * @return 文件编码格式
     * @throws Exception 读取异常
     */
//    public static String getFileCharsetName(String file) throws Exception {
//        String code = null;
//        FileInputStream in = new FileInputStream(file);
//        BufferedInputStream bin = new BufferedInputStream(in);
//        int p = (bin.read() << 8) + bin.read();
//        bin.close();
//        in.close();
//        switch (p) {
//            case 0x5c75:
//                code = "ANSI|ASCII";
//                break;
//            case 0xefbb:
//                code = "UTF-8";
//                break;
//            case 0xfeff:
//                code = "UTF-16BE";
//                break;
//            case 0xfffe:
//                code = "Unicode";
//                break;
//            default:
//                code = "GBK";
//        }
//
//        return code;
//    }
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
