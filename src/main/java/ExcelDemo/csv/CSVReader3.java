package ExcelDemo.csv;

import org.supercsv.io.CsvMapReader;
import org.supercsv.io.ICsvMapReader;
import org.supercsv.prefs.CsvPreference;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

/**
 * 这个测试失败
 */
public class CSVReader3 {

    public static void main(String[] args) throws IOException {

        String file = "/Users/adminqian/my/test.csv";
        String charset = "utf-8";

        ICsvMapReader mapReader = null;
        try {
            mapReader = new CsvMapReader(new FileReader(file), CsvPreference.STANDARD_PREFERENCE);

            // the header columns are used as the keys to the Map
            final String[] header = mapReader.getHeader(true);
            //final CellProcessor[] processors = getProcessors();

            Map<String, String> customerMap;
            int i = 0;
            while ((customerMap = mapReader.read(header)) != null) {
                i++;
                System.out.println(String.format("lineNo=%s, rowNo=%s, customerMap=%s", mapReader.getLineNumber(),
                        mapReader.getRowNumber(), customerMap));
            }
            System.out.println("line count:" + i);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (mapReader != null) {
                mapReader.close();
            }
        }
    }
}
