package ExcelDemo.csv;

import com.opencsv.CSVReader;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 这个测试失败
 */
public class CSVReader1 {
    public static void main(String[] args) throws IOException {
        String file = "/Users/adminqian/my/test.csv";
        String charset = "utf-8";

        CSVReader reader = new CSVReader(new InputStreamReader(new FileInputStream(file), charset));
        List<String[]> dataList = new ArrayList<>();
        int rowCount = 0;
        String[] strs;
        while ((strs = reader.readNext()) != null) {
            //strs = Arrays.copyOfRange(strs, 0, strs.length);
            dataList.add(strs);
            if(strs.length != 9){
                System.out.println("here");
            }
            rowCount++;
        }

        //我们最好通过打断点的形式来看读出的数据
        System.out.println("total row:" + rowCount);

    }
}
