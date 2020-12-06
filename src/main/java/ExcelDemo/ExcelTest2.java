package ExcelDemo;

import io.netty.util.internal.StringUtil;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ExcelTest2 {
    public static void main(String[] args) throws IOException, InvalidFormatException {

        hangye();
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
                    }else{
                        second.add(new Model(cellVal, model.getCode() + "10" + (colIx + 1), model.getCode()));
                        model.setChildren(second);
                    }
                } else if ( r >= 22) {
                    if (colIx == 0) {
                        model = getModel(list,cellVal);
                    }else{
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

                if(StringUtil.isNullOrEmpty(cellVal))
                    continue;

                if (r <= 19) {
                    if (colIx == 0) {
                        model = new Model(cellVal, "10" + (r + 1), "");
                        list.add(model);
                    }else{
                        second.add(new Model(cellVal, model.getCode() + "10" + (colIx + 1), model.getCode()));
                        model.setChildren(second);
                    }
                } else if ( r >= 22) {
                    if (colIx == 0) {
                        model = getModel(list,cellVal);
                        if(model==null){
                            throw new IOException("没找到父类:" + cellVal);
                        }
                    }else{
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

    private static Model getModel(List<Model> list, String cellVal){
        for (Model modelSet : list){
           for(Model model :  modelSet.getChildren()){
               if(model.getName().equals(cellVal)){
                   return model;
               }
           }
        }
        return null;
    }

    private static void output(List<Model> list){
        StringBuilder builder = new StringBuilder();
        for (Model model : list) {
            builder.append(model.getName()).append(",").append(model.getCode()).append(",").append(model.getParentCode()).append("\r\n");
            for(Model childmodel : model.getChildren()){
                builder.append(childmodel.getName()).append(",").append(childmodel.getCode()).append(",").append(childmodel.getParentCode()).append("\r\n");
                if(childmodel.getChildren()!=null){
                    for(Model childmodel2 : childmodel.getChildren()){
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
