package fileDemo;

import org.apache.poi.openxml4j.opc.internal.FileHelper;

import java.io.File;

public class Test {
    public static void main(String[] args) {
        File file = FileHelper.getDirectory(new File("D:\\datafortablebigdata\\extraExcel\\52\\统计公报\\2014年闽侯县国民经济和社会发展统计公报.txt"));
        System.out.println(file.getPath());
        System.out.println(file.getAbsolutePath());

        int number = 2;
        String msg =  number > 1 ? "大于1" : "小于1";
        int result = number > 2 ? 10 : 20;
    }
}
