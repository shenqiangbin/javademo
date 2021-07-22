package PDF;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.apache.commons.io.FilenameUtils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PDFDemo01 {

    /**
     * 参考：https://www.cnblogs.com/liaojie970/p/7132475.html
     * <p>
     * 相关库还有 Flying sauser
     * <p>
     * pdf 旋转
     * pdf 切割 + 旋转 同时进行
     * pdf 切割 + 旋转 + 输出到压缩包 同时进行 ？？ pdfReader 也可以直接读取流
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {

        //createAPdf();
        //setBgAndProperty();
        //setPwd();
        //splitPdf();
        //splitPdf("/Users/adminqian/git/javademo/splitPDF.pdf", 1);
        //splitPdf("/Users/adminqian/git/javademo/splitPDF.pdf", 2);
        //splitPdf("/Users/adminqian/git/javademo/splitPDF.pdf", 3);
        //splitPdf("/Users/adminqian/git/javademo/扫描件.pdf", 3);
        List<RotateConfig> list = new ArrayList<>();
        list.add(new RotateConfig(1, 90));
        list.add(new RotateConfig(3, 90));
        splitPdf("/Users/adminqian/git/javademo/扫描件.pdf", "1,2-9", list);
        //rotate("/Users/adminqian/git/javademo/扫描件.pdf", list);

        System.out.println("over");
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

    /**
     * 按自定义规则拆分 pdf
     *
     * @param pdf
     * @param customRule 1-2,5,6-9 支持中文的逗号
     */
    static void splitPdf(String pdf, String customRule, List<RotateConfig> rotateConfigs) throws IOException, DocumentException {
        PdfReader reader = new PdfReader(pdf);
        // 获取总页数
        int totalNumber = reader.getNumberOfPages();

        rotate(reader, rotateConfigs);

        customRule = customRule.replace("，", ",");

        String[] pageRanges = customRule.split(",");
        for (int i = 1; i <= pageRanges.length; i++) {
            List<Integer> pageNumbers = getPageNumbers(pageRanges[i - 1], totalNumber);
            writeOnePdf(pdf, i, reader, pageNumbers);
        }
    }

    private static List<Integer> getPageNumbers(String pageRange, int totalNumber) {
        List<Integer> pageNumbers = new ArrayList<>();

        String[] pageRangeArr = pageRange.split("-");
        if (pageRangeArr.length == 2) {
            int min = Integer.parseInt(pageRangeArr[0]);
            int max = Integer.parseInt(pageRangeArr[1]);
            for (int i = min; i <= max; i++) {
                if (i <= totalNumber)
                    pageNumbers.add(i);
            }
        } else {
            int pageNum = Integer.parseInt(pageRange);
            if (pageNum <= totalNumber)
                pageNumbers.add(pageNum);
        }

        return pageNumbers;
    }

    /**
     * 按每n页拆分pdf文件
     *
     * @param pdf
     * @param pageCountPerPdf
     * @throws IOException
     * @throws DocumentException
     */
    static void splitPdf(String pdf, int pageCountPerPdf, List<RotateConfig> rotateConfigs) throws IOException, DocumentException {
        PdfReader reader = new PdfReader(pdf);
        // 获取总页数
        int totalNumber = reader.getNumberOfPages();

        rotate(reader, rotateConfigs);

        // 要输出几个 pdf 文件
        int loop = totalNumber % pageCountPerPdf == 0 ? totalNumber / pageCountPerPdf : (totalNumber / pageCountPerPdf) + 1;

        int pageNumber = 1;

        List<Integer> pageNumbers;
        for (int i = 1; i <= loop; i++) {
            pageNumbers = new ArrayList<>();
            while (pageNumber <= pageCountPerPdf * i && pageNumber <= totalNumber) {
                pageNumbers.add(pageNumber);
                pageNumber++;
            }
            writeOnePdf(pdf, i, reader, pageNumbers);
        }

    }

    /**
     * @param origiFile   原始文件路径
     * @param pdfIndex    要生成的第几个pdf
     * @param reader      原始pdf的reader
     * @param pageNumbers 原始pdf的页码集合
     * @throws FileNotFoundException
     * @throws DocumentException
     */
    static void writeOnePdf(String origiFile, int pdfIndex, PdfReader reader, List<Integer> pageNumbers) throws IOException, DocumentException {

        String newFile = getNewFileName(origiFile, String.valueOf(pdfIndex));

        Document doc = new Document();

        //PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(newFile));
        PdfCopy p = new PdfSmartCopy(doc, new FileOutputStream(newFile)); // 生成的目标PDF文件

        doc.open();

        //PdfContentByte cb = writer.getDirectContent();

        for (int pageNumber : pageNumbers) {
            //doc.newPage();
            //PdfImportedPage pdfImportedPage = writer.getImportedPage(reader, pageNumber);
            //cb.addTemplate(pdfImportedPage, 0, 0);
            p.addPage(p.getImportedPage(reader, pageNumber));

        }

        doc.close();
    }

    static String getNewFileName(String origiFile, String other) {
        String path = FilenameUtils.getFullPath(origiFile);
        String filenameWithoutExt = FilenameUtils.getBaseName(origiFile);
        String extension = FilenameUtils.getExtension(origiFile);
        String newFileName = filenameWithoutExt + "_" + other + "." + extension;

        return FilenameUtils.concat(path, newFileName);
    }


    static void rotate(PdfReader reader, List<RotateConfig> rotateConfigs) {
        if (rotateConfigs == null || rotateConfigs.size() == 0)
            return;

        for (RotateConfig config : rotateConfigs) {
            PdfDictionary dictionary = reader.getPageN(config.getPage());
            dictionary.put(PdfName.ROTATE, new PdfNumber(config.getRotate()));
        }
    }

    static void rotate(String pdf, List<RotateConfig> rotateConfigs) throws IOException, DocumentException {
        PdfReader reader = new PdfReader(pdf);
        // 获取总页数
        int totalNumber = reader.getNumberOfPages();
        rotate(reader, rotateConfigs);

        Document doc = new Document();
        String newFile = getNewFileName(pdf, "旋转");
        PdfCopy p = new PdfSmartCopy(doc, new FileOutputStream(newFile)); // 生成的目标PDF文件
        doc.open();

        for (int i = 1; i <= totalNumber; i++) {
            p.addPage(p.getImportedPage(reader, i));
        }

        doc.close();

    }

    // pdf 旋转
    // pdf 压缩
}
