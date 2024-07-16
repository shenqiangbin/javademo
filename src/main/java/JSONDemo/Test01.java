package JSONDemo;

import MyStr.StringHasHighChar;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class Test01 {
    public static void main(String[] args) throws IOException {
        String content = FileUtils.readFileToString(new File("E:\\预警系统\\问题\\exclude索引超出范围\\示例数据_json_1720664042897.txt"), "UTF-8");
        JSONObject jsonObject = JSON.parseObject(content);

        String leftFullText = jsonObject.getString("text1");

        String leftFullText_2 = StringHasHighChar.convertSurrogatePairsToBMP(leftFullText);
        int length2 = leftFullText_2.length();

        int length = leftFullText.length();
        System.out.println("length:" + length); // 输出：length:221996


        JSONArray jsonArray = jsonObject.getJSONArray("exclude");
        int batch = 1;
        for (int i = 0; i < jsonArray.size(); i += 2) {
            System.out.println("batch:" + batch++);
            Long startIndex = jsonArray.getLong(i);
            Long endIndex = jsonArray.getLong(i + 1);

            String txt = leftFullText.substring(startIndex.intValue(), endIndex.intValue());
            String txt2 = leftFullText_2.substring(startIndex.intValue(), endIndex.intValue());
            System.out.println(txt);
        }

        System.out.println("ok");
    }





}
