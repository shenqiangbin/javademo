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


        String txt = "部门名称\t部门编码\t委办局内机构名称\t委办局内机构编码\t主干业务名称\t主干业务编码\t子业务名称\t子业务编码\t业务责任（三定业务描述）\t一级主题分类\t一级行业分类\t二级主题分类\t二级行业分类\t三级主题分类\t三级行业分类\t系统名称\t系统编码\t系统类型\t垂直系统提供数据类型\t数据库名称\t数据库类型\t网络环境\t建设时间\t建设厂商\t厂商联系人\t厂商联系方式";
        txt = "系统编码\t表名称\t表注释\t表编码\t资源名称\t资源摘要\t业务或技术\t表分类\t业务关键字\t是否提供增量机制\t增量判断字段\t增量判断条件详细描述\t总体记录数量\t最近一次增量记录数\t是否逻辑删除\t更新频率\t更新时间\t是否手工数据维护\t数据维护单位\t数据维护责任人\t数据维护责任人联系方法";
        txt = "表编码\t表名称\t表注释\t字段名称\t字段注释\t业务或技术\t字段编码\t信息项名称\t业务描述\t字段分类\t共享类型\t共享条件\t开放类型\t开放条件\t数据业务类型\t枚举值解释\t逻辑联系字段\t是否搜索内容\t是否搜索结果展示\t数据技术类型\t是否业务主键\t是否技术主键\t是否外键\t是否可空\t精度\t是否敏感\t是否支持计算\t小数位\t数据长度\t初始值\t数值比例\t规格单位\t是否代码字段\t是否已经参考行业标准\t代码行业标准编号\t代码范围\t是否搜索核心字段\t是否搜索概览字段";
        //txt = "系统编码\t表名称\t表注释\t表编码\t资源名称\t资源摘要\t业务或技术\t表分类\t业务关键字\t是否提供增量机制\t增量判断字段\t增量判断条件详细描述\t总体记录数量\t最近一次增量记录数\t是否逻辑删除\t更新频率\t更新时间\t是否手工数据维护\t数据维护单位\t数据维护责任人\t数据维护责任人联系方法";
        //txt = "表编码\t表名称\t表注释\t字段名称\t字段注释\t数据业务类型\t枚举值解释\t逻辑联系字段\t是否搜索内容\t是否搜索结果展示\t数值比例\t规格单位\t是否搜索核心字段\t是否搜索概览字段\n";
        String[] arr = txt.split("\t");
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            String item = arr[i];
            String s = "\"代码行业标准编号\" varchar(500) COLLATE \"default\"".replace("代码行业标准编号", item);
            if (i != arr.length - 1) {
                s = s + ",";
            }
            builder.append("\r\n");
            builder.append(s);
        }

        System.out.println(builder.toString());

        String filePath = "d:/data/1.xlsx";
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
