package fileDemo;

import MyHttpClient.HttpHelper;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.poi.openxml4j.opc.internal.FileHelper;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Test {
    public static void main(String[] args) throws Exception {


        String str =  fileDemo.FileHelper.fileToByteConent("e:/1.jpg");

        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("fileName","1.jpg");
        paramsMap.put("getByte",str);
        String val = HttpHelper.httpPost("http://10.120.151.2/Ocrwebapi/MapOcrService.asmx/GetLayOut",paramsMap,null);

        File file = FileHelper.getDirectory(new File("D:\\datafortablebigdata\\extraExcel\\52\\统计公报\\2014年闽侯县国民经济和社会发展统计公报.txt"));
        System.out.println(file.getPath());
        System.out.println(file.getAbsolutePath());

        int number = 2;
        String msg =  number > 1 ? "大于1" : "小于1";
        int result = number > 2 ? 10 : 20;
    }
}
