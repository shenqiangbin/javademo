package WordDemo;

import com.aspose.words.*;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

// 可以获取 页码 总数
// 能够获取指定页的内容信息
public class Test02 {
    public static void main(String[] args) throws Exception {
        System.out.println("ok");
        test1();
        //test2();
    }

    private static void test1() throws Exception {
        Document doc = new Document("d:/测试文档.docx");
        int pageCount = getWordPageCount(doc);
        pageTest(doc);
        System.out.println("一共有:" + pageCount + "页");
    }

    private static void test2() throws Exception {
        Document doc = new Document("d:/招标结果告知函.docx");
        int pageCount = getWordPageCount(doc);
        pageTest(doc);
        System.out.println("一共有:" + pageCount + "页");
    }

    public static int getWordPageCount(Document doc) throws Exception {
        DocumentBuilder builder = new DocumentBuilder(doc);
        FieldNumPages fieldNumPages = (FieldNumPages) builder.insertField(FieldType.FIELD_NUM_PAGES, true);
        doc.updateFields();
        String returnStr = fieldNumPages.getResult();
        return Integer.parseInt(returnStr);

        // 上面的可能比较准确
        //return doc.getPageCount();
    }

    public static void pageTest(Document doc) throws Exception {
        LayoutCollector layoutCollector = new LayoutCollector(doc);

        //NodeCollection childNodes = doc.getChildNodes();
        NodeCollection childNodes = doc.getChildNodes(NodeType.ANY, true);
        for (int i = 0; i < childNodes.getCount(); i++) {
            Node childNode = childNodes.get(i);
            int numPage = layoutCollector.getStartPageIndex(childNode);

            if(childNode.getNodeType() == NodeType.PARAGRAPH){
                String text = childNode.getText();
                if(StringUtils.isNotBlank(text)) {
                    System.out.println("内容:" + text);
                    System.out.println("页码:" + numPage);
                }
            }
            if(childNode.getNodeType() == NodeType.TABLE){
                System.out.println("======= this is table");
                System.out.println("页码:" + numPage);
            }

        }

        /*NodeCollection runs = doc.getChildNodes(NodeType.PARAGRAPH, true);
        for (int i = 0; i < runs.getCount(); i++) {
            Node r = runs.get(i);
            int numPage = layoutCollector.getStartPageIndex(r);
        }*/

    }
}
