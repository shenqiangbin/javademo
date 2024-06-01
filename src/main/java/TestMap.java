import MyDate.DateUtil;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class TestMap {
    public static Map<String,String> doingTask = new HashMap<>();

    static void addInfo(String key, String msg){
        if(!doingTask.containsKey(key)){
            doingTask.put(key,msg);
        }else{
            msg = msg + doingTask.get(key) + ";";
            doingTask.put(key, msg);
        }
    }

    public static void main(String[] args) {
        HashMap<String, Integer> map = new HashMap<>();
        map.put("apple", 10);
        map.put("banana", 20);
        map.put("orange", 30);

        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
        }

        List<Long> doingTaskId = new ArrayList<>();
        doingTaskId.remove(1.0);

        System.out.println("ok");


        // 假设你的字符串日期时间格式为 "yyyy-MM-dd HH:mm:ss"
        String dateTimeStr = "2024-04-09 16:23:24";

        // 定义一个DateTimeFormatter来解析字符串
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // 使用DateTimeFormatter解析字符串为LocalDateTime对象
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeStr, formatter);

        long allStartTime = dateTime.toInstant(ZoneOffset.of("+8")).toEpochMilli();

        // 结束时间
        long contrastEndTime = System.currentTimeMillis();
        String format = DateUtil.format(new Date());
        System.out.println(format);

        long l = contrastEndTime - allStartTime;
        System.out.println("毫秒数：" + l);


    }
}
