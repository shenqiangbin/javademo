import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sun.jersey.json.impl.JSONHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Test2 {
    public static void main(String[] args) {

        Map<String,Object> resultMap = new HashMap<>();

        String oldJson = "{\n" +
                "    \"sortList\": [\n" +
                "        \"技术\",\n" +
                "        \"浙江大学\",\n" +
                "        \"清华大学\"\n" +
                "    ],\n" +
                "    \"list\": [\n" +
                "        {\n" +
                "            \"技术\": \"机组在线监测故障特征提取和诊断技术\",\n" +
                "            \"浙江大学\": \"10\",\n" +
                "            \"清华大学\": \"0\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"技术\": \"光纤光栅温度传感技术\",\n" +
                "            \"浙江大学\": \"0\",\n" +
                "            \"清华大学\": \"0\"\n" +
                "        }\n" +
                "    ]\n" +
                "}";

        resultMap = JSON.parseObject(oldJson, Map.class);

        System.out.println(resultMap);

        HashMap result = toStandard(resultMap);

        System.out.println(JSON.toJSONString(result));

        System.out.println("over");

        String s = String.format("author like '%%%s%%'", "abc");
        System.out.println(s);
    }

    /**
     * 转换成标准格式
     * @param resultMap
     * @return
     */
    static HashMap toStandard(Map<String,Object> resultMap){
        JSONArray jsonArray = (JSONArray)resultMap.get("sortList");
        Object xAxisData = jsonArray.stream().filter(m -> !m.equals("技术")).collect(Collectors.toList());

        JSONArray jsonArray2 = (JSONArray)resultMap.get("list");
        Object yAxisData = jsonArray2.stream().map(m -> ((JSONObject)m).get("技术")).collect(Collectors.toList());


        List<Object> dataResultList  = new ArrayList<>();
        for(Object item : (ArrayList)yAxisData){
            List<Object[]> dataList = new ArrayList<>();
            Object jsonObject = jsonArray2.stream().filter(m -> ((JSONObject) m).get("技术").equals(item)).findFirst().get();
            for(Object xItem : (ArrayList)xAxisData){
                Object val = ((JSONObject)jsonObject).get(xItem);
                Object[] obj = new Object[]{xItem, item, val};
                dataList.add(obj);
            }
            dataResultList.add(new HashMap<String, Object>(){
                {
                    put("data", dataList);
                    put("name", item);
                }
            });
        }

        HashMap result = new HashMap(){
            {
                put("xAxisData", xAxisData);
                put("yAxisData", yAxisData);
                put("list", dataResultList);
            }
        };
        return result;
    }
}
