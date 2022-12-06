package ExcelDemo;

/**
 * @author SongBin on 2019/9/23.
 */
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * Excel 相关操作类(小数据量写入<=60000)
 */
public class Excel2007Utils {

    private static final int DEFAULT_COLUMN_SIZE = 30;

    /**
     * 断言Excel文件写入之前的条件
     *
     * @param directory 目录
     * @param fileName  文件名
     * @return file
     * @throws IOException
     */
    private static File assertFile(String directory, String fileName) throws IOException {
        File tmpFile = new File(directory + File.separator + fileName + ".xlsx");
        if (tmpFile.exists()) {
            if (tmpFile.isDirectory()) {
                throw new IOException("File '" + tmpFile + "' exists but is a directory");
            }
            if (!tmpFile.canWrite()) {
                throw new IOException("File '" + tmpFile + "' cannot be written to");
            }
        } else {
            File parent = tmpFile.getParentFile();
            if (parent != null) {
                if (!parent.mkdirs() && !parent.isDirectory()) {
                    throw new IOException("Directory '" + parent + "' could not be created");
                }
            }
        }
        return tmpFile;
    }

    /**
     * 日期转化为字符串,格式为yyyy-MM-dd HH:mm:ss
     */
    private static String getCnDate(Date date) {
        String format = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    /**
     * Excel 导出，POI实现
     *
     * @param fileName    文件名
     * @param sheetName   sheet页名称
     * @param columnNames 表头列表名
     * @param sheetTitle  sheet页Title
     * @param objects     目标数据集
     */
    public static File writeExcel(String directory, String fileName, String sheetName, List<String> columnNames,
                                  String sheetTitle, List<List<Object>> objects, boolean append) throws IOException {
        File tmpFile = assertFile(directory, fileName);
        return exportExcel(tmpFile, sheetName, columnNames, sheetTitle, objects, append);
    }

    /**
     * Excel 导出，POI实现，先写入Excel标题，与writeExcelData配合使用
     * 先使用writeExcelTitle再使用writeExcelData
     *
     * @param directory   目录
     * @param fileName    文件名
     * @param sheetName   sheetName
     * @param columnNames 列名集合
     * @param sheetTitle  表格标题
     * @param append      是否在现有的文件追加
     * @return file
     * @throws IOException
     */
    public static File writeExcelTitle(String directory, String fileName, String sheetName, List<String> columnNames,
                                       String sheetTitle, boolean append) throws IOException {
        File tmpFile = assertFile(directory, fileName);
        return exportExcelTitle(tmpFile, sheetName, columnNames, sheetTitle, append);
    }

    /**
     * Excel 导出，POI实现，写入Excel数据行列，与writeExcelTitle配合使用
     * 先使用writeExcelTitle再使用writeExcelData
     *
     * @param directory 目录
     * @param fileName  文件名
     * @param sheetName sheetName
     * @param objects   数据信息
     * @return file
     * @throws IOException
     */
    public static File writeExcelData(String directory, String fileName, String sheetName, List<List<Object>> objects)
            throws IOException {
        File tmpFile = assertFile(directory, fileName);
        return exportExcelData(tmpFile, sheetName, objects);
    }

    /**
     * 导出字符串数据
     *
     * @param file        文件名
     * @param columnNames 表头
     * @param sheetTitle  sheet页Title
     * @param append      是否追加写文件
     * @return file
     */
    private static File exportExcelTitle(File file, String sheetName, List<String> columnNames,
                                         String sheetTitle, boolean append) throws IOException {
        // 声明一个工作薄
        Workbook workBook;
        if (file.exists() && append) {
            workBook = new XSSFWorkbook(new FileInputStream(file));
        } else {
            workBook = new XSSFWorkbook();
        }
        Map<String, CellStyle> cellStyleMap = styleMap(workBook);
        // 表头样式
        CellStyle headStyle = cellStyleMap.get("head");
        // 生成一个表格
        Sheet sheet = workBook.getSheet(sheetName);
        if (sheet == null) {
            sheet = workBook.createSheet(sheetName);
        }
        //最新Excel列索引,从0开始
        int lastRowIndex = sheet.getLastRowNum();
        if (lastRowIndex > 0) {
            lastRowIndex++;
        }
        // 设置表格默认列宽度
        sheet.setDefaultColumnWidth(DEFAULT_COLUMN_SIZE);
        // 合并单元格
        sheet.addMergedRegion(new CellRangeAddress(lastRowIndex, lastRowIndex, 0, columnNames.size() - 1));
        // 产生表格标题行
        Row rowMerged = sheet.createRow(lastRowIndex);
        lastRowIndex++;
        Cell mergedCell = rowMerged.createCell(0);
        mergedCell.setCellStyle(headStyle);
        mergedCell.setCellValue(new XSSFRichTextString(sheetTitle));
        // 产生表格表头列标题行
        Row row = sheet.createRow(lastRowIndex);
        for (int i = 0; i < columnNames.size(); i++) {
            Cell cell = row.createCell(i);
            cell.setCellStyle(headStyle);
            RichTextString text = new XSSFRichTextString(columnNames.get(i));
            cell.setCellValue(text);
        }
        try {
            OutputStream ops = new FileOutputStream(file);
            workBook.write(ops);
            ops.flush();
            ops.close();
        } catch (IOException e) {
        }
        return file;
    }

    /**
     * 导出字符串数据
     *
     * @param file    文件名
     * @param objects 目标数据
     * @return
     */
    private static File exportExcelData(File file, String sheetName, List<List<Object>> objects) throws IOException {
        // 声明一个工作薄
        Workbook workBook;
        if (file.exists()) {
            workBook = new XSSFWorkbook(new FileInputStream(file));
        } else {
            workBook = new XSSFWorkbook();
        }

        Map<String, CellStyle> cellStyleMap = styleMap(workBook);
        // 正文样式
        CellStyle contentStyle = cellStyleMap.get("content");
        //正文整数样式
        CellStyle contentIntegerStyle = cellStyleMap.get("integer");
        //正文带小数整数样式
        CellStyle contentDoubleStyle = cellStyleMap.get("double");
        // 生成一个表格
        Sheet sheet = workBook.getSheet(sheetName);
        if (sheet == null) {
            sheet = workBook.createSheet(sheetName);
        }
        //最新Excel列索引,从0开始
        int lastRowIndex = sheet.getLastRowNum();
        if (lastRowIndex > 0) {
            lastRowIndex++;
        }
        // 设置表格默认列宽度
        sheet.setDefaultColumnWidth(DEFAULT_COLUMN_SIZE);
        // 遍历集合数据,产生数据行,前两行为标题行与表头行
        for (List<Object> dataRow : objects) {
            Row row = sheet.createRow(lastRowIndex);
            lastRowIndex++;
            for (int j = 0; j < dataRow.size(); j++) {
                Cell contentCell = row.createCell(j);
                Object dataObject = dataRow.get(j);
                if (dataObject != null) {
                    if (dataObject instanceof Integer) {
                        contentCell.setCellStyle(contentIntegerStyle);
                        contentCell.setCellValue(Integer.parseInt(dataObject.toString()));
                    } else if (dataObject instanceof Double) {
                        contentCell.setCellStyle(contentDoubleStyle);
                        contentCell.setCellValue(Double.parseDouble(dataObject.toString()));
                    } else if (dataObject instanceof Long && dataObject.toString().length() == 13) {
                        contentCell.setCellStyle(contentStyle);
                        contentCell.setCellValue(getCnDate(new Date(Long.parseLong(dataObject.toString()))));
                    } else if (dataObject instanceof Date) {
                        contentCell.setCellStyle(contentStyle);
                        contentCell.setCellValue(getCnDate((Date) dataObject));
                    } else {
                        contentCell.setCellStyle(contentStyle);
                        contentCell.setCellValue(dataObject.toString());
                    }
                } else {
                    contentCell.setCellStyle(contentStyle);
                    // 设置单元格内容为字符型
                    contentCell.setCellValue("");
                }
            }
        }
        try {
            OutputStream ops = new FileOutputStream(file);
            workBook.write(ops);
            ops.flush();
            ops.close();
        } catch (IOException e) {
        }
        return file;
    }

    /**
     * 导出字符串数据
     *
     * @param file        文件名
     * @param columnNames 表头
     * @param sheetTitle  sheet页Title
     * @param objects     目标数据
     * @param append      是否追加写文件
     * @return
     */
    private static File exportExcel(File file, String sheetName, List<String> columnNames,
                                    String sheetTitle, List<List<Object>> objects, boolean append) throws IOException {
        // 声明一个工作薄
        Workbook workBook;
        if (file.exists() && append) {
            // 声明一个工作薄
            workBook = new XSSFWorkbook(new FileInputStream(file));
        } else {
            workBook = new XSSFWorkbook();
        }
        Map<String, CellStyle> cellStyleMap = styleMap(workBook);
        // 表头样式
        CellStyle headStyle = cellStyleMap.get("head");
        // 正文样式
        CellStyle contentStyle = cellStyleMap.get("content");
        //正文整数样式

        CellStyle contentIntegerStyle = cellStyleMap.get("integer");
        //正文带小数整数样式
        CellStyle contentDoubleStyle = cellStyleMap.get("double");
        // 生成一个表格
        Sheet sheet = workBook.getSheet(sheetName);
        if (sheet == null) {
            sheet = workBook.createSheet(sheetName);
        }
        //最新Excel列索引,从0开始
        int lastRowIndex = sheet.getLastRowNum();
        if (lastRowIndex > 0) {
            lastRowIndex++;
        }
        // 设置表格默认列宽度
        sheet.setDefaultColumnWidth(DEFAULT_COLUMN_SIZE);
        // 合并单元格
        sheet.addMergedRegion(new CellRangeAddress(lastRowIndex, lastRowIndex, 0, columnNames.size() - 1));
        // 产生表格标题行
        Row rowMerged = sheet.createRow(lastRowIndex);
        lastRowIndex++;
        Cell mergedCell = rowMerged.createCell(0);
        mergedCell.setCellStyle(headStyle);
        mergedCell.setCellValue(new XSSFRichTextString(sheetTitle));
        // 产生表格表头列标题行
        Row row = sheet.createRow(lastRowIndex);
        lastRowIndex++;
        for (int i = 0; i < columnNames.size(); i++) {
            Cell cell = row.createCell(i);
            cell.setCellStyle(headStyle);
            RichTextString text = new XSSFRichTextString(columnNames.get(i));
            cell.setCellValue(text);
        }
        // 遍历集合数据,产生数据行,前两行为标题行与表头行
        for (List<Object> dataRow : objects) {
            row = sheet.createRow(lastRowIndex);
            lastRowIndex++;
            for (int j = 0; j < dataRow.size(); j++) {
                Cell contentCell = row.createCell(j);
                Object dataObject = dataRow.get(j);
                if (dataObject != null) {
                    if (dataObject instanceof Integer) {
                        contentCell.setCellType(XSSFCell.CELL_TYPE_NUMERIC);
                        contentCell.setCellStyle(contentIntegerStyle);
                        contentCell.setCellValue(Integer.parseInt(dataObject.toString()));
                    } else if (dataObject instanceof Double) {
                        contentCell.setCellType(XSSFCell.CELL_TYPE_NUMERIC);
                        contentCell.setCellStyle(contentDoubleStyle);
                        contentCell.setCellValue(Double.parseDouble(dataObject.toString()));
                    } else if (dataObject instanceof Long && dataObject.toString().length() == 13) {
                        contentCell.setCellType(XSSFCell.CELL_TYPE_STRING);
                        contentCell.setCellStyle(contentStyle);
                        contentCell.setCellValue(getCnDate(new Date(Long.parseLong(dataObject.toString()))));
                    } else if (dataObject instanceof Date) {
                        contentCell.setCellType(XSSFCell.CELL_TYPE_STRING);
                        contentCell.setCellStyle(contentStyle);
                        contentCell.setCellValue(getCnDate((Date) dataObject));
                    } else {
                        contentCell.setCellType(XSSFCell.CELL_TYPE_STRING);
                        contentCell.setCellStyle(contentStyle);
                        contentCell.setCellValue(dataObject.toString());
                    }
                } else {
                    contentCell.setCellStyle(contentStyle);
                    // 设置单元格内容为字符型
                    contentCell.setCellValue("");
                }
            }
        }
        try {
            OutputStream ops = new FileOutputStream(file);
            workBook.write(ops);
            ops.flush();
            ops.close();
        } catch (IOException e) {
        }
        return file;
    }


    public interface ExcelData{
        String getSheetName();
        long getDataCount();
        List<String> getColumnNames();
        Object getDataByIndex(int dataIndex,int colIndex);
    }

    public static void exportExcelToWeb(HttpServletRequest request, HttpServletResponse response, ExcelData excelData) throws IOException {
        String name = excelData.getSheetName();
        if (request.getHeader("User-Agent") != null) {
            String agent = request.getHeader("User-Agent").toLowerCase();
            //识别IE浏览器
            if (agent != null && (agent.indexOf("msie") != -1 ||
                    (agent.indexOf("rv") != -1 && agent.indexOf("firefox") == -1))) {
                name = URLEncoder.encode(name, "UTF-8");
            }else if(agent != null && agent.indexOf("firefox") != -1){
                name = new String(name.getBytes("UTF-8"), "ISO-8859-1");
            }
            else {
                //name = new String(name.getBytes("UTF-8"), "ISO-8859-1");
                name = URLEncoder.encode(name, "UTF-8");
            }
        }
        //response.setContentLength(length);
        response.setHeader("Content-Type", "application/octet-stream;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment; filename=" + name);
        exportExcelTo(excelData,response.getOutputStream());
    }

    private static void exportExcelTo(ExcelData excelData,OutputStream outputStream) throws IOException {
        // 声明一个工作薄
        Workbook workBook = new XSSFWorkbook();
        Map<String, CellStyle> cellStyleMap = styleMap(workBook);
        // 表头样式
        CellStyle headStyle = cellStyleMap.get("head");
        // 正文样式
        CellStyle contentStyle = cellStyleMap.get("content");
        //正文整数样式

        CellStyle contentIntegerStyle = cellStyleMap.get("integer");
        //正文带小数整数样式
        CellStyle contentDoubleStyle = cellStyleMap.get("double");
        // 生成一个表格
        Sheet sheet = workBook.getSheet(excelData.getSheetName());
        if (sheet == null) {
            sheet = workBook.createSheet(excelData.getSheetName());
        }
        //最新Excel列索引,从0开始
        int lastRowIndex = sheet.getLastRowNum();
        if (lastRowIndex > 0) {
            lastRowIndex++;
        }
        // 设置表格默认列宽度
        sheet.setDefaultColumnWidth(DEFAULT_COLUMN_SIZE);
        // 合并单元格
        //sheet.addMergedRegion(new CellRangeAddress(lastRowIndex, lastRowIndex, 0, excelData.getColumnNames().size() - 1));
        // 产生表格标题行
        //Row rowMerged = sheet.createRow(lastRowIndex);
        //lastRowIndex++;
        //Cell mergedCell = rowMerged.createCell(0);
        //mergedCell.setCellStyle(headStyle);
        //mergedCell.setCellValue(new XSSFRichTextString(excelData.getSheetName()));
        // 产生表格表头列标题行
        Row row = sheet.createRow(lastRowIndex);
        List<String> columnNames = excelData.getColumnNames();
        lastRowIndex++;
        for (int i = 0; i < columnNames.size(); i++) {
            Cell cell = row.createCell(i);
            cell.setCellStyle(headStyle);
            RichTextString text = new XSSFRichTextString(columnNames.get(i));
            cell.setCellValue(text);
        }
        // 遍历集合数据,产生数据行,前两行为标题行与表头行
        for (int ji =0; ji< excelData.getDataCount(); ji++ ) {
            row = sheet.createRow(lastRowIndex);
            lastRowIndex++;
            for (int j = 0; j < columnNames.size(); j++) {
                Cell contentCell = row.createCell(j);
                Object dataObject = excelData.getDataByIndex(ji,j);
                if (dataObject != null) {
                    if (dataObject instanceof Integer) {
                        contentCell.setCellType(XSSFCell.CELL_TYPE_NUMERIC);
                        contentCell.setCellStyle(contentIntegerStyle);
                        contentCell.setCellValue(Integer.parseInt(dataObject.toString()));
                    } else if (dataObject instanceof Double) {
                        contentCell.setCellType(XSSFCell.CELL_TYPE_NUMERIC);
                        contentCell.setCellStyle(contentDoubleStyle);
                        contentCell.setCellValue(Double.parseDouble(dataObject.toString()));
                    } else if (dataObject instanceof Long && dataObject.toString().length() == 13) {
                        contentCell.setCellType(XSSFCell.CELL_TYPE_STRING);
                        contentCell.setCellStyle(contentStyle);
                        contentCell.setCellValue(getCnDate(new Date(Long.parseLong(dataObject.toString()))));
                    } else if (dataObject instanceof Date) {
                        contentCell.setCellType(XSSFCell.CELL_TYPE_STRING);
                        contentCell.setCellStyle(contentStyle);
                        contentCell.setCellValue(getCnDate((Date) dataObject));
                    } else {
                        contentCell.setCellType(XSSFCell.CELL_TYPE_STRING);
                        contentCell.setCellStyle(contentStyle);
                        contentCell.setCellValue(dataObject.toString());
                    }
                } else {
                    contentCell.setCellStyle(contentStyle);
                    // 设置单元格内容为字符型
                    contentCell.setCellValue("");
                }
            }
        }
        workBook.write(outputStream);
        //outputStream.flush();
        //outputStream.close();
    }

    /**
     * 创建单元格表头样式
     *
     * @param workbook 工作薄
     */
    private static CellStyle createCellHeadStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        // 设置边框样式
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        //设置对齐样式
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        // 生成字体
        Font font = workbook.createFont();
        // 表头样式
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        font.setFontHeightInPoints((short) 12);
        font.setBold(true);
        // 把字体应用到当前的样式
        style.setFont(font);
        return style;
    }

    /**
     * 创建单元格正文样式
     *
     * @param workbook 工作薄
     */
    private static CellStyle createCellContentStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        // 设置边框样式
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        //设置对齐样式
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        // 生成字体
        Font font = workbook.createFont();
        // 正文样式
        style.setFillPattern(FillPatternType.NO_FILL);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        font.setBold(false);
        // 把字体应用到当前的样式
        style.setFont(font);
        return style;
    }

    /**
     * 单元格样式(Integer)列表
     */
    private static CellStyle createCellContent4IntegerStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        // 设置边框样式
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        //设置对齐样式
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        // 生成字体
        Font font = workbook.createFont();
        // 正文样式
        style.setFillPattern(FillPatternType.NO_FILL);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        font.setBold(false);
        // 把字体应用到当前的样式
        style.setFont(font);
        style.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));//数据格式只显示整数
        return style;
    }

    /**
     * 单元格样式(Double)列表
     */
    private static CellStyle createCellContent4DoubleStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        // 设置边框样式
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        //设置对齐样式
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        // 生成字体
        Font font = workbook.createFont();
        // 正文样式
        style.setFillPattern(FillPatternType.NO_FILL);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        font.setBold(false);
        // 把字体应用到当前的样式
        style.setFont(font);
        style.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0.00"));//保留两位小数点
        return style;
    }

    /**
     * 单元格样式列表
     */
    private static Map<String, CellStyle> styleMap(Workbook workbook) {
        Map<String, CellStyle> styleMap = new LinkedHashMap<>();
        styleMap.put("head", createCellHeadStyle(workbook));
        styleMap.put("content", createCellContentStyle(workbook));
        styleMap.put("integer", createCellContent4IntegerStyle(workbook));
        styleMap.put("double", createCellContent4DoubleStyle(workbook));
        return styleMap;
    }

    public static void main(String[] args) throws IOException {
        String sheetName = "测试Excel格式";
        String sheetTitle = "测试Excel格式";
        List<String> columnNames = new LinkedList<>();
        columnNames.add("日期-String");
        columnNames.add("日期-Date");
        columnNames.add("时间戳-Long");
        columnNames.add("客户编码");
        columnNames.add("整数");
        columnNames.add("带小数的正数");

        //写入标题--第二种方式(先写入标题，再再写入数据)
        Excel2007Utils.writeExcelTitle("E:\\temp", "a", sheetName, columnNames, sheetTitle, false);

        List<List<Object>> objects = new LinkedList<>();
        for (int i = 0; i < 1000; i++) {
            List<Object> dataA = new LinkedList<>();
            dataA.add("2016-09-05 17:27:25");
            dataA.add(new Date(1451036631012L));
            dataA.add(1451036631012L);
            dataA.add("000628");
            dataA.add(i);
            dataA.add(1.323 + i);
            objects.add(dataA);
        }
        try {
            //写入数据--第二种方式(先写入标题，再再写入数据)
            Excel2007Utils.writeExcelData("E:\\temp", "b", sheetName, objects);

            //标题和数据一起写入--第一种方式
            Excel2007Utils.writeExcel("E:\\temp", "a", sheetName, columnNames, sheetTitle, objects, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}