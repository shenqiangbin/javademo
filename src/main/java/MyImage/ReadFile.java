package MyImage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ReadFile {
    public static void main(String[] args) throws IOException {
        readFile("D:\\资料\\新建文件夹\\北京_intermed_results.txt");
        readFile("d:/test/ttest/dic.txt");
    }

    public static void readFile(String path) throws IOException {
        FileInputStream fileInputStream = null;
        BufferedReader bufferedReader = null;
        try {
            fileInputStream = new FileInputStream(path);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));

            StringBuilder builder = new StringBuilder();

            int word;
            while ((word = bufferedReader.read()) != -1) {
                builder.append((char)word);
            }

            System.out.println(builder.toString());

//            String line;
//            while ((line = bufferedReader.readLine()) != null) {
//                System.out.println(line);
//            }

        } finally {
            bufferedReader.close();
            fileInputStream.close();
        }
    }
}
