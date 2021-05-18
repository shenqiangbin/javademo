package ExcelDemo.csv;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.text.csv.CsvData;
import cn.hutool.core.text.csv.CsvReader;
import cn.hutool.core.text.csv.CsvRow;
import cn.hutool.core.text.csv.CsvUtil;

import java.util.List;

/**
 * 这个测试失败
 */
public class CSVReader2 {

    public static void main(String[] args) {

        String file = "/Users/adminqian/my/test.csv";
        String charset = "utf-8";

        CsvReader reader = CsvUtil.getReader();
        //从文件中读取CSV数据

        CsvData data = reader.read(FileUtil.file(file));
        List<CsvRow> rows = data.getRows();
        //遍历行
        for (CsvRow csvRow : rows) {
            //getRawList返回一个List列表，列表的每一项为CSV中的一个单元格（既逗号分隔部分）
            List<String> list = csvRow.getRawList();
            System.out.println(list);
        }

        System.out.println("over");
    }
}
