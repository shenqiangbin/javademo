package ExcelDemo;

import com.opencsv.CSVReader;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;

import java.io.*;
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

    public CommonExcel(String filePath, ICommonResultHandler handler) {
        this.filePath = filePath;
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

    public String[] getTitle() throws IOException, InvalidFormatException {
        if (isCSV) {
            return this.getCsvTitle();
        } else {
            return this.getExcelTitle();
        }
    }

    private String[] getCsvTitle() throws IOException {
        DataInputStream in = null;
        String[] titles = null;
        try {
            in = new DataInputStream(new FileInputStream(filePath));
            CSVReader reader = new CSVReader(new InputStreamReader(in, "gbk"));
            titles = reader.readNext();
        } finally {
            if (in != null) {
                in.close();
            }
        }
        return titles;
    }

    private String[] getExcelTitle() throws IOException, InvalidFormatException {
        InputStream stream = new FileInputStream(filePath);
        wb = WorkbookFactory.create(stream);

        Sheet sheet = wb.getSheetAt(0);
        if (sheet == null || sheet.getLastRowNum() == 0) {
            return null;
        }

        int r = 0;
        List<String> cells = new ArrayList<>();
        Row row = sheet.getRow(r);

        short minColIx = row.getFirstCellNum();
        short maxColIx = row.getLastCellNum();

        for (short colIx = minColIx; colIx < maxColIx; colIx++) {
            Cell cell = row.getCell(colIx);
            String cellVal = getCellVal(cell);
            cells.add(cellVal);
        }

        wb.close();
        stream.close();

        return cells.toArray(new String[cells.size()]);
    }

    private void csvHandle() throws IOException {
        DataInputStream in = new DataInputStream(new FileInputStream(filePath));
        CSVReader reader = new CSVReader(new InputStreamReader(in, "gbk"));
        String[] titles = null;
        String[] strs;
        int i = 1;
        while ((strs = reader.readNext()) != null) {
            if (i == 1) {
                titles = Arrays.copyOfRange(strs, 0, strs.length);
            } else {
                resultHandler.store(strs, titles);
            }
            i++;
        }
        resultHandler.done();
        reader.close();
    }

    private void excelHandle() throws IOException, InvalidFormatException {
        InputStream stream = new FileInputStream(filePath);
        wb = WorkbookFactory.create(stream);

        for (int i = 0; i < wb.getNumberOfSheets(); i++) {
            Sheet sheet = wb.getSheetAt(i);
            if (sheet == null || sheet.getLastRowNum() == 0) {
                return;
            }

            Row row = null;
            int totalRow = sheet.getLastRowNum();
            if (this.debug) {
                System.out.println("共有" + totalRow + "行");
            }

            List<String> cells = new ArrayList<>();
            String[] titles = null;

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
                    titles = cells.toArray(new String[cells.size()]);
                } else {
                    if (!isRowEmpty(cells)) {
                        resultHandler.store(cells.toArray(new String[cells.size()]), titles);
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
        print("ok");
        String file = "F:\\daoru.xlsx";
        //file = "F:\\daoru.csv";
        CommonExcel commonExcel = new CommonExcel(file, new ICommonResultHandler() {
            @Override
            public String store(String[] cellVals, String[] titles) {
                System.out.println(Arrays.deepToString(cellVals));
                System.out.println(Arrays.deepToString(titles));

                buildSql(cellVals, titles);
                return "ok";
            }

            @Override
            public void done() {
                System.out.println("done");
            }
        });
        String[] titles = commonExcel.getTitle();
        // validateTitles 获取表头后，先验证表头，验证通过，再执行 commonExcel.handle() 方法
        System.out.println("titles");
        System.out.println(Arrays.deepToString(titles));

        commonExcel.handle();
    }

    private static void buildSql(String[] cellVals, String[] titles) {
        for (int i = 0; i < titles.length; i++) {
            String title = titles[i];
            String val = cellVals[i];
        }
    }

    private static void print(String msg) {
        System.out.println(msg);
    }
}
