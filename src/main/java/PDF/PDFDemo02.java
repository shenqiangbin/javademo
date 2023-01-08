//package PDF;
//
//import org.apache.pdfbox.pdmodel.PDDocument;
//import org.apache.pdfbox.rendering.PDFRenderer;
//
//import javax.imageio.ImageIO;
//import java.awt.image.BufferedImage;
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.util.Base64;
//
//public class PDFDemo02 {
//
//    public static void main(String[] args) throws IOException {
//        //toImage("splitPDF.pdf", 0.5f);
//        toImage("/Users/adminqian/my/工作居住证/工作居住证/申强宾户口页&本页.pdf", 0.5f);
//    }
//
//    public static void toImage(String file, float scale) throws IOException {
//        PDDocument doc = PDDocument.load(new File(file));
//        PDFRenderer renderer = new PDFRenderer(doc);
//
//        int pageNumber = doc.getNumberOfPages();
//
//        for (int i = 0; i < pageNumber; i++) {
//            BufferedImage image = renderer.renderImage(i, scale);
//            ImageIO.write(image, "png", new FileOutputStream(i + "pic.png"));
//        }
//    }
//
//    public static void toImage(String file, float scale, int startPageNumber, int endPageNumber) throws IOException {
//        PDDocument doc = PDDocument.load(new File(file));
//        PDFRenderer renderer = new PDFRenderer(doc);
//
//        int pageNumber = doc.getNumberOfPages();
//
//        for (int i = 0; i < pageNumber; i++) {
//            BufferedImage image = renderer.renderImage(i, scale);
//            saveToBase64Img(image);
//        }
//    }
//
//    public static String saveToBase64Img(BufferedImage bufferedImage) throws IOException {
//
//        ByteArrayOutputStream output = new ByteArrayOutputStream();
//
//        ImageIO.write(bufferedImage, "png", output);
//        output.flush();
//        output.close();
//
//        byte[] byteArr = output.toByteArray();
//
//        String str = Base64.getEncoder().encodeToString(byteArr);
//        return new StringBuilder().append("data:image/png;base64,").append(str).toString();
//
//    }
//}
