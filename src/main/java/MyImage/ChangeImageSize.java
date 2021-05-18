package MyImage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;

public class ChangeImageSize {
    public static void main(String[] args) {
        try {

            OutputStream outputStream = new FileOutputStream("/Users/adminqian/Desktop/th-thumbnail.png");
            ImgUtil.thumbnailImg(new URL("https://wx2.sinaimg.cn/large/8e2ef8f7gy1goyti1bzy2j218k0tmaei.jpg"),600,null,outputStream);

            //resizeImage(300);
//            OutputStream outputStream = new FileOutputStream("d:/th-thumbnail.png");
//            ImgUtil.thumbnailImg(new File("d:/th.jpg"),400,null,outputStream);
//            ImgUtil.thumbnailImg(new URL("http://bigdata.cnki.net/img/1.bed4469b.png"),100,null,outputStream);

        } catch (IOException e) {
            e.printStackTrace();
        }
        //test2();
//        test3();
//        test4();
        //test5();
    }




    //暂不支持gif
    public static void resizeImage(int wishWidth) throws IOException, IOException {
        String file = "C:\\personMgr\\upload\\abc-287316d66a6a4f38a67351753f47dee3.jpg";
        BufferedImage prevImage = ImageIO.read(new File(file));

       //Graphics2D oldg = prevImage.getGraphics();

        double width = prevImage.getWidth();
        double height = prevImage.getHeight();
        double percent = wishWidth / width;
        int newWidth = (int) (width * percent);
        int newHeight = (int) (height * percent);
        BufferedImage image = new BufferedImage(newWidth, newHeight, prevImage.getType());
        Graphics2D graphics = image.createGraphics();
        graphics.drawImage(prevImage, 0, 0, newWidth, newHeight, null);

        addMark(graphics,newHeight);

        save(image);
    }

    public static void save(BufferedImage bufferedImage){

        boolean success = false;
        try {
            success = ImageIO.write(bufferedImage,"jpg",new File("d:/3.jpg"));
            Runtime.getRuntime().exec(new String[]{"cmd.exe", "/c","d:/3.jpg"});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void addMark(Graphics2D g,int imgHeight){
        imgHeight = 30;
        g.setColor(Color.gray);
        // 画笔字体
        g.setFont(new Font("宋体", Font.PLAIN, imgHeight - 10));
        //输入汉字，必须设置字体，且 系统中必须有对应字体，否则会乱码。
        String content = "www.sqber.com";
        g.drawString(content,0,imgHeight - 10);
    }
}
