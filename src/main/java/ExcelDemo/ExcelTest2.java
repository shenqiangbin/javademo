package ExcelDemo;

import de.siegmar.fastcsv.reader.CsvReader;
import de.siegmar.fastcsv.reader.CsvRow;
import io.netty.util.internal.StringUtil;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvMapReader;
import org.supercsv.io.ICsvMapReader;
import org.supercsv.prefs.CsvPreference;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ExcelTest2 {
    public static void main(String[] args) throws IOException, InvalidFormatException {

        //hangye();
        test();
    }

//    private static CellProcessor[] getProcessors() {
//
//        final String emailRegex = "[a-z0-9\\._]+@[a-z0-9\\.]+"; // just an example, not very robust!
//        StrRegEx.registerMessage(emailRegex, "must be a valid email address");
//
//        final CellProcessor[] processors = new CellProcessor[] {
//                new UniqueHashCode(), // customerNo (must be unique)
//                new NotNull(), // firstName
//                new NotNull(), // lastName
//                new ParseDate("dd/MM/yyyy"), // birthDate
//                new NotNull(), // mailingAddress
//                new Optional(new ParseBool()), // married
//                new Optional(new ParseInt()), // numberOfKids
//                new NotNull(), // favouriteQuote
//                new StrRegEx(emailRegex), // email
//                new LMinMax(0L, LMinMax.MAX_LONG) // loyaltyPoints
//        };
//
//        return processors;
//    }


    public static void test() throws IOException, InvalidFormatException {

        String file = "/Users/adminqian/my/test.csv";

        String charset = "utf-8";
//        DataInputStream in = new DataInputStream(new FileInputStream(file));
//        CSVReader reader = new CSVReader(new InputStreamReader(in, "utf-8"));
//        List<String[]> list = reader.readAll();





    }

    public static void zhuti() throws IOException, InvalidFormatException {
        List<Model> list = new ArrayList<>();

        String filePath = "d:/行业及主题分类.xlsx";
        InputStream stream = new FileInputStream(filePath);
        Workbook wb = WorkbookFactory.create(stream);

        Sheet sheet = wb.getSheetAt(0);

        Row row = null;
        int totalRow = sheet.getLastRowNum();

        List<String> cells = new ArrayList<>();
        String[] titles = null;

        short minColIx = 0;
        short maxColIx = 0;

        for (int r = 0; r <= sheet.getLastRowNum(); r++) {

            cells = new ArrayList<>();
            row = sheet.getRow(r);

            minColIx = row.getFirstCellNum();
            maxColIx = row.getLastCellNum();

            Model model = null;
            List<Model> second = new ArrayList<>();
            List<Model> third = new ArrayList<>();
            for (short colIx = minColIx; colIx < maxColIx; colIx++) {
                Cell cell = row.getCell(colIx);
                String cellVal = getCellVal(cell);
                cells.add(cellVal);

                if (r <= 19) {
                    if (colIx == 0) {
                        model = new Model(cellVal, "10" + (r + 1), "");
                        list.add(model);
                    } else {
                        second.add(new Model(cellVal, model.getCode() + "10" + (colIx + 1), model.getCode()));
                        model.setChildren(second);
                    }
                } else if (r >= 22) {
                    if (colIx == 0) {
                        model = getModel(list, cellVal);
                    } else {
                        third.add(new Model(cellVal, model.getCode() + "10" + (r + 1), model.getCode()));
                        model.setChildren(third);
                    }
                }
            }

            //System.out.println(r);
            //System.out.println(Arrays.deepToString(cells.toArray()));
        }

        output(list);
    }

    public static void hangye() throws IOException, InvalidFormatException {
        List<Model> list = new ArrayList<>();

        String filePath = "d:/行业及主题分类.xlsx";
        InputStream stream = new FileInputStream(filePath);
        Workbook wb = WorkbookFactory.create(stream);

        Sheet sheet = wb.getSheetAt(1);

        Row row = null;
        int totalRow = sheet.getLastRowNum();

        List<String> cells = new ArrayList<>();
        String[] titles = null;

        short minColIx = 0;
        short maxColIx = 0;

        for (int r = 0; r <= sheet.getLastRowNum(); r++) {

            cells = new ArrayList<>();
            row = sheet.getRow(r);

            minColIx = row.getFirstCellNum();
            maxColIx = row.getLastCellNum();

            Model model = null;
            List<Model> second = new ArrayList<>();
            List<Model> third = new ArrayList<>();
            for (short colIx = minColIx; colIx < maxColIx; colIx++) {
                Cell cell = row.getCell(colIx);
                String cellVal = getCellVal(cell);
                cells.add(cellVal);

                if (StringUtil.isNullOrEmpty(cellVal))
                    continue;

                if (r <= 19) {
                    if (colIx == 0) {
                        model = new Model(cellVal, "10" + (r + 1), "");
                        list.add(model);
                    } else {
                        second.add(new Model(cellVal, model.getCode() + "10" + (colIx + 1), model.getCode()));
                        model.setChildren(second);
                    }
                } else if (r >= 22) {
                    if (colIx == 0) {
                        model = getModel(list, cellVal);
                        if (model == null) {
                            throw new IOException("没找到父类:" + cellVal);
                        }
                    } else {
                        third.add(new Model(cellVal, model.getCode() + "10" + (r + 1), model.getCode()));
                        model.setChildren(third);
                    }
                }
            }

            //System.out.println(r);
            //System.out.println(Arrays.deepToString(cells.toArray()));
        }

        output(list);
    }

    private static Model getModel(List<Model> list, String cellVal) {
        for (Model modelSet : list) {
            for (Model model : modelSet.getChildren()) {
                if (model.getName().equals(cellVal)) {
                    return model;
                }
            }
        }
        return null;
    }

    private static void output(List<Model> list) {
        StringBuilder builder = new StringBuilder();
        for (Model model : list) {
            builder.append(model.getName()).append(",").append(model.getCode()).append(",").append(model.getParentCode()).append("\r\n");
            for (Model childmodel : model.getChildren()) {
                builder.append(childmodel.getName()).append(",").append(childmodel.getCode()).append(",").append(childmodel.getParentCode()).append("\r\n");
                if (childmodel.getChildren() != null) {
                    for (Model childmodel2 : childmodel.getChildren()) {
                        builder.append(childmodel2.getName()).append(",").append(childmodel2.getCode()).append(",").append(childmodel2.getParentCode()).append("\r\n");
                    }
                }
            }
        }
        System.out.println(builder.toString());
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
