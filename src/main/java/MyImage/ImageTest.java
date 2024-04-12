package MyImage;

import ThreadDemo.PageInfoResult;
import cn.hutool.core.date.DateTime;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.groupdocs.conversion.Converter;
import com.groupdocs.conversion.ConverterSettings;
import com.groupdocs.conversion.caching.FileCache;
import com.groupdocs.conversion.filetypes.ImageFileType;
import com.groupdocs.conversion.options.convert.ImageConvertOptions;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class ImageTest {
    public static void main(String[] args) throws IOException {
        //test1();
        //test2();
//        test3();
//        test4();
        //test5();
        //test6();



        //AnayImage();
        //testjp2ToPng();
        testHandleJp2ToPng();
        System.out.println("ok");
    }

    // jp2 图片转 png 图片
    private static void testjp2ToPng() {
        ImgUtil.convertImgToPng("E:\\预警系统\\全文抽取测试\\d5196bcd340d4f6897ec7dd300e2b677\\第285页--1241769773-img652.jp2",
                "E:\\预警系统\\全文抽取测试\\d5196bcd340d4f6897ec7dd300e2b677\\第285页--1241769773-img652-imgUtilTest.png");

        ImgUtil.convertImgToJpg("E:\\预警系统\\全文抽取测试\\d5196bcd340d4f6897ec7dd300e2b677\\第285页--1241769773-img652.jp2",
                "E:\\预警系统\\全文抽取测试\\d5196bcd340d4f6897ec7dd300e2b677\\第285页--1241769773-img652-imgUtilTest.jpg");
    }

    //图片上写字
    public static void test1() {
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
        g.drawString(content, 0, imgHeight - 10);

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
    public void save() {
        BufferedImage bufferedImage = new BufferedImage(100, 200, BufferedImage.TYPE_INT_RGB);
        /*
        .........
        */
        boolean success = false;
        try {
            success = ImageIO.write(bufferedImage, "png", new File("d:/3.png"));
            Runtime.getRuntime().exec(new String[]{"cmd.exe", "/c", "d:/3.png"});
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
    public static void test2() {
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
        int fontHeight = imgHeight - 10;
        g.setFont(new Font("宋体", Font.PLAIN, fontHeight));
        //输入汉字，必须设置字体，且 系统中必须有对应字体，否则会乱码。
        String content = "小明";

        FontMetrics metrics = g.getFontMetrics(g.getFont());

        System.out.println("fontHeight:" + String.valueOf(fontHeight));
        System.out.println("ascent:" + metrics.getAscent());
        System.out.println("descent:" + metrics.getDescent());
        System.out.println("height:" + metrics.getHeight());

        int x = (imgWidth - metrics.stringWidth(content)) / 2;
        int y = ((imgHeight - metrics.getHeight()) / 2) + metrics.getAscent();

        g.drawString(content, x, y);

        g.dispose();

        boolean success = false;
        try {
            success = ImageIO.write(bufferedImage, "png", new File("d:/3.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("result :" + success);
    }

    //设置图片背景色
    public static void test3() {
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
            success = ImageIO.write(bufferedImage, "png", file);
            Runtime.getRuntime().exec(new String[]{"cmd.exe", "/c", "d:/3.png"});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //图片上画线 多边形
    public static void test4() {
        // 创建一个画布
        BufferedImage bufferedImage = new BufferedImage(100, 200, BufferedImage.TYPE_INT_RGB);
        // 创建一个画笔，要开始画东西了
        Graphics2D g = bufferedImage.createGraphics();
        // 画布背景色
        /*g.setBackground(Color.WHITE);
        g.clearRect(0, 0, 100,200);*/

        //线
        g.drawLine(0, 0, 99, 120);

        //多边形
        int[] arr1 = new int[]{10, 40, 30, 40, 50};
        int[] arr2 = new int[]{20, 40, 10, 10, 60};

        g.drawPolygon(arr1, arr2, arr1.length);

        g.dispose();

        boolean success = false;
        try {
            File file = new File("d:/3.png");
            success = ImageIO.write(bufferedImage, "png", file);
            Runtime.getRuntime().exec(new String[]{"cmd.exe", "/c", "d:/3.png"});
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //旋转角度
    public static void test5() {


        // 创建一个画布
        BufferedImage bufferedImage = new BufferedImage(100, 200, BufferedImage.TYPE_INT_RGB);
        // 创建一个画笔，要开始画东西了
        Graphics2D g = bufferedImage.createGraphics();


        int jiaodu = 60; // 角度 [-180,180]度
        g.rotate(jiaodu * Math.PI / 180);
        g.drawString("李卫当官", 30, 10);


        g.rotate(-60 * Math.PI / 180);


        //平移原点：向右移动10，向下移动20
        g.translate(10, 20);
        g.rotate(20 * Math.PI / 180);
        g.drawString("李", 0, 0);
        g.rotate(-20 * Math.PI / 180);

        //平移原点：向右移动20，上下不移动了
        g.translate(20, 0);
        g.rotate(-20 * Math.PI / 180);
        g.drawString("卫", 0, 0);
        g.rotate(20 * Math.PI / 180);

        g.translate(20, 0);
        g.rotate(20 * Math.PI / 180);
        g.drawString("当", 0, 0);
        g.rotate(-20 * Math.PI / 180);

        g.translate(20, 0);
        g.rotate(-20 * Math.PI / 180);
        g.drawString("官", 0, 0);
        g.rotate(20 * Math.PI / 180);

        g.dispose();

        boolean success = false;
        try {
            File file = new File("d:/3.png");
            success = ImageIO.write(bufferedImage, "png", file);
            Runtime.getRuntime().exec(new String[]{"cmd.exe", "/c", "d:/3.png"});
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
            g.setFont(new Font("Arial", Font.PLAIN, imgHeight - 18));
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

    public static void test6() throws IOException {
        String str = "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";
        str = "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";

        str = "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";
        str = "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";
        str = "H4sIAAAAAAAEA3V1dVDUX9T+Z2EJaYElpHtppBcpAQnpEKSlu7sFWUC6GwFBlm6UZoGlQ0AQkEZCkVZKYn/4e+f71zvv3Hvuc565z50598yZcwAAIHDUYmQkxIvGAwCA1tY7wN3RXZDx3gf+oaO7v+W9/eP0np42HqDLSwALAL0GsGQBAkaAlATgxf2nBXjv1fb3SOFgb+15j833dgtg4uCA7h2A2MrN2ROE8c8nAQCynnuxFVoTrQ3w/7u9F/7Pg/9P/hfHxAGB/mejdQACAASAQFiYmACANgFY5GQB5ful9X+e6GTg38cA4A2AhU773FxPh+kmci0tuesATgrpZifp7py2+10epMBLT1i/WgSLiJHDCfGrvx4rK7vCNejflSbAiogWcQw3ij0+IXtx+ZEBR56UcsdBkJuBWvWgyi5kaFaOCIXL+lIW1wdKzns9XN6bsuMUdljcP+2nQmEPzyn1g0r8FwJIGnQfQv8ee/SsLs6sGm1bx5+mlOgxmtzxmudpNzXYUHnRQ+y1JgpyAz5E07nli1Dt75bv6Yb3xb3JK7oDLpq1LiMFh//WBH9+e70p3V0j89s35RLJxwDzjfPw21+nyOvBwd6xmX3qtr5q5eHVcgQX1KMJDoRKqHCtZnAmkO1H2ZJcWyOvCJBXhMhguCzltZruEFqSPbQlY3VvkI6wwHpqwjbf13CDbrjtSDtspKfAs/u6Iq55yBDxU/JJGs4fkzIJnuLoA7CeFXlXf4ZCf1VS9iiW8Bfb3t95b3ZlQKe0lPpHrE5tI5BTus5WK5jl0nEqN+PTEgLCzreZspHpJOnvoLf2xrFw8JC7AMMlV4Z1LKeUutZvE9Q3O8ix75huNJ5b3ZgkVwctkK8bUzzNy8OnWgBZVPwvexh+GP+y96NNfi/wzXY5QJfQOX5wijN0SAD+O0psTRrOUaVf//YqqGp5X+fLU5LR/lmvRFcRKuDNgMez585K0fyqTkgvTrdsaZqH73Syes++Dt75Z9zIjWgttfAKzpSr1JZr51u0mzLb4Fks6b7tWeGtFR15d3snlqyCodUV1huPmUwq2M86N9op4BVzG50rms3n4MFapvKaH5t+bply9bJla8tsKkCNgMuQoGhtFD8ZyphS1fgrAjtKueaKnJyPPwCBCwzJJ7RFolKcVPtPZy9kJiGqZ9/gNRar81l9DwKNMXk3AUqq7l8ZcXY3gTIf2h96jppg/aZFSPCfK3z2Cbnujv/Jcsc/ReVuOkleSNw4iRRWJTP5FBb7wxPNScB47JLNb3fHuKwHMTD5qlFVEg6xr7JJKVSJW+AheawkwSQZpQFOjXTdKOZoP1ZDXjq+Chauxyt6ikmY1rBSdfD4Le7x8eOXeyXu57K5wh0/JpNG7U4aCiYQDcefeHOr32Gq3lDxUrkc2jGmB00keRsIgjG7uJVme3UnfiGCZYT5cKT5YhtN6G3Mf/jlV7IfEvWeikXPloCOFwAdzc0ISgtWUq8CHugXdnVi8nAi4xyMuL5FBHLaj8qq+E+OYI0vslEWzePg9KVgiIuocaMIn0ivb1CUhoW/AtA4Nd2nr1R45b+260CYtB+8Nd1WpUlJIk7kUI/25GrH1vRkmlUvGP+7oR6vk2/05SKKJ/ARhYwPNxPrYN4lobYA5kb2NrCxDYqUlxtiBl+MpLxFvtwFtcZ4GNv5WH1sCuhWPBh8SI5vW5RtRWUD4sSzVzaDF9PQaGP/V3ZgAcz7shuanrL/K8FJnGUBnHzecjfWOHtlX2fW62nx4mC8DzarYimkK97LcKpHEcx607zXdSyMc8MXJNfRuPbM0OBMi3nYcVx/qYZO04GP24I5hJO1eTZmhNXwPadoVKdwZjjUjMC9AhWnmSeSL2+c4a/j4pRM3NECRbWr4+dd+4GePzFIpebEii3lRFzZz4mXmKHK6flGD0GOZtl1vNOZD4yyjkl1yBfGkfHYLxUO2fQfUYdDKOsHraXaqLKduM8ZmZl31IUzW/J/Dx0Q2WVJLnJLA4Y23wHl6+R8h7Zn9L0xa27bbn/ICK6YosJDmKSPw4bVw1ua5G94SjXUmuJ37kSvbS/D19iLWHKbB1zH8EVQ7RipHePdKmSHarWelHrdqlULaN0EhKZo+ozAad1MEniwnuJFkzY7Vzj5neZhS0IpPtxrFJsOK0XkjRMyh1YhK52stQahdba+rZW4h79Q85qrfnwlnoA71ghvs0+kyJH5N1TFjSau/h1fgKUgAppuTWJCTa/FIvrG/3i9G5ZrTJTUntiZ7bI/SBo0n8TuVci76M8Vp4SUIiuxdBPED7Re+jbGtsPp+kumCTfVt08VXiutlzMoVDX+e3th1n6WK5RMNH9xT682QZTTVNwogmrpmUSkhi3U1nimqGFfNgVyyPwN3zxbUjZue8hicGkxGRwoAto3loUq5qs5SjYgh/DjpegKlkjUc65KRJC6WYkDDdns/k2fVl2JFFeTtLqedmhSVgqUZCbK1m5G63uYNM4a1Co46WzZiDwLyjdOU3/r+Aax+Wy9C3//Ia5foHnalfrSsFBDF8uVKL2TRRmzP6qSJ6GtHq9QPTcE/Ht/DzZr7jwD1t/fnNu1wydzGMoYr2prvX4nhZUwRwXNuR7iz9eG4oOTt+FD45uyuGtqK5o6hKqlYUksI25B2O+NAtlKU9GMZTuSEj8zYCZurIU11pN9f9zsaxrMWcL1dfq+5j1w3Bo3TXbAUYado52Ql9yvLnOASDb4t+oKx87HPEbMeyqjHSjv935Gy8Uj2YLnAWVJ+LHpzfuC4cMT1YK9pROPZ8royqBsod9IeHm1Prb6tQeuJAxbG3vUj9iICli/sfLl3kbEZeAe0y7UWmXsEJBB8VKRl6L2wV9fczySKDyGPjOVJqDqnIV9v3F1CEucJpt/lNpUTocSOHySPEFYTp3jbq/P3irkG3+jx+YXgsgYZ/6ruVw0u5OYHs2XaQZtPJHOVSvIUf8gwTfh0NQaFqsQiglCMNpMu1QM2ujB7xD+E6N81TSbnHNPe32eBT0HGRnYNgtqNa201bZdgbu1cJONwvG3925wfnKE9nANDqjr0832dO2zqxsXyy9tixl/316zqN1HMTrU84rSW3w9n7+7mifFMPwZfGqzieSMFG77tfnnklmN1iz7v36BTQG+7xcbl83Rm8s9CQUHqpQD1jcdQ6dzMrzX+PNZ7Y6XtDcasrLMDbvjsnj05dT1s7oKC1YMWx9PMk96OYNE6wcGbxZZCkOSIGMT8iWVmln7KfMT3VbYRLAyJnEG8VSBFmXrZuewWiXv/HdqwV1N6d4SseLc6aoED0lhuUaPZOQCtjRJMVi9F/xQg4++vNnne9s0rdpJ5P7pbW/RQu4wszYribKz6hP6LtYBwsXy0QfREOk7Ri0T95jan8FH3B7fB5kID026e1gaY9NUfCJhN1/3qsUk4DhqDLMrbx0HWvrSD1ZMaM0+0gJxu3be1mwm7C9i5Bh11TwrHF320Phlm0W/cnSXJx7U7sKt7coKjY+r1ukgFdWM4JsfU9gO8y/dVQIpux7IsNDsczriXM4lle3w2xfLUPdeOH6cafFaqTsAmIarEp3QEM0ZtBATZFFhp6vsjdv+e79i8KkxZ+WLeVNpPQ6PCY0FC/7lIPUjdtuikeDmDcIQ1L4FzltX+HvHUQ9etpHBXTwZP5/6VmXG63Ei6wAOF/EEHty1SITWefAnJmHJpRrnP3HLkwL6U849HADzt9XW/vBubYpeMtbbp+87j6N6tG5OE4rN/RX44dtFq07dTTiQhLNmlQ8ZLctzEmHPPq/gEzqFbM3wrWuhikXMshv4vrOohD13P+f50fxV+eD9cz1pUGrR3u62eHtreePX4cTH/rCz76BqnZI5nRMKDDG1wUZJKZZNVN7RINFzjWiLqjSDH2RU0hEJozwEfQ2VDQKcJf1OUa6+xn5jjZp9U/SLzQYZwZP8UX9qcnnMHai5jFObK1Rm1+q/GZQaLCieZWlXuDkSuZEF2UmYf5ZIPAlYLpyBd67mxow93TrTcFc5tEkwfluiXoOQLlhqaw6sYvGJOW4mRBhjBKJpxckxWH0ZJkBvSi+wlwJazm2o1H5QFdVwvqq76X5ql0XXRejZqJ5tgQCvuY28JuhaEEn/YlIyqaVeYpvSoE3xC9Ok0D35Orbigf/G2sT5J7rHo9q9vNOhStZ1k/4jaJ72TMP86jUTxAclgccK9VtaJ6+xIpilZtCCW5UZNuNCyty6EsmyX/L3PpdmNkjQL78GuvPZvzk1X+zxqo3L3IYqSMoLDXpGE1UFzj4Se6BLHtYaIA7GlJrsY2Cm7dl2M3t1epgVMTaz545TZhgWNEouc2z/+vDngW9zn6AFccW6PO2BDP01wZ14R+LOUg1G02x2kW1HrsjlCLvS5p4+eOCaCb5TTY2Qv1M0ao7+1b05n5nzl0sHcipEHXAQ/QCW4n3ySZs34UIPxKH+HPdULqkN3wW/L8tJPlqSQ1JXnmjEamFr+SgsOP/nvtyNx2vMhTJlepeNNB/qJAYCW0UwJVk3lNVx2bHhUL1uBgczr9zM8Lnrq2NnUYFi8x88eelpHu6/JJHOimdToL716QijutxGIxpO1SUXjdy4ROv64skQZMPAZH83k9WL1C6Xnhf9+tfyJ22TUQjYaUyGSJ4hgaDQhutB+pcmkwaRv6Aqs8oHgow0WNm16RTlhM9mkbJE74z+er/UOzcoXxVzKgQuW3/n91yPR7QYxnIR3UpkSK18QFVszFshT7ZFy9nSt/vZgPGfkOatPfgwIq9fgXg6MsP520PB4oTlNOxlIrM64WeugsYZdyLrsHbq/klDo69C8dmuZDGkxKil4NsX2hfU1V/d28OK7cz5rljch3kchnCYPqrWFKXmMjzLeLIRnP0iq6jamansQqdl3/m5g0vY8MzGS7dRgT7kC1NonwN8I2v9Pd1ILKSI2rmLrYLKlP9uveBWYEOR0ANqZO+k1V4HWvydiEpTnCIEh57pC6GoaWwnHPZiNZt9r1NLicx54ktZlTwIvekpjUac47y+W5VMT8qab07BtnFKQ2mI+y3V3vWFaukEVSjTwgd8lhhzdJU6R6OPaEtomDQ7lIgfO6UN293hOaZXzn8+u71VFEDZ/YkiUZVysKPofr7xCrBAnG8yatRDsGyrnNNUKFKCNDNjd0uTAqRFq2LRPM9gbsullf3vIWxo+oGHvCcOPJ49PJhNW21UlfH/TQXwNNb9VDherjmTuRSu6vA6ZvCQiTatc/j09vSIcnS5rofo4NKPO7o1Lt3VRWxO5uVFtQyEtUgUM1ysa8zia1FdScpzS5VCma6zWp+1i5CVGLStaZ6CD48qHCbwcfmJhGFvHUMDYMlT96HYVUjszSNbCd0qw6u74Ray2urlQbvRLzqvfr30/nknhJVGHeCErHNqOuiXDV85ljdiF8ZYLQiVGU8G98x7R388GGXhfzEQHXgxMGT8clxYjbSnPlnM/4SLJgq8E28GCr9tGw/u+SL+nQMDUMK1xUKQRMoPcX8KUIuZ6FLhevdgyot2KLY8482WLVr4SGA9oH9W61Nz7jzvBAIsQ4kb9RF//Sla+UaBrAhzlrPz4cqVSDhr/ML6bQ2YRjxAOjMKyi7Rl++L0eh/WksTsDHkISToFrFtwp9ZDROa8Go4rAn3a+lZaX/8OaCNmiYdtbg2mXwjH8aL+Kw6bViSwQImURaIN1/VzRS2xmiYO0ybX6Oiwl1yUEgPJRYfJBCW2q5atO/JbGWrI0wjM43N7vRVx8rtw6u9HDH39oJL0QuucuF50+sLiD3kPxDjwvaUXhKS95mScOd8kmZnNuUZgksRI4FSPP4SnzPIeOoUiY9mMaDOlch7ADEcZyDczPkSNjV2V+jxQQ/0+i8c33Sgbp8p/mF2UqUYB+S8UFKBSY1/J1jFFzbTKD+kwq+QJHv555MbKkaGKdjVw1u44krT/qky6nOd008P+2hpdS5mmylb6Yf2/DF+FJFn1uC/bcqvPnCf6MX4a6kUf+CgHqSmqqRoSien6f0D0080Tv4GwIxm8NqhCcoyDDXeQrnt70bW4KkRBxgdkAK52iDGcviuwSPZvJQphQ4uWCsqMOHG0YBj8OzFE7NuL7NNvL9W8p6ygUSQXn36JP2Bkk+kzBvuSW6Pj9RgvmFsaBlO2/eFOghK7a35LcGV61h/FvwLKVJi1qdKCcmr2rFYnnPKiVGu9ZPuGiC8G7mXdux7rg9rNofki6otelMoLC+n06GxPWUJdKAtXME4tYNBCh/4Cf5p0tXRyq5KYDU2R6Zeu3MtVi70nDIojOpQrkvStQ9EtvO5RdgCr6/jxi+iAGfomwEKwycmXtZqVE2eVjxmLXsGUxf1kCWD4Cz4SX0xovJojiWzRBchO1+T5/LSnt1geSS73jvuEfOE2bU/GLXTozVQbI21HsTttdmBDFNkJGNL2dMz/dvMpOnxR3Gg2d177Fum4cD0gXoLcZOd8FoLxIeBqf9IFy6X/GplyOg3zlHM170dDGpf8Dlf4NO523Xe3SEN2FlC9jtaFcgqZTzyo6JNfRy4gmnjqIKGFILc45Za/U4dSlR3Fbx46+XZ3m/bZh1JP0ldoERfMQYdEcevtCCp3dUixeBuFm9SlPSq+9N4WqX/0w+7DEdiF0CJPo/dV/KMC4WSBY6yVDOQyuSeZ12t2QOjROaK8T/E56XHI1pRlq3rEy8/dIvHZ6vFrlXaOT+WuCrgFf5kKdN2Ev/q1/7P7f5XRcBAJamPunCoP87rqhLk3uyvJ6sT6dzfqyIqapcqE1LeM4gb4X0MSSlZs+T7LSoVnvFrZCwiLu3de+hfZuYry87kR3kjDx4/qSCjlB/LcYPFWRsSdDWiF/8fgrSRzTIUAAA=";

        String filePath = "d:/1imgWrite.png";
        gunzipToFile(str, filePath);
    }

    public static void gunzipToFile(String compressedStr, String filePath) throws IOException {

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = new ByteArrayInputStream(Base64.getDecoder().decode(compressedStr));
        GZIPInputStream ginzip = new GZIPInputStream(in);

        byte[] buf = new byte[1024];
        int offset = -1;
        while (-1 != (offset = ginzip.read(buf))) {
            out.write(buf, 0, offset);
        }
        out.flush();

        byte[] byteArr = out.toByteArray();
        FileUtils.writeByteArrayToFile(new File(filePath), byteArr);
    }


    public static void AnayImage() throws IOException {
        String path = "E:\\预警系统\\全文抽取测试\\0fc1e228e0df4ccdaeeeb07f81555fe6 - 抽取结果.json";
        String destDir = "E:\\预警系统\\全文抽取测试\\0fc1e228e0df4ccdaeeeb07f81555fe6";
        extractImgToFile(path, destDir);

        path = "E:\\预警系统\\全文抽取测试\\d5196bcd340d4f6897ec7dd300e2b677 - 抽取结果.json";
        destDir = "E:\\预警系统\\全文抽取测试\\d5196bcd340d4f6897ec7dd300e2b677";
        extractImgToFile(path, destDir);
    }

    public static void testHandleJp2ToPng() throws IOException {
        String destDir = "E:\\预警系统\\全文抽取测试\\d5196bcd340d4f6897ec7dd300e2b677";
        //handleJp2ToPng(destDir);
        handleJp2ToPngAsync(destDir);
    }

    // 将目录中的 jp2 图片转成 png 图片
    public static void handleJp2ToPng(String path) throws IOException {

        ImageConvertOptions convertOptions = new ImageConvertOptions();
        convertOptions.setFormat(ImageFileType.Png);
        convertOptions.setPagesCount(1);

        Converter converter = null;

        String outputDirectory = "E:\\预警系统\\全文抽取测试\\tmp";
        String cachePath = new File(outputDirectory, "cache").getPath();

        FileCache cache = new FileCache(cachePath);

        ConverterSettings settingsFactory =  new ConverterSettings();
        settingsFactory.setCache(cache);

        Stream<Path> files = Files.walk(Paths.get(path));
        List<Path> regulearFiles = files.filter(m -> Files.isRegularFile(m)).collect(Collectors.toList());
        for (Path item : regulearFiles) {
            String filenameWithoutExt = FilenameUtils.getBaseName(item.toString());
            String extension = FilenameUtils.getExtension(item.toString());
            String baseBath = FilenameUtils.getFullPath(item.toString());
            if (extension.equals("jp2")) {
                String dest =  Paths.get(baseBath.toString(), filenameWithoutExt + "-convert.png").toString();
                System.out.println(item.toString());
                System.out.println(dest.toString());

                converter = new Converter(item.toString(), () -> settingsFactory);
                converter.convert(dest, convertOptions);

                // 删除旧的 jp2
                Files.deleteIfExists(item);
            }
        }
    }

    //Thu Apr 11 16:22:02 CST 2024
    //
    public static void handleJp2ToPngAsync(String path) throws IOException {
        System.out.println(new Date());
        Stream<Path> files = Files.walk(Paths.get(path));
        List<Path> regulearFiles = files.filter(m -> Files.isRegularFile(m)).collect(Collectors.toList());

        List<CompletableFuture<String>> taskList = new ArrayList<>();

        for (Path item : regulearFiles) {
            String filenameWithoutExt = FilenameUtils.getBaseName(item.toString());
            String extension = FilenameUtils.getExtension(item.toString());
            String baseBath = FilenameUtils.getFullPath(item.toString());
            if (extension.equals("jp2")) {
                String dest =  Paths.get(baseBath.toString(), filenameWithoutExt + "-convert.jpg").toString();

                taskList.add(doTask(item, dest));
            }
        }

        CompletableFuture[] arr = taskList.toArray(new CompletableFuture[taskList.size()]);
        CompletableFuture completableFuture = CompletableFuture.allOf(arr);

        try {
            completableFuture.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(new Date());
        System.out.println("all done");
    }
    static ExecutorService executor = Executors.newFixedThreadPool(3);
    public static CompletableFuture<String> doTask(Path item, String dest){
        return CompletableFuture.supplyAsync(() -> {

            ImageConvertOptions convertOptions = new ImageConvertOptions();
            convertOptions.setFormat(ImageFileType.Jpg);
            convertOptions.setPagesCount(1);
            convertOptions.setHorizontalResolution(50);
            convertOptions.setVerticalResolution(50);

            Converter converter = null;

            String outputDirectory = "E:\\预警系统\\全文抽取测试\\tmp";
            String cachePath = new File(outputDirectory, "cache").getPath();

            FileCache cache = new FileCache(cachePath);

            ConverterSettings settingsFactory =  new ConverterSettings();
            settingsFactory.setCache(cache);

//            System.out.println(item.toString());
//            System.out.println(dest.toString());
            System.out.println( "thread:" + Thread.currentThread() + " " + DateTime.now() + " - " + item.toString());


//            converter = new Converter(item.toString(), () -> settingsFactory);
            converter = new Converter(item.toString());
            converter.convert(dest, convertOptions);

            try {
                Files.deleteIfExists(item);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return "";
        }, executor).whenComplete((v, e) -> {
//            System.out.println(v);
//            System.out.println(e);
        });
    }

    /**
     * 提取 JSON 的 Images 到某个目录
     *
     * @param jsonPath
     * @param destDir
     * @throws IOException
     */
    public static void extractImgToFile(String jsonPath, String destDir) throws IOException {
        String fileContent = FileUtils.readFileToString(new File(jsonPath), Charset.defaultCharset());
        JSONObject jsonObject = JSON.parseObject(fileContent);

        JSONArray images = jsonObject.getJSONArray("images");
        for (Object image : images) {
            JSONObject jsonImage = (JSONObject) image;
            String imgCode = jsonImage.get("imgCode").toString();
            String imgBase64 = jsonImage.get("imgBase64").toString();
            PageInfoResult pageInfo = getPageInfo(jsonObject, imgCode);

            String fileName = pageInfo.getPageInfo() + "-" + imgCode + "-" + pageInfo.getImgName();
            String filePath = destDir + "\\" + fileName;
            gunzipToFile(imgBase64, filePath);
        }
    }

    /**
     * 获取图片所在页的信息
     *
     * @param jsonObject
     * @param targetImgCode
     * @return 第3页
     */
    public static PageInfoResult getPageInfo(JSONObject jsonObject, String targetImgCode) {
        JSONArray contents = jsonObject.getJSONArray("content");
        for (Object content : contents) {
            JSONObject jsonContent = (JSONObject) content;
            String pageNum = jsonContent.get("位置").toString();
            JSONArray imgArr = jsonContent.getJSONArray("图片");
            for (Object img : imgArr) {
                JSONObject jsonImage = (JSONObject) img;
                String imgCode = jsonImage.get("imgCode").toString();
                String imgName = jsonImage.get("imgName").toString();
                if (imgCode.equals(targetImgCode))
                    return new PageInfoResult(pageNum, imgName);
            }
        }
        return null;
    }

    public static String gzip(String data) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzip = new GZIPOutputStream(out);
        gzip.write(data.getBytes());
        gzip.close();
        return Base64.getEncoder().encodeToString(out.toByteArray());
    }

    public static String gunzip2(String compressedStr) {
        String str = null;
        try (
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                ByteArrayInputStream in = new ByteArrayInputStream(Base64.getDecoder().decode(compressedStr));
                GZIPInputStream ginzip = new GZIPInputStream(in)
        ) {
            byte[] buf = new byte[1024];
            int offset = -1;
            while (-1 != (offset = ginzip.read(buf))) {
                out.write(buf, 0, offset);
            }
            out.flush();
            str = out.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return str;
    }
}
