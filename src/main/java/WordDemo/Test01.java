package WordDemo;

/*import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Range;*/
import org.apache.poi.xwpf.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * <dependency>
 *     <groupId>org.apache.poi</groupId>
 *     <artifactId>poi</artifactId>
 *     <version>5.2.3</version>
 * </dependency>
 * <dependency>
 *     <groupId>org.apache.poi</groupId>
 *     <artifactId>poi-ooxml</artifactId>
 *     <version>5.2.3</version>
 * </dependency>
 */
public class Test01 {
    public static void main(String[] args) throws IOException {
        File file = new File("d:/测试文档.docx");
        FileInputStream fis = new FileInputStream(file);
        XWPFDocument document = new XWPFDocument(fis);

        XWPFNumbering numbering = document.getNumbering();

        int pageCount = document.getProperties().getExtendedProperties().getUnderlyingProperties().getPages();



        for (XWPFParagraph paragraph : document.getParagraphs()) {

            for (XWPFRun run : paragraph.getRuns()) {
               //paragraph.getNumID()
                //int pageNum = document.getOverallPageNumber(paragraph.getCharacterRun(0).getStartOffset());
                System.out.println(run.getText(0));
            }
        }

        for (IBodyElement element : document.getBodyElements()) {
            if (element instanceof XWPFParagraph) {
                XWPFParagraph paragraph = (XWPFParagraph) element;
                // 获取段落文本内容
                String text = paragraph.getText();
                System.out.println(text);
            } else if (element instanceof XWPFTable) {
                XWPFTable table = (XWPFTable) element;
                // 遍历表格中的每一行每一列
                for (XWPFTableRow row : table.getRows()) {
                    for (XWPFTableCell cell : row.getTableCells()) {
                        // 获取单元格文本内容
                        String text = cell.getText();
                        System.out.println(text);
                    }
                }
            }
        }

        document.close();
        fis.close();

        // doc 处理 https://blog.51cto.com/u_16213322/8714801

        /*FileInputStream fis = new FileInputStream("example.doc");
        HWPFDocument doc = new HWPFDocument(fis);

        Range range = doc.getRange();
        Paragraph[] paragraphs = range.getParagraphs();

        for (int i = 0; i < paragraphs.length; i++) {
            Paragraph paragraph = paragraphs[i];
            if (paragraph.isInTable()) {
                // 处理表格中的文本内容
            } else if (paragraph.isInHeaderFooter()) {
                // 处理页眉/页脚中的文本内容
            } else {
                doc.get
                // 处理普通段落中的文本内容
                int pageNum = doc.getOverallPageNumber(paragraph.getCharacterRun(0).getStartOffset());
                String text = paragraph.text();
                System.out.println("第" + pageNum + "页：\n" + text);
            }
        }*/
    }
}
