package ExcelDemo;

import de.siegmar.fastcsv.reader.CsvReader;
import de.siegmar.fastcsv.reader.CsvRow;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommonExcel {

    private String filePath;
    private boolean isExcel2003;
    private boolean isCSV;
    private Workbook wb = null;
    private ICommonResultHandler resultHandler;
    private boolean debug = false;
    private String charset = "GBK";

    public CommonExcel(String filePath, ICommonResultHandler handler) {
        this(filePath, "GBK", handler);
    }

    public CommonExcel(String filePath, String charset, ICommonResultHandler handler) {
        this.filePath = filePath;
        this.charset = charset;
        this.resultHandler = handler;
        isExcel2003 = filePath.matches("^.+\\.(?i)(xls)$");
        isCSV = filePath.matches("^.+\\.(?i)(csv)$");
    }

    public void handle() throws IOException, InvalidFormatException {
        if (isCSV) {
            this.csvHandle();
        } else {
            this.excelHandle();
        }
    }

    private void csvHandle() throws IOException {
        CsvReader csv = CsvReader.builder().build(Paths.get(filePath), Charset.forName(charset));

        List<String> titles = null;
        int i = 1;

        for (CsvRow item : csv) {
            if (i == 1) {
                if (resultHandler.validateTilte(item.getFields())) {
                    break;
                }
            } else {
                resultHandler.store(item.getFields(), titles);
            }
            i++;
        }
        resultHandler.done();
    }

    private void excelHandle() throws IOException, InvalidFormatException {
        InputStream stream = new FileInputStream(filePath);
        wb = WorkbookFactory.create(stream);

        for (int i = 0; i < wb.getNumberOfSheets(); i++) {
            Sheet sheet = wb.getSheetAt(i);
            if (sheet == null || sheet.getLastRowNum() == 0) {
                return;
            }

            Row row;
            int totalRow = sheet.getLastRowNum();
            if (this.debug) {
                System.out.println("共有" + totalRow + "行");
            }

            List<String> cells;
            List<String> titles = null;

            short minColIx = 0;
            short maxColIx = 0;

            for (int r = 0; r <= sheet.getLastRowNum(); r++) {

                cells = new ArrayList<>();
                row = sheet.getRow(r);

                /* 以 表头 的列为标准 */
                if (r == 0) {
                    minColIx = row.getFirstCellNum();
                    maxColIx = row.getLastCellNum();
                }

                for (short colIx = minColIx; colIx < maxColIx; colIx++) {
                    Cell cell = row.getCell(colIx);
                    String cellVal = getCellVal(cell);
                    cells.add(cellVal);
                }

                if (r == 0) {
                    titles = cells;
                    resultHandler.validateTilte(titles);
                } else {
                    if (!isRowEmpty(cells)) {
                        resultHandler.store(cells, titles);
                    }
                }

            }

            resultHandler.done();
        }

        wb.close();
        stream.close();
    }

    private boolean isRowEmpty(List<String> list) {
        for (String item : list) {
            if (StringUtils.isNotEmpty(item)) {
                return false;
            }
        }
        return true;
    }

    private String getCellVal(Cell cell) {
        String cellVal = "";
        if (cell == null) {
            return cellVal;
        }

        if (cell.getCellTypeEnum() == CellType.NUMERIC) {

            java.text.DecimalFormat formatter = new java.text.DecimalFormat("########.####");
            cellVal = formatter.format(cell.getNumericCellValue());

        } else if (cell.getCellTypeEnum() == CellType.BOOLEAN) {
            cellVal = String.valueOf(cell.getBooleanCellValue());
        } else if (cell.getCellTypeEnum() == CellType.FORMULA) {
            try {
                cellVal = cell.getStringCellValue();
            } catch (IllegalStateException e) {
                cellVal = String.valueOf(cell.getNumericCellValue());
            }
            System.out.println(cellVal);
        } else {
            cellVal = cell.getStringCellValue();
        }
        return cellVal;
    }

    public static void main(String[] args) throws IOException, InvalidFormatException {
        String file = "F:\\daoru.xlsx";
        //file = "F:\\daoru.csv";
        file = "d:/data/1.xlsx";

        final String[] validateMsg = {""};
        List<List<String>> abc = new ArrayList<>();
        CommonExcel commonExcel = new CommonExcel(file, new ICommonResultHandler() {

            @Override
            public boolean validateTilte(List<String> titles) {
                if(titles == null || titles.size() == 5){
                    validateMsg[0] = "标题不对";
                    return false;
                }
                return true;
            }

            @Override
            public String store(List<String> cellVals, List<String> titles) {
                System.out.println(cellVals);
                System.out.println(titles);
                abc.add(cellVals);
                return "ok";
            }

            @Override
            public void done() {
                System.out.println("done");
            }
        });
        commonExcel.handle();

        if(validateMsg[0] != ""){
            // 格式不对，直接提示给用户
        }

        // 这里可以先对 abc 进行验证一下，然后再进行操作。

    }

}
