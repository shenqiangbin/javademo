package ExcelDemo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import dbmgr.mySqlAccess.MySqlHelper;
import fileDemo.FileHelper;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.*;
import java.util.*;

public class Nongkeyuan_class {
    static MySqlHelper mySqlHelper = new MySqlHelper(new HikariDataSource(getConfig()));
    static HashMap<Object, Object> map = new LinkedHashMap();
    static List<List<Object>> objects = new ArrayList<>();



    /**
     * select count(*) from nv_normal_value
     * SELECT * from nv_class_standard where ClassCode like '4590/%'
     */
    public static void main(String[] args) throws Exception {

        map.put("4590","全球地理区划规范");
        map.put("4591","全球");

        handle();

        saveToExcel();
        System.out.println("over");
    }


    private static void handle() throws Exception {
        String str = "SELECT id,className,classCode,parentID from nv_class_standard where ParentID = 4591 order by className desc";
        List<LinkedHashMap<String, Object>> linkedHashMaps = mySqlHelper.simpleQuery(str, null);
        for(LinkedHashMap<String,Object> item : linkedHashMaps){
            Object id = item.get("id");
            Object className = item.get("className");
            Object classCode = item.get("classCode");
            Object parentID = item.get("parentID");
            map.put(id.toString(),className);

            if(hasChildren(id)){
                handleChildren(id);
            }else{
                wirteToExcel(classCode);
            }
            System.out.println("other");
        }
    }


    private static boolean hasChildren(Object id) throws Exception {
        String str = "SELECT id,className,classCode,parentID from nv_class_standard where parentID = ?";
        List<LinkedHashMap<String, Object>> linkedHashMaps = mySqlHelper.simpleQuery(str, new Object[]{id});
        if(linkedHashMaps == null || linkedHashMaps.size() == 0){
            return false;
        }
        return true;
    }

    private static void handleChildren(Object nodeid) throws Exception {
        String str = "SELECT id,className,classCode,parentID from nv_class_standard where parentID = ? order by className desc";
        List<LinkedHashMap<String, Object>> linkedHashMaps = mySqlHelper.simpleQuery(str, new Object[]{nodeid});
        for(LinkedHashMap<String,Object> item : linkedHashMaps) {
            Object id = item.get("id");
            Object className = item.get("className");
            Object classCode = item.get("classCode");
            Object parentID = item.get("parentID");
            map.put(id.toString(),className);

            if(hasChildren(id)){
                handleChildren(id);
            }else{
                wirteToExcel(classCode);
            }
        }
    }

    private static void wirteToExcel(Object classCode) throws Exception {
        String[] arr = classCode.toString().split("/");
        List<Object> result = new ArrayList<>();
        for(Object item : arr){
            Object name = map.get((Object)item);
            result.add(name);
            result.add(getEnName(name));
        }

        objects.add(result);
    }

    private static String getEnName(Object name) throws Exception {
        String str = "select NormalNameCn from nv_normal_value where NormalName = ? limit 10";
        List<LinkedHashMap<String, Object>> linkedHashMaps = mySqlHelper.simpleQuery(str, new Object[]{name});
        if(linkedHashMaps == null || linkedHashMaps.size() ==0){
            return "-";
        }
        for(LinkedHashMap<String,Object> item : linkedHashMaps) {
            String enName = item.get("NormalNameCn").toString();
            return enName;
        }
        return "-";
    }

    private static void saveToExcel() throws IOException {

        String sheetName = "国家数据";
        String sheetTitle = "国家数据";
        List<String> columnNames = new LinkedList<>();
        columnNames.add("一级（中）");
        columnNames.add("一级（英）");
        columnNames.add("二级（中）");
        columnNames.add("二级（英）");
        columnNames.add("三级（中）");
        columnNames.add("三级（英）");
        columnNames.add("四级（中）");
        columnNames.add("四级（英）");
        columnNames.add("五级（中）");
        columnNames.add("五级（英）");
        columnNames.add("六级（中）");
        columnNames.add("六级（英）");
        columnNames.add("七级（中）");
        columnNames.add("七级（英）");
        columnNames.add("八级（中）");
        columnNames.add("八级（英）");

        Excel2007Utils.writeExcel("E:\\temp", "国家数据", sheetName, columnNames, sheetTitle, objects, false);

    }



    private static CellStyle createCellHeadStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        // 设置边框样式
//        style.setBorderBottom(BorderStyle.THIN);
//        style.setBorderLeft(BorderStyle.THIN);
//        style.setBorderRight(BorderStyle.THIN);
//        style.setBorderTop(BorderStyle.THIN);
        //设置对齐样式
//        style.setAlignment(HorizontalAlignment.CENTER);
//        style.setVerticalAlignment(VerticalAlignment.CENTER);
        // 生成字体
        // Font font = workbook.createFont();
        // 表头样式
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
//        font.setFontHeightInPoints((short) 12);
//        font.setBold(true);
        // 把字体应用到当前的样式
        //style.setFont(font);
        return style;
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


    public static HikariConfig getConfig() {

        HikariConfig config = new HikariConfig();

        //config.setJdbcUrl("jdbc:mysql://:3306/bd?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=UTC");
        config.setJdbcUrl("jdbc:mysql://192.168.52.64:3306/tablebigdata-nongkeyuan-dev?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=UTC");
        config.setUsername("root");
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setPassword("123456");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmptCacheSqlLimit", "2048");

        return config;
    }

}
