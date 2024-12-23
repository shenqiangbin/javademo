import MyImage.ImgUtil;
import net.coobird.thumbnailator.Thumbnails;

import java.io.*;

public class HutoolTest {
    public static void main(String[] args) throws IOException {

        //-Djava.io.tmpdir=d:/tmptest
        String tempDir = System.getProperty("java.io.tmpdir");
        System.out.println(tempDir);

        File srcImageFile = new File("c:/Users/cnki52/Pictures/1.jpg");
        File destImageFile = new File("c:/Users/cnki52/Pictures/1-2.jpg");

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        //cn.hutool.core.img.ImgUtil.scale(srcImageFile, destImageFile, 0.5f);
        cn.hutool.core.img.ImgUtil.scale(new FileInputStream(srcImageFile), byteArrayOutputStream, 0.5f);

//        <!-- https://mvnrepository.com/artifact/net.coobird/thumbnailator -->
//        <dependency>
//            <groupId>net.coobird</groupId>
//            <artifactId>thumbnailator</artifactId>
//            <version>0.4.20</version>
//        </dependency>
//         除了可以缩放外，还可以添加水印。
//        Thumbnails.of(srcImageFile).scale(0.5f).toOutputStream(destImageFile);
//        Thumbnails.of(srcImageFile).scale(0.5f).toFile("c:/Users/cnki52/Pictures/1-2-thumbnails.jpg");


    }
}
