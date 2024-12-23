package fileDemo;

import MyHttpClient.HttpHelper;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.FileUtils;
import org.apache.poi.openxml4j.opc.internal.FileHelper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {
    public static void main(String[] args) throws Exception {
        test2();
    }

    public static void test() throws Exception {

        // 创建临时目录 && 临时文件
        Path tmp = Files.createTempDirectory("tmp");
        String fileId = UUID.randomUUID().toString().replaceAll("-", "");
        Path docPath = Files.createTempFile(tmp, "", ".doc");
        Path pngPath = Files.createTempFile(tmp, "", ".png");

        String str = fileDemo.FileHelper.fileToByteConent("d:/2-test2.png");

        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("fileName", "1.jpg");
        paramsMap.put("getByte", str);
        String val = HttpHelper.httpPost("http://10.120.151.2/Ocrwebapi/MapOcrService.asmx/GetLayOut", paramsMap, null);

        File file = FileHelper.getDirectory(new File("D:\\datafortablebigdata\\extraExcel\\52\\统计公报\\2014年闽侯县国民经济和社会发展统计公报.txt"));
        System.out.println(file.getPath());
        System.out.println(file.getAbsolutePath());

        int number = 2;
        String msg = number > 1 ? "大于1" : "小于1";
        int result = number > 2 ? 10 : 20;
    }

    public static void test2() throws Exception {
        String content = FileUtils.readFileToString(new File("E:\\aaa专利\\EP4330841A1.txt"), "UTF-8");


        System.out.println(content);
    }

    /**
     * 解析 REC 字符串
     * @param content
     * @return
     */
    static LinkedHashMap parseRecStr(String content) {
        String[] lines = content.split("\n");

        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        String currentField = null;
        for (String line : lines) {
            String field = getFiledName(line);
            if (field != null) {
                currentField = field;
                map.put(field, getVal(line));
            } else {
                map.put(currentField, map.get(currentField) + "\n" + line);
            }
        }
        return map;
    }

    static String getFiledName(String content) {
        Pattern pattern = Pattern.compile("<(.*?)>=(.*)");
        Matcher matcher = pattern.matcher(content);

        if (matcher.find()) {
            String key = matcher.group(1); // 捕获的第一个组，即键名
            String value = matcher.group(2); // 捕获的第二个组，即键值
            return key;
        }
        return null;
    }

    static String getVal(String content) {
        Pattern pattern = Pattern.compile("<(.*?)>=(.*)");
        Matcher matcher = pattern.matcher(content);

        if (matcher.find()) {
            String key = matcher.group(1); // 捕获的第一个组，即键名
            String value = matcher.group(2); // 捕获的第二个组，即键值
            return value;
        }
        return content;
    }
}
