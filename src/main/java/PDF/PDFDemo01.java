package PDF;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;

public class PDFDemo01 {

    /**
     * 参考：https://www.cnblogs.com/liaojie970/p/7132475.html
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {

        //createAPdf();
        //setBgAndProperty();
        //setPwd();
        splitPdf();

    }

    static void createAPdf() throws Exception {

        //Step 1—Create a Document. 创建一个文档。Document 代表文档
        Document document = new Document();

        //Step 2—Get a PdfWriter instance. 将文档写入到指定文件。PdfWriter pdf写入器
        PdfWriter.getInstance(document, new FileOutputStream("createSamplePDF.pdf"));

        //Step 3—Open the Document. 打开文档
        document.open();

        //Step 4—Add content. 写入内容。 Paragraph 代表段落
        document.add(new Paragraph("Hello World"));

        //Step 5—Close the Document.
        document.close();
    }

    static void setBgAndProperty() throws Exception {


        //页面大小
        Rectangle rect = new Rectangle(PageSize.A4.rotate());

        //页面背景色
        rect.setBackgroundColor(BaseColor.ORANGE);

        //Step 1—Create a Document. 创建一个文档。Document 代表文档
        Document doc = new Document(rect);

        //Step 2—Get a PdfWriter instance. 将文档写入到指定文件。PdfWriter pdf写入器
        PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream("示例.pdf"));
        //PDF版本(默认1.4)
        writer.setPdfVersion(PdfWriter.PDF_VERSION_1_2);

        //Step 3—Open the Document. 打开文档
        doc.open();

        //文档属性
        doc.addTitle("Title@sample");
        doc.addAuthor("Author@rensanning");
        doc.addSubject("Subject@iText sample");
        doc.addKeywords("Keywords@iText");
        doc.addCreator("Creator@iText");

        //页边空白
        //doc.setMargins(10, 20, 30, 40);

        doc.open();
        doc.add(new Paragraph("Hello World"));

        //Step 5—Close the Document.
        doc.close();
    }

    /**
     * 设置密码
     *
     * @throws Exception
     */
    static void setPwd() throws Exception {

        Document doc = new Document();
        PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream("有密码的示例.pdf"));

        // 设置密码为："World" 则可以编辑， 输入 "Hello" 只可以查看
        writer.setEncryption("Hello".getBytes(), "World".getBytes(),
                PdfWriter.ALLOW_SCREENREADERS,
                PdfWriter.STANDARD_ENCRYPTION_128);

        doc.open();
        doc.add(new Paragraph("Hello World"));

        doc.close();

    }

    /**
     * 分割 pdf，拆分 pdf
     *
     * @throws Exception
     */
    static void splitPdf() throws Exception {

        FileOutputStream out = new FileOutputStream("splitPDF.pdf");

        Document document = new Document();

        PdfWriter.getInstance(document, out);

        document.open();
        document.add(new Paragraph("1 page"));

        document.newPage();
        document.add(new Paragraph("2 page"));

        document.newPage();
        document.add(new Paragraph("3 page"));

        document.newPage();
        document.add(new Paragraph("4 page"));

        document.close();

        /******************************* 上面是先生成一个共 4 页的 PDF，然后下面进行分割 ***********************/

        PdfReader reader = new PdfReader("splitPDF.pdf");

        Document dd = new Document();
        PdfWriter writer = PdfWriter.getInstance(dd, new FileOutputStream("splitPDF1.pdf"));
        dd.open();
        PdfContentByte cb = writer.getDirectContent();
        dd.newPage();
        cb.addTemplate(writer.getImportedPage(reader, 1), 0, 0);
        dd.newPage();
        cb.addTemplate(writer.getImportedPage(reader, 2), 0, 0);
        dd.close();
        writer.close();

        Document dd2 = new Document();
        PdfWriter writer2 = PdfWriter.getInstance(dd2, new FileOutputStream("splitPDF2.pdf"));
        dd2.open();
        PdfContentByte cb2 = writer2.getDirectContent();
        dd2.newPage();
        cb2.addTemplate(writer2.getImportedPage(reader, 3), 0, 0);
        dd2.newPage();
        cb2.addTemplate(writer2.getImportedPage(reader, 4), 0, 0);
        dd2.close();
        writer2.close();

    }


}
