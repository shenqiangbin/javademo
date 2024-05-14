package xmltest;

import ExcelDemo.Excel2007Utils;
import cn.hutool.core.util.XmlUtil;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jetty.util.ajax.JSON;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.FileInputStream;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class LegalTest {

    @Test
    public void testCnXml() throws Exception {
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

        List<LegalModel> list = LegalService.getLegalEvents(document, openNumber);

        List<List<Object>> objects = list.stream().map(m -> {
            try {
                return getObjectToValue(m);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());

        List<String> cols = Arrays.asList(new String[]{"日期", "事件编码", "序号", "法律效力", "法律状态", "法律状态信息", "附加信息", "变更前内容", "变更后内容", "分类",
                "无效宣告决定日", "无效宣告决定号", "委内编号", "授权日", "授权公告日", "受让人_被许可人", "让与人_许可人", "许可种类", "审查结论", "申请人", "申请公布日", "其他有关事项", "期满终止日期", "解除日", "合同履行期限", "合同备案号", "公开日", "公告说明", "更正项目", "发明名称", "登记生效日", "登记解除日", "登记号", "出质人", "备案日期", "转移_转让人", "转移_受让人"});
        Excel2007Utils.writeExcel("E:\\temp", "legal_" + openNumber, "sheet1", cols, openNumber, objects, false);
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
