package ExcelDemo;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import dbmgr.mySqlAccess.MySqlHelper;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.*;
import java.util.*;

/**
 * 合并相同数据得单元格
 */
public class Nongkeyuan_excel {

    public static void main(String[] args) throws Exception {

        String filePath = "E:\\temp\\国家数据.xlsx";
        String newfilePath = "E:\\temp\\国家数据-2.xlsx";

        InputStream stream = new FileInputStream(filePath);
        Workbook wb = WorkbookFactory.create(stream);

        Sheet sheet = wb.getSheetAt(0);

        short minColIx = 0;
        short maxColIx = 0;

        for (int r = 1; r <= sheet.getLastRowNum(); r++) {
            Row row = sheet.getRow(r);
            minColIx = row.getFirstCellNum();
            maxColIx = row.getLastCellNum();
            break;
        }

        maxColIx = 23;

        String origiVal = "";
        int startrow = 0;
        int endrow = 0;

        for (int col = minColIx; col <= maxColIx; col++) {
            for (int r = 1; r <= sheet.getLastRowNum(); r++) {
                Row row = sheet.getRow(r);
                String cellVal = getCellVal(row.getCell(col));

                if (cellVal.equals(origiVal)) {
                    // 置空当前cell
                    if (row.getCell(col) != null) {
                        row.getCell(col).setCellValue("");
                    }
                } else {
                    endrow = r - 1;
                    if (StringUtils.isNotEmpty(origiVal) && endrow > startrow) {
                        //sheet.addMergedRegion(new CellRangeAddress(startrow, endrow, col, col));
                    }
                    if (StringUtils.isNotEmpty(cellVal)) {
                        origiVal = cellVal;
                    }
                    startrow = r;
                }
                if(r == sheet.getLastRowNum()){
                    endrow = r;
                    if (StringUtils.isNotEmpty(origiVal) && endrow > startrow) {
                        //sheet.addMergedRegion(new CellRangeAddress(startrow, endrow, col, col));
                    }
                }
            }

        }


        OutputStream ops = new FileOutputStream(newfilePath);
        wb.write(ops);
        ops.flush();
        ops.close();
        wb.close();
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
