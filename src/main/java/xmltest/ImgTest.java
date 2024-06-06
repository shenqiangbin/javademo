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

public class ImgTest {

    @Test
    public void testCnXml() throws Exception {
        //String file = "C:\\Users\\cnki52\\Desktop\\CN1297393A.xml";
        String file = "C:\\Users\\cnki52\\Desktop\\CN1303364A-附图示例.xml";
        String openNumber = "CN1297393A";

        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = builder.parse(new FileInputStream(file));

        List<LegalPicModel> list = ImgService.getImgs(document, openNumber);
        list.stream().forEach(m -> m.setVal("a", "b", "c"));


        List<List<Object>> objects = list.stream().map(m -> {
            try {
                return getObjectToValue(m);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());

        List<String> cols = Arrays.asList(new String[]{
                "id", "公开号", "专利种类", "专利类型",
                "专利名称",
                "图片名称", "图片类型", "alt", "格式"
        });
        Excel2007Utils.writeExcel("E:\\temp", "img_" + openNumber, "sheet1", cols, openNumber, objects, false);
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


    @Test
    public void testLegalOrigiServiceXml() throws Exception {
//        String file = "C:\\Users\\cnki52\\Desktop\\CN117322164A.xml";
//        String openNumber = "CN117322164A";

//        String file = "C:\\Users\\cnki52\\Desktop\\CN106067413B8.xml";
//        String openNumber = "CN106067413B8";

//        String file = "C:\\Users\\cnki52\\Desktop\\CN1051443C.xml";
//        String openNumber = "CN1051443C";

        String file = "C:\\Users\\cnki52\\Desktop\\CN1274241C.xml";
        String openNumber = "CN1274241C";

        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = builder.parse(new FileInputStream(file));

        List<LegalOrigiModel> list = LegalOrigiService.getLegalEvents(document, openNumber);

        List<List<Object>> objects = list.stream().map(m -> {
            try {
                return getObjectToValue(m);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());

        List<String> cols = Arrays.asList(new String[]{
                "id", "日期", "事件编码", "序号", "法律描述", "类别code", "类别", "状态标识符", "公开编码", "申请编号", "自由文本", "请求者", "生效日期", "新拥有者"
        });
        Excel2007Utils.writeExcel("E:\\temp", "legal_origi_" + openNumber, "sheet1", cols, openNumber, objects, false);

        Assert.assertEquals("a", "a");
    }


}
