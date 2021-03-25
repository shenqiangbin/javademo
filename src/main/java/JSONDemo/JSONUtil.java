package JSONDemo;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class JSONUtil {

    public static String toString(Object obj) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(obj);
    }

    public static <T> T toObject(String content, Class<T> valueType) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(content, valueType);
    }

    public static String filterJson(String enumStr){
        enumStr = enumStr.replace("“","\"");
        enumStr = enumStr.replace("：",":");
        enumStr = enumStr.replace("<br/>","");
        enumStr = enumStr.replace("\\\"","\"");
        enumStr = enumStr.replace("\n","");
        enumStr = enumStr.replace("\",,\"","\",\"");
        return enumStr;
    }

    public static boolean isJson(String content) {
        JSONObject jo = null;
        try {
            jo = JSONObject.parseObject(content);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
