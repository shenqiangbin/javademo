package MyImage;

import com.groupdocs.conversion.Converter;
import com.groupdocs.conversion.filetypes.ImageFileType;
import com.groupdocs.conversion.options.convert.ImageConvertOptions;
import com.groupdocs.conversion.options.convert.ImageFlipModes;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

/**
 * 使用方法：
 * <pre>
 * OutputStream outputStream = new FileOutputStream("d:/th-thumbnail.png");
 * ImgUtil.thumbnailImg(new File("d:/th.jpg"),400,null,outputStream);
 * ImgUtil.thumbnailImg(new URL("http://bigdata.cnki.net/img/1.bed4469b.png"),100,null,outputStream);
 *
 * // 网页输出流使用方法
 * //HttpServletResponse response
 * OutputStream outputStream = response.getOutputStream();
 * ImgUtil.thumbnailImg(new File("d:/th.jpg"),400,null,outputStream);
 * </pre>
 */
public class ImgUtil {

    /**
     * 获取图片的缩略图（解决了背景是黑色的问题）
     * @param file 图片地址
     * @param w 缩略图宽度
     * @param h 缩略图高度（为 null 时，等比缩放）
     * @param output 输出流
     * @throws IOException
     */
    public static void thumbnailImg(File file, Integer w, Integer h, OutputStream output) throws IOException {
        BufferedImage preImage = ImageIO.read(file);
        thumbnail(preImage,w,h,output);
    }

    /**
     *  获取图片的缩略图（解决了背景是黑色的问题）
     * @param input 输入流
     * @param w 缩略图宽度
     * @param h 缩略图高度（为 null 时，等比缩放）
     * @param output 输出流
     * @throws IOException
     */
    public static void thumbnailImg(InputStream input, Integer w, Integer h, OutputStream output) throws IOException {
        BufferedImage preImage = ImageIO.read(input);
        thumbnail(preImage,w,h,output);
    }

    /**
     * 获取图片的缩略图（解决了背景是黑色的问题）
     * @param input 图片网址
     * @param w 缩略图宽度
     * @param h 缩略图高度（为 null 时，等比缩放）
     * @param output 输出流
     * @throws IOException
     */
    public static void thumbnailImg(URL input, Integer w, Integer h, OutputStream output) throws IOException {
        BufferedImage preImage = ImageIO.read(input);
        thumbnail(preImage,w,h,output);
    }

    private static void thumbnail(BufferedImage prevImage, Integer w, Integer h, OutputStream output) throws IOException {
        int width = prevImage.getWidth();
        int height = prevImage.getHeight();

        int newWidth = width;
        int newHeight = height;

        /*缩放*/
        if (w != null) {
            double percent = (w / (double)width);
            newWidth = (int) (width * percent);
            newHeight = (int) (height * percent);

            if (h != null)
                newHeight = h;
        }

        BufferedImage newimage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_BGR);
        Graphics2D graphics = newimage.createGraphics();
        newimage = graphics.getDeviceConfiguration().createCompatibleImage(newWidth,newHeight,Transparency.TRANSLUCENT);
        graphics.dispose();
        graphics = newimage.createGraphics();

        //graphics.setBackground(oldG.getBackground());
        graphics.drawImage(prevImage, 0, 0, newWidth, newHeight, null);

        // 也可以直接输出到文件，根据需要修改代码
        ImageIO.write(newimage, "png", output);
        output.flush();
        output.close();
    }

    public static void convertImgToPng(String imgFile, String destPng){
        Converter converter = new Converter(imgFile);
        ImageConvertOptions convertOptions = new ImageConvertOptions();
        convertOptions.setFormat(ImageFileType.Png);
        convertOptions.setPagesCount(1);
        convertOptions.setHorizontalResolution(50);
        convertOptions.setVerticalResolution(50);
        converter.convert(destPng, convertOptions);

    }

    public static void convertImgToJpg(String imgFile, String destPng){
        Converter converter = new Converter(imgFile);
        ImageConvertOptions convertOptions = new ImageConvertOptions();
        convertOptions.setFormat(ImageFileType.Jpg);
        convertOptions.setPagesCount(1);
        convertOptions.setHorizontalResolution(50);
        convertOptions.setVerticalResolution(50);
        converter.convert(destPng, convertOptions);

    }
}
