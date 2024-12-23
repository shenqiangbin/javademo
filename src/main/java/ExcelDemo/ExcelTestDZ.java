package ExcelDemo;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.bouncycastle.crypto.agreement.jpake.JPAKEUtil;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExcelTestDZ {
    public static void main(String[] args) throws IOException, InvalidFormatException {


        String filePath = "d:/御园11号楼.xls";
        InputStream stream = new FileInputStream(filePath);
        Workbook wb = WorkbookFactory.create(stream);

        List<List<Object>> objects = new ArrayList<>();
        for (int i = 0; i < wb.getNumberOfSheets(); i++) {
            Sheet sheet = wb.getSheetAt(i);
            if (sheet == null || sheet.getLastRowNum() == 0) {
                return;
            }

            Row row = null;
            int totalRow = sheet.getLastRowNum();

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

                if (r == 1) {

                }

//                System.out.println(r);
//                System.out.println(Arrays.deepToString(cells.toArray()));
            }

            for (int r = 1; r <= sheet.getLastRowNum(); r += 3) {
                cells = new ArrayList<>();
                Row row1 = sheet.getRow(r);
                Row row2 = sheet.getRow(r + 1);
                Row row3 = sheet.getRow(r + 2);

                for (short colIx = minColIx; colIx < maxColIx; colIx++) {
                    String cellVal1 = getCellVal(row1.getCell(colIx));
                    String cellVal2 = getCellVal(row2.getCell(colIx));
                    String cellVal3 = getCellVal(row3.getCell(colIx));
                    System.out.println(String.format("%s,%s,%s", cellVal1, cellVal2, cellVal3));
                    List<Object> data = new ArrayList<>() {{
                        add(cellVal1);
                        add(cellVal2);
                        add(cellVal3);
                    }};
                    objects.add(data);
                }
            }

            save(objects);
        }

    }

    static void save(List<List<Object>> objects) throws IOException {
        String date = new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date());
        Excel2007Utils.writeExcelData("E:\\", "dz-"+date, "Sheet1", objects);
    }

    private static String getCellVal(Cell cell) {
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
}
