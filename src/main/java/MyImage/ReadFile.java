package MyImage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ReadFile {
    public static void main(String[] args) throws IOException {
        //readFile("D:\\资料\\新建文件夹\\北京_intermed_results.txt");
        //readFile("d:/test/ttest/dic.txt");
        readFile("d:/更新/107XT02_intermed_results.txt");
    }

    public static void readFile(String path) throws IOException {
        FileInputStream fileInputStream = null;
        BufferedReader bufferedReader = null;
        try {
            fileInputStream = new FileInputStream(path);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));

//            StringBuilder builder = new StringBuilder();
//
//            int word;
//            while ((word = bufferedReader.read()) != -1) {
//                builder.append((char)word);
//            }
//
//            System.out.println(builder.toString());

            String dir = "d:/更新/";
            int i = 0;
            int fileNum = 1;
            String fileName = "";
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                i++;
                if (i % 10000 == 0) {
                    fileName = fileNum + "107XT02_intermed_results.txt";
                    createIfNotExist(dir + fileName, true);
                }
                if (!line.contains("测试")) {
                    System.out.println(i + " " + line);

                    writeFile(dir + fileName, line + "\n");
                }
            }

        } finally {
            bufferedReader.close();
            fileInputStream.close();
        }
    }

    public static void createIfNotExist(String path, boolean isFile) throws IOException {
        File file = new File(path);
        //如果文件不存在则创建
        if (!file.exists()) {
            if (!isFile) {
                //如果是目录
                file.mkdirs();
            } else {
                //如果是文件
                file.createNewFile();
            }
        }
    }

    public static void writeFile(String path, String content) throws IOException {
        FileOutputStream fileOutputStream = null;
        BufferedWriter bufferedWriter = null;
        try {
            fileOutputStream = new FileOutputStream(path, true);
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(fileOutputStream));

            bufferedWriter.write(content);
        } finally {
            bufferedWriter.flush();
            bufferedWriter.close();
            fileOutputStream.flush();
            fileOutputStream.close();
        }
    }
}
