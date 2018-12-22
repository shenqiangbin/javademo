package MyImage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.Random;

public class ImageTest {
    public static void main(String[] args){
        test1();
        //test2();
//        test3();
//        test4();
        //test5();
    }

    //图片上写字
    public static void test1(){
        int imgWidth = 113;
        int imgHeight = 45;

        // 创建一个画布
        BufferedImage bufferedImage = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_RGB);
        // 创建一个画笔，要开始画东西了
        Graphics2D g = bufferedImage.createGraphics();
        // 画布背景色
        g.setBackground(Color.WHITE);
        g.clearRect(0, 0, imgWidth, imgHeight);

        // 画笔颜色
        g.setColor(Color.gray);
        // 画笔字体
        g.setFont(new Font("宋体", Font.PLAIN, imgHeight - 10));
        //输入汉字，必须设置字体，且 系统中必须有对应字体，否则会乱码。
        String content = "小明";
        g.drawString(content,0,imgHeight - 10);

        g.dispose();

        saveToBase64Img(bufferedImage);
//        boolean success = false;
//        try {
//            File file = new File("d:/3.png");
//            success = ImageIO.write(bufferedImage,"png",file);
//            Runtime.getRuntime().exec(new String[]{"cmd.exe", "/c","d:/3.png"});
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        System.out.println("result :" + success);
    }

    //图片保存到磁盘,并打开
    public void save(){
        BufferedImage bufferedImage = new BufferedImage(100, 200, BufferedImage.TYPE_INT_RGB);
        /*
        .........
        */
        boolean success = false;
        try {
            success = ImageIO.write(bufferedImage,"png",new File("d:/3.png"));
            Runtime.getRuntime().exec(new String[]{"cmd.exe", "/c","d:/3.png"});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //图片保存成base64形式
    public static void saveToBase64Img(BufferedImage bufferedImage) {

        ByteArrayOutputStream output = new ByteArrayOutputStream();

        try {
            ImageIO.write(bufferedImage, "png", output);
            output.flush();
            output.close();

            byte[] byteArr = output.toByteArray();

            String str = Base64.getEncoder().encodeToString(byteArr);
            String imgStr = new StringBuilder().append("data:image/png;base64,").append(str).toString();
            System.out.println(imgStr);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //居中图片上的字
    public static void test2(){
        int imgWidth = 113;
        int imgHeight = 45;

        // 创建一个画布
        BufferedImage bufferedImage = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_RGB);
        // 创建一个画笔，要开始画东西了
        Graphics2D g = bufferedImage.createGraphics();
        // 画布背景色
        g.setBackground(Color.WHITE);
        g.clearRect(0, 0, imgWidth, imgHeight);

        // 画笔颜色
        g.setColor(Color.gray);
        // 画笔字体
        int fontHeight = imgHeight-10;
        g.setFont(new Font("宋体", Font.PLAIN, fontHeight));
        //输入汉字，必须设置字体，且 系统中必须有对应字体，否则会乱码。
        String content = "小明";

        FontMetrics metrics = g.getFontMetrics(g.getFont());

        System.out.println("fontHeight:"+String.valueOf(fontHeight));
        System.out.println("ascent:"+metrics.getAscent());
        System.out.println("descent:"+metrics.getDescent());
        System.out.println("height:"+metrics.getHeight());

        int x = (imgWidth -metrics.stringWidth(content))/2;
        int y = ((imgHeight - metrics.getHeight()) / 2) + metrics.getAscent();

        g.drawString(content,x,y);

        g.dispose();

        boolean success = false;
        try {
            success = ImageIO.write(bufferedImage,"png",new File("d:/3.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("result :" + success);
    }

    //设置图片背景色
    public static void test3(){
        int imgWidth = 113;
        int imgHeight = 45;

        // 创建一个画布
        BufferedImage bufferedImage = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_RGB);
        // 创建一个画笔，要开始画东西了
        Graphics2D g = bufferedImage.createGraphics();
        // 画布背景色
        g.setBackground(Color.WHITE);
        g.clearRect(0, 0, imgWidth, imgHeight);

        g.setPaint(new GradientPaint(0, 0, new Color(28, 64, 124), imgWidth, imgHeight, new Color(46, 109, 163)));
        // g.setPaint(Color.BLUE);
        g.fillRect(0, 0, imgWidth, imgHeight);

        g.dispose();

        boolean success = false;
        try {
            File file = new File("d:/3.png");
            success = ImageIO.write(bufferedImage,"png",file);
            Runtime.getRuntime().exec(new String[]{"cmd.exe", "/c","d:/3.png"});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //图片上画线 多边形
    public static void test4(){
        // 创建一个画布
        BufferedImage bufferedImage = new BufferedImage(100, 200, BufferedImage.TYPE_INT_RGB);
        // 创建一个画笔，要开始画东西了
        Graphics2D g = bufferedImage.createGraphics();
        // 画布背景色
        /*g.setBackground(Color.WHITE);
        g.clearRect(0, 0, 100,200);*/

        //线
        g.drawLine(0,0,99,120);

        //多边形
        int[] arr1 = new int[]{10,40,30,40,50};
        int[] arr2 = new int[]{20,40,10,10,60};

        g.drawPolygon(arr1,arr2,arr1.length);

        g.dispose();

        boolean success = false;
        try {
            File file = new File("d:/3.png");
            success = ImageIO.write(bufferedImage,"png",file);
            Runtime.getRuntime().exec(new String[]{"cmd.exe", "/c","d:/3.png"});
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //旋转角度
    public static void test5(){


        // 创建一个画布
        BufferedImage bufferedImage = new BufferedImage(100, 200, BufferedImage.TYPE_INT_RGB);
        // 创建一个画笔，要开始画东西了
        Graphics2D g = bufferedImage.createGraphics();


        int jiaodu = 60; // 角度 [-180,180]度
        g.rotate(jiaodu * Math.PI / 180);
        g.drawString("李卫当官",30,10);


        g.rotate(-60 * Math.PI / 180);


        //平移原点：向右移动10，向下移动20
        g.translate(10,20);
        g.rotate(20 * Math.PI / 180);
        g.drawString("李",0,0);
        g.rotate(-20 * Math.PI / 180);

        //平移原点：向右移动20，上下不移动了
        g.translate(20,0);
        g.rotate(-20 * Math.PI / 180);
        g.drawString("卫",0,0);
        g.rotate(20 * Math.PI / 180);

        g.translate(20,0);
        g.rotate(20 * Math.PI / 180);
        g.drawString("当",0,0);
        g.rotate(-20 * Math.PI / 180);

        g.translate(20,0);
        g.rotate(-20 * Math.PI / 180);
        g.drawString("官",0,0);
        g.rotate(20 * Math.PI / 180);

        g.dispose();

        boolean success = false;
        try {
            File file = new File("d:/3.png");
            success = ImageIO.write(bufferedImage,"png",file);
            Runtime.getRuntime().exec(new String[]{"cmd.exe", "/c","d:/3.png"});
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //随机位置放文字
    public static void addOtherText(Graphics2D g, int imgWidth, int imgHeight, String text) {
        Random randomer = new Random();

        for (int i = 0; i < text.length(); i++) {
            char item = text.charAt(i);

            int pointX = randomer.nextInt(imgWidth);
            int pointY = randomer.nextInt(imgHeight);
            g.setFont(new Font("Arial", Font.PLAIN, imgHeight-18));
            g.drawString(Character.toString(item), pointX, pointY);
        }
    }

    //随机画多边形
    public static void addPolygon(Graphics2D g, int imgWidth, int imgHeight) {
        int pointNum = 10;

        Random randomer = new Random();
        int[] xArr = new int[pointNum];
        int[] yArr = new int[pointNum];

        for (int i = 0; i < pointNum; i++) {
            int pointX = randomer.nextInt(imgWidth);
            int pointY = randomer.nextInt(imgHeight);
            xArr[i] = pointX;
            yArr[i] = pointY;
        }

        g.drawPolygon(xArr, yArr, pointNum);
    }

    //随机画线
    public static void addLine(Graphics2D g, int imgWidth, int imgHeight) {
        int pointNum = 10;
        Random randomer = new Random();

        for (int i = 0; i < pointNum; i++) {
            int pointX1 = randomer.nextInt(imgWidth);
            int pointY1 = randomer.nextInt(imgHeight);
            int pointX2 = randomer.nextInt(imgWidth);
            int pointY2 = randomer.nextInt(imgHeight);
            g.drawLine(pointX1, pointY1, pointX2, pointY2);
        }
    }
}
