package ExcelDemo;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 解析 csv 文件记录导入到数据库（解析到一行，缓存一行，缓存到100行，一次性插入到数据库中）
 */
public class ParsingFile {

    public static void main(String[] args) throws IOException {
        parseByLine();
    }

    public static void parse() throws IOException {
        File file = new File("f:/目录.csv");
        InputStream inputStream = new FileInputStream(file);

        byte[] content = new byte[inputStream.available()];
        int result;
        do {
            result = inputStream.read(content, 0, content.length);
        }
        while (result != -1);

        System.out.println(content);
        for (byte item : content) {
            System.out.print(item + ",");
        }
        System.out.println("");
        String val = new String(content, "GBK");
        System.out.println(val);

        /* 13,10 这相邻的这个byte值就是 \r\n */
        String str = new String(new byte[]{13, 10}, "GBK");
        System.out.println(str);
    }

    public static void parseByLine() throws IOException {
        File file = new File("f:/目录.csv");
        InputStream inputStream = new FileInputStream(file);
        System.out.println(inputStream.available());

        byte[] content;
        int length = 3;
        int result;
        do {
            content = new byte[length];
            result = inputStream.read(content, 0, length);

            for (int i = 0; i < 3; i++) {
                if (content[i] != 0){
                    byteList.add(content[i]);
                }
            }

            boolean hasLine;
            do{
                hasLine = handle();
            }while (hasLine);
        }
        while (result != -1);

        handleLine(byteList);
        byteList.clear();
    }

    public static List<Byte> byteList = new ArrayList<>();

    public static boolean handle() throws UnsupportedEncodingException {
        boolean hasLine = false;
        List<Byte> line = new ArrayList<>();
        int breakPoint = 0;
        for (int i = 0; i < byteList.size() - 1; i++) {
            if (byteList.get(i) == 13 && byteList.get(i + 1) == 10) {
                hasLine = true;
                handleLine(line);
                breakPoint = i + 2;
                break;
            } else {
                line.add(byteList.get(i));
            }
        }

        int index = 0;
        while (index < breakPoint) {
            byteList.remove(0); //始终删除的第一位的值
            index++;
        }

        return hasLine;
    }

    public static void handleLine(List<Byte> line) throws UnsupportedEncodingException {
        byte[] arr = new byte[line.size()];

        for (int i = 0; i < arr.length; i++)
            arr[i] = line.get(i);

        String str = new String(arr, "GBK");
        System.out.println(str);
    }
}
