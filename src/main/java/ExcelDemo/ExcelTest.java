package ExcelDemo;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ExcelTest {
    public static void main(String[] args) throws IOException, InvalidFormatException {


        String filePath = "d:/御园11号楼.xlsx";
        InputStream stream = new FileInputStream(filePath);
        Workbook wb = WorkbookFactory.create(stream);

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

                System.out.println(r);
                System.out.println(Arrays.deepToString(cells.toArray()));
            }
        }
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
