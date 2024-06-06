package xmltest;

import ExcelDemo.Excel2007Utils;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.FileInputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CpcTest {

    @Test
    public void testCnXml() throws Exception {
        //String file = "C:\\Users\\cnki52\\Desktop\\CN1297393A.xml";
        String file = "E:\\aaa专利\\US9233085B1 - CPC 组合码附表.xml";
        String openNumber = "US9233085B1";

        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = builder.parse(new FileInputStream(file));

        List<CpcModel> list = CpcService.getCombinationSet(document, openNumber, openNumber);

        List<List<Object>> objects = list.stream().map(m -> {
            try {
                return getObjectToValue(m);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());

        List<String> cols = Arrays.asList(new String[]{
                "文档编号", "公开号", "国家地区代码", "CPC组合码"
        });
        Excel2007Utils.writeExcel("E:\\temp", "cpc_" + openNumber, "sheet1", cols, openNumber, objects, false);
        //Excel2007Utils.writeExcelData("E:\\temp", "legal", "sheet1", objects);

        Assert.assertEquals("a", "a");
    }

    public static List<Object> getObjectToValue(Object obj) throws IllegalAccessException {
        List<Object> result = new ArrayList<>();
        Class<?> clazz = obj.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            String fieldName = field.getName();
            Object value = field.get(obj);
            if (value == null) {
                value = "";
            }
            result.add(value);
        }
        return result;
    }

}
