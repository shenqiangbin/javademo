package PDF;

import MyDate.DateUtil;
import MyImage.ImgUtil;
import com.itextpdf.text.*;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.codec.TIFFDirectory;
import com.itextpdf.text.pdf.codec.TIFFField;
import com.itextpdf.text.pdf.parser.ImageRenderInfo;
import com.itextpdf.text.pdf.parser.PdfImageObject;
import com.twelvemonkeys.imageio.plugins.tiff.TIFFImageMetadata;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.io.FilenameUtils;
import org.apache.lucene.analysis.CharArrayMap;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import javax.imageio.*;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStreamImpl;
import javax.imageio.stream.MemoryCacheImageOutputStream;
import java.awt.*;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class PDFDemo01 {

    /**
     * 参考：https://www.cnblogs.com/liaojie970/p/7132475.html
     * <p>
     * 相关库还有 Flying sauser， Free Spire.PDF https://www.cnblogs.com/Yesi/p/11206330.html
     * <p>
     * pdf 旋转
     * pdf 切割 + 旋转 同时进行
     * pdf 切割 + 旋转 + 压缩(图片压缩) 同时进行
     * pdf 切割 + 旋转 + 输出到压缩包 同时进行 ？？ pdfReader 也可以直接读取流
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {

        String[] arr = ImageIO.getWriterFileSuffixes();

        //createAPdf();
        //setBgAndProperty();
        //setPwd();
        //splitPdf();
        //splitPdf("/Users/adminqian/git/javademo/splitPDF.pdf", 1);
        //splitPdf("/Users/adminqian/git/javademo/splitPDF.pdf", 2);
        //splitPdf("/Users/adminqian/git/javademo/splitPDF.pdf", 3);
        //splitPdf("/Users/adminqian/git/javademo/扫描件.pdf", 3);
        List<RotateConfig> list = new ArrayList<>();
        list.add(new RotateConfig(1, -90));
        list.add(new RotateConfig(2, -90));
        //splitPdf("/Users/adminqian/git/javademo/扫描件.pdf", "1,2-9", list);
        //rotate("/Users/adminqian/git/javademo/扫描件.pdf", list);


        String file = "/Users/adminqian/my/工作居住证/工作居住证/申强宾户口页&本页.pdf";
        // 这个文件反而变大了
        file = "/Users/adminqian/Desktop/tmp/Hive编程指南.pdf";
        //file = "/Users/adminqian/Desktop/tmp/Hive编程指南_compressed.pdf";

        Date from = new Date();
        compress(file);
        System.out.println(DateUtil.diff(from, new Date(), DateUtil.Type.SECOND));

        from = new Date();
        //compress2(file);
        //test(file);
        System.out.println(DateUtil.diff(from, new Date(), DateUtil.Type.SECOND));

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

//        Collection<File> filesToArchive = ...
//        try (ArchiveOutputStream o = ... create the stream for your format ...) {
//            for (File f : filesToArchive) {
//                // maybe skip directories for formats like AR that don't store directories
//                ArchiveEntry entry = o.createArchiveEntry(f, entryName(f));
//                // potentially add more flags to entry
//                o.putArchiveEntry(entry);
//                if (f.isFile()) {
//                    try (InputStream i = Files.newInputStream(f.toPath())) {
//                        IOUtils.copy(i, o);
//                    }
//                }
//                o.closeArchiveEntry();
//            }
//            out.finish();
//        }

//        ZipOutputStream zip = new ZipOutputStream(new FileOutputStream(FILE_DIR + "zipPDF.zip"));
//        for (int i = 1; i <= 3; i++) {
//            ZipEntry entry = new ZipEntry("hello_" + i + ".pdf");
//            zip.putNextEntry(entry);
//            Document document = new Document();
//            PdfWriter writer = PdfWriter.getInstance(document, zip);
//            writer.setCloseStream(false);
//            document.open();
//            document.add(new Paragraph("Hello " + i));
//            document.close();
//            zip.closeEntry();
//        }
//        zip.close();
    }

    static String getNewFileName(String origiFile, String other) {
        String path = FilenameUtils.getFullPath(origiFile);
        String filenameWithoutExt = FilenameUtils.getBaseName(origiFile);
        String extension = FilenameUtils.getExtension(origiFile);
        String newFileName = filenameWithoutExt + "_" + other + "." + extension;

        return FilenameUtils.concat(path, newFileName);
    }


    // pdf 旋转

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

        rotate(reader, rotateConfigs);

        String newFile = getNewFileName(pdf, "旋转12");
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(newFile));
        stamper.close();


        // 获取总页数
//        int totalNumber = reader.getNumberOfPages();
//        //rotate(reader, rotateConfigs);
//
//        Document doc = new Document();
//        String newFile = getNewFileName(pdf, "旋转");
//        PdfCopy p = new PdfSmartCopy(doc, new FileOutputStream(newFile)); // 生成的目标PDF文件
//        doc.open();
//
//        for (int i = 1; i <= totalNumber; i++) {
//            p.addPage(p.getImportedPage(reader, i));
//        }
//
//        doc.close();

    }


    // pdf 压缩

    static void compress(String pdf) throws IOException, DocumentException {
        PdfReader reader = new PdfReader(pdf);

        compress(reader);

        String newFile = getNewFileName(pdf, "压缩5");
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(newFile));
        stamper.close();

    }

    static void compress(PdfReader reader) throws IOException {

        int n = reader.getXrefSize();
        for (int i = 0; i < n; i++) {

            PdfObject object = reader.getPdfObject(i);

            if (object == null || !object.isStream())
                continue;

            PRStream stream = (PRStream) object;

//            Set<PdfName> pdfNameSet = stream.getKeys();
//            for (PdfName pdfName : pdfNameSet) {
//                System.out.printf(pdfName.toString());
//                PdfObject obj = stream.get(pdfName);
//            }

            try {
                PdfImageObject image = new PdfImageObject(stream);

                String str = "图片类型：" + image.getFileType();
                String widthstr = "宽：" + image.getBufferedImage().getWidth();
                String heightstr = "高：" + image.getBufferedImage().getHeight();
                for (PdfName key : image.getDictionary().getKeys()) {
                    PdfObject pdfObject = image.getDictionary().get(key);
                    String s = pdfObject.toString();
                    System.out.println(key + ": " + s);
                }

                BufferedImage bi = image.getBufferedImage();
                byte[] bytes = image.getImageAsBytes();
                //ImageIO.write(image.getBufferedImage(), "png", new FileOutputStream(i + "my.png"));

                try {

                    double scale = 0.25f;
                    double quality = 1f;

//                BufferedImage thumbnail = Thumbnails.of(bi)
//                        .imageType(bi.getType())
//                        .scale(scale)
//                        .outputQuality(quality)
//                        // .outputFormat(imageFormat)
//                        .useOriginalFormat()
//                        .asBufferedImage();
                    BufferedImage thumbnail = bi;

                    //BufferedImage resultImage = compressor.compress(bi, pdfImageObject.getFileType(), quality, scale);

                    replaceImage(stream, thumbnail, bytes);

                } catch (Exception e) {
                    e.printStackTrace();
                    break;
                }

            } catch (Exception e) {
                // 报错，则不是图片
            }

        }

        //stamper.close();
        //reader.close();
    }

    static void replaceImage(PRStream stream, BufferedImage resultImage, byte[] bytes) throws IOException {

        ByteArrayOutputStream imgBytes = new ByteArrayOutputStream();
        //ImageIO.write(resultImage, "JPEG", imgBytes);
        //writeImage(resultImage, imgBytes);
        //Thumbnails.of(resultImage).toOutputStream(imgBytes);

        imgBytes = getStream(resultImage);

        PdfObject typeObj = stream.get(PdfName.TYPE);
        PdfObject subTypeObj = stream.get(PdfName.SUBTYPE);
        PdfObject filterObj = stream.get(PdfName.FILTER);
        PdfObject bitObj = stream.get(PdfName.BITSPERCOMPONENT);
        PdfObject decodeObj = stream.get(PdfName.DECODEPARMS);
        PdfObject colorObj = stream.get(PdfName.COLORSPACE);


        stream.clear();

        stream.setData(imgBytes.toByteArray(), false, PRStream.NO_COMPRESSION);
        //stream.setData(imgBytes, false, PRStream.NO_COMPRESSION);

        //stream.setData(bytes, false, PRStream.NO_COMPRESSION);
        stream.put(PdfName.TYPE, typeObj);
        stream.put(PdfName.SUBTYPE, subTypeObj);
        stream.put(PdfName.FILTER, filterObj);
        stream.put(PdfName.WIDTH, new PdfNumber(resultImage.getWidth() / 4));
        stream.put(PdfName.HEIGHT, new PdfNumber(resultImage.getHeight() / 4));
        stream.put(PdfName.BITSPERCOMPONENT, bitObj);
        stream.put(PdfName.DECODEPARMS, decodeObj);
        stream.put(PdfName.COLORSPACE, colorObj);
    }

    static ByteArrayOutputStream getStream(BufferedImage resultImage) throws IOException {
        ByteArrayOutputStream imgBytes = new ByteArrayOutputStream();
        //ImageIO.write(resultImage, "JPEG", imgBytes);
        MemoryCacheImageOutputStream memoryCacheImageOutputStream = new MemoryCacheImageOutputStream(imgBytes);
        //writeImage(resultImage, memoryCacheImageOutputStream);
//        Thumbnails.of(resultImage).outputQuality(1f)
//                .width(resultImage.getWidth() / 4)
//                .height(resultImage.getHeight() / 4)
//                .outputFormat("tiff").toOutputStream(imgBytes);
        //TiffOutput.TiffOutput(resultImage, imgBytes, 300);
        imgBytes.flush();
        System.out.println("here");
        return imgBytes;
    }

    static void writeImage(BufferedImage fileBufferd, MemoryCacheImageOutputStream stream) throws IOException {
        //这么写是为了防止使用ImageIO.write后失真
        Iterator<ImageWriter> iter = ImageIO.getImageWritersByFormatName("jpg");
        if (iter.hasNext()) {
            ImageWriter writer = iter.next();
            ImageWriteParam param = writer.getDefaultWriteParam();

            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            //param.setCompressionQuality(0.92f);
            param.setCompressionQuality(0.2f);
            writer.setOutput(stream);
            // writer.write(bi);
            writer.write(null, new IIOImage(fileBufferd, null, null), param);
            stream.close();
            writer.dispose();
        }
    }

    static void compress2(String file) throws IOException {

        PDDocument document = PDDocument.load(new File(file));

        PDPageTree pages = document.getDocumentCatalog().getPages();
        Iterator<PDPage> iter = pages.iterator();
        int count = 0;
        while (iter.hasNext()) {
            PDPage page = iter.next();
            PDResources resources = page.getResources();
            for (COSName c : resources.getXObjectNames()) {
                PDXObject o = resources.getXObject(c);
                // https://github.com/mkl-public/testarea-itext5/blob/master/src/test/java/mkl/testarea/itext5/extract/ImageExtraction.java
                if (o instanceof PDImageXObject) {
                    PDImageXObject image = (PDImageXObject) o;
                    //File file = new File(imageDir, pageIndex + "-" + System.nanoTime() + "." + img.getSuffix());
                    //ImageIO.write(((PDImageXObject) o).getImage(), image.getSuffix(), new FileOutputStream(count++ + ".tiff"));

                    //BufferedImage thumbnail = compressImg(image.getImage());
                    //BufferedImage thumbnail = image.getImage();

                    //BufferedImage resultImage = compressor.compress(bi, pdfImageObject.getFileType(), quality, scale);
                    ByteArrayOutputStream imgBytes = new ByteArrayOutputStream();
                    //ImageIO.write(thumbnail, "JPG", imgBytes);

                    //TiffOutput.TiffOutput(image.getImage(), imgBytes, 300);
                    TiffOutput.TiffOutput(image.getImage(), imgBytes, 300);

                    //image.getN
                    PDImageXObject newObj = PDImageXObject.createFromByteArray(document, imgBytes.toByteArray(), "1");
                    resources.put(c, newObj);
                }
            }
        }

        String newFile = getNewFileName(file, "压缩2");
        document.save(new FileOutputStream(newFile));
    }

    static BufferedImage compressImg(BufferedImage bi) throws IOException {
        //ImageIO.write(image.getBufferedImage(), "png", new FileOutputStream(i + "my.png"));

        double scale = 0.25f;
        double quality = 1f;

        BufferedImage thumbnail = Thumbnails.of(bi)
                .imageType(bi.getType())
                .scale(scale)
                .outputQuality(quality)
                // .outputFormat(imageFormat)
                .useOriginalFormat()
                .asBufferedImage();
        return thumbnail;
    }


    static void test(String file) throws IOException {

        PDDocument document = PDDocument.load(new File(file));

        PDPageTree pages = document.getDocumentCatalog().getPages();
        Iterator<PDPage> iter = pages.iterator();
        int count = 0;
        while (iter.hasNext()) {
            PDPage page = iter.next();
            PDResources resources = page.getResources();
            for (COSName c : resources.getXObjectNames()) {
                PDXObject o = resources.getXObject(c);
                // https://github.com/mkl-public/testarea-itext5/blob/master/src/test/java/mkl/testarea/itext5/extract/ImageExtraction.java
                if (o instanceof PDImageXObject) {
                    PDImageXObject image = (PDImageXObject) o;

                    //File file = new File(imageDir, pageIndex + "-" + System.nanoTime() + "." + img.getSuffix());
                    FileOutputStream stream = new FileOutputStream(new File("/Users/adminqian/Desktop/tmp/compress2/" + count++ + "-min2." + image.getSuffix()));
                    //ImageIO.write(((PDImageXObject) o).getImage(), image.getSuffix(), stream);
                    //ImageIO.write(((PDImageXObject) o).getImage(), "tif", stream);
                    //Thumbnails.of(image.getImage()).scale(1).outputQuality(1f).outputFormat(image.getSuffix()).toOutputStream(stream);

                    FileImageOutputStream stream2 = new FileImageOutputStream(new File("/Users/adminqian/Desktop/tmp/compress/" + count++ + "-min2." + image.getSuffix()));
                    //toImg(image.getSuffix(), stream2, image.getImage());
                    TiffOutput.TiffOutput(image.getImage(), stream, 300);

                    //stream.flush();
                    //stream.close();

                }
            }
            System.out.println("ok");
        }

    }

    // 这个可以将 tif 的图片保存到本地，并可以打开正常查看。
    static void toImg(String format, ImageOutputStreamImpl stream, RenderedImage fileBufferd) throws IOException {
        //这么写是为了防止使用ImageIO.write后失真


        Iterator<ImageWriter> iter = ImageIO.getImageWritersByFormatName(format);
        if (iter.hasNext()) {
            ImageWriter writer = iter.next();
            ImageWriteParam param = writer.getDefaultWriteParam();

            //param.setTilingMode();
            //param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            //param.setCompressionType("CCITT T.6");
            //param.setCompressionType("JPEGJPEG");
            //param.setCompressionType("LZW");

            //ImageTypeSpecifier imageType = ImageTypeSpecifier.createFromRenderedImage(fileBufferd);
            //TIFFImageMetadata imageMetadata = (TIFFImageMetadata) writer.getDefaultImageMetadata(imageType, param);
            //imageMetadata = createImageMetadata(imageMetadata, fileBufferd.getHeight(), fileBufferd.getWidth(), dpi, compression, fileBufferd.getType());

            //param.setCompressionQuality(0.2f);
//            param.setCompressionQuality(1f);
            writer.setOutput(stream);
            // writer.write(bi);
            writer.write(null, new IIOImage(fileBufferd, null, null), param);
            stream.close();
            writer.dispose();
        }
    }


}
