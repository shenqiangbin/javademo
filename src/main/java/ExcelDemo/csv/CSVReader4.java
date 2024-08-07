package ExcelDemo.csv;

import de.siegmar.fastcsv.reader.CsvReader;
import de.siegmar.fastcsv.reader.CsvRow;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.util.List;

/**
 * 这个测试OK
 */
public class CSVReader4 {
    public static void main(String[] args) throws IOException {

        String path = System.getProperty("user.dir");
        String file = Paths.get(path, "src/main/java/ExcelDemo/csv/test.csv").toString();
        //file = "/Users/adminqian/my/国统局合作_宏观经济指标体系.xlsx";
        String charset = "utf-8";

        CsvReader csv = CsvReader.builder().build(Paths.get(file), Charset.forName(charset));
        int i = 0;
        for (CsvRow item : csv) {
            i++;
            List<String> list = item.getFields();
            if (list.size() != 9) {
                System.out.println("here");
            }
            if (list.contains("用人单位招聘服务") || list.contains("Y12_GL05ZW01076")) {
                System.out.println("here");
            }

        }
        System.out.println(i);

    }
}
