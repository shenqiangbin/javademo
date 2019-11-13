package ExcelDemo;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;

public class TreeExcel {
    private String filePath;
    private boolean isExcel2003;
    private Workbook wb = null;
    private IResultHandler resultHandler;
    private boolean debug = false;

    public TreeExcel(String filePath, IResultHandler handler) {
        this.filePath = filePath;
        this.resultHandler = handler;
        isExcel2003 = filePath.matches("^.+\\.(?i)(xls)$");
    }

    public void enableDebug() {
        this.debug = true;
    }

    public void handle() throws IOException {
        InputStream stream = new FileInputStream(filePath);

//        String thefileName = new File(filePath).getName();
//        String thefileNameWithoutExt = thefileName.substring(0, thefileName.indexOf("."));

        if (isExcel2003) {
            wb = new HSSFWorkbook(stream);
        } else {
            wb = new XSSFWorkbook(stream);
        }

        for (int i = 0; i < wb.getNumberOfSheets(); i++) {
            Sheet sheet = wb.getSheetAt(i);
            if (sheet == null || sheet.getLastRowNum() == 0)
                return;

            Row row = null;
            int totalRow = sheet.getLastRowNum();
            if (this.debug)
                System.out.println("共有" + totalRow + "行");

            for (int r = 0; r <= sheet.getLastRowNum(); r++) {
                row = sheet.getRow(r);
                short minColIx = row.getFirstCellNum();
                short maxColIx = row.getLastCellNum();

                for (short colIx = minColIx; colIx < maxColIx; colIx++) {

                    Cell cell = row.getCell(colIx);
                    String cellVal = getCellVal(cell);

                    String parentId = "0";
                    if (colIx > minColIx) {
                        CellModel cellModel = getCellValIfMerged(wb, sheet, row, r, colIx - 1);
                        if (cellModel != null) {
                            parentId = cellModel.getFontName();
                        }
                    }

                    String recordId = "";
                    boolean isLeaf = colIx == maxColIx - 1;
                    if (StringUtils.isNotBlank(cellVal)) {
                        recordId = resultHandler.store(cellVal, parentId, isLeaf);
                        setFont(wb, recordId, colIx, row);
                    }

                    if (this.debug)
                        System.out.println(String.format("cellVal:%s recordId:%s parentId:%s isLeaf:%s", cellVal, recordId, parentId, isLeaf));
                }

            }
        }
    }

    private String getCellVal(Cell cell) {
        String cellVal = "";
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

    private void setFont(Workbook wb, String val, int col, Row row) {

        if (isExcel2003) {
            HSSFCellStyle style = (HSSFCellStyle) wb.createCellStyle();
            HSSFFont font = (HSSFFont) wb.createFont();
            font.setFontName(val);
            style.setFont(font);
            row.getCell(col).setCellStyle(style);
        } else {
            XSSFCellStyle style = (XSSFCellStyle) wb.createCellStyle();
            XSSFFont font = (XSSFFont) wb.createFont();
            font.setFontName(val);
            style.setFont(font);
            row.getCell(col).setCellStyle(style);
        }
    }

    private String getFont(Workbook wb, int col, Row row) {
        String name = "";
        if (isExcel2003) {
            name = ((HSSFCellStyle) row.getCell(col).getCellStyle()).getFont(wb).getFontName();
        } else {
            name = ((XSSFCellStyle) row.getCell(col).getCellStyle()).getFont().getFontName();
        }
        return name;
    }

    private CellModel getCellValIfMerged(Workbook wb, Sheet sheet, Row row, int rowIndex, int col) {
        Cell cell = row.getCell(col);
        String cellVal = getCellVal(cell);
        String fontName = getFont(wb, col, row);

        if (!StringUtils.isNotBlank(cellVal)) {
            return getMergedRegionValue(wb, sheet, rowIndex, col);
        }

        //System.out.println(String.format("%s-%s %s", rowIndex, col, cellVal));
        return new CellModel(cellVal, fontName);
    }

    private CellModel getMergedRegionValue(Workbook wb, Sheet sheet, int row, int column) {
        int sheetMergeCount = sheet.getNumMergedRegions();

        for (int i = 0; i < sheetMergeCount; i++) {
            CellRangeAddress ca = sheet.getMergedRegion(i);
            int firstColumn = ca.getFirstColumn();
            int lastColumn = ca.getLastColumn();
            int firstRow = ca.getFirstRow();
            int lastRow = ca.getLastRow();

            if (row >= firstRow && row <= lastRow) {
                if (column >= firstColumn && column <= lastColumn) {
                    Row fRow = sheet.getRow(firstRow);
                    Cell fCell = fRow.getCell(firstColumn);
                    String fontName = getFont(wb, firstColumn, fRow);
                    String cellVal = getCellVal(fCell);

                    return new CellModel(cellVal, fontName);
                }
            }
        }

        return null;
    }

    class CellModel {
        private String cellVal;
        private String fontName;

        public CellModel(String cellVal, String fontName) {
            this.cellVal = cellVal;
            this.fontName = fontName;
        }

        public String getCellVal() {
            return cellVal;
        }

        public void setCellVal(String cellVal) {
            this.cellVal = cellVal;
        }

        public String getFontName() {
            return fontName;
        }

        public void setFontName(String fontName) {
            this.fontName = fontName;
        }
    }

}
