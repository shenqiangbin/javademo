package ExcelDemo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
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
import java.util.ArrayList;
import java.util.List;

import static fileDemo.FileHelper.findFile;

public class Nongkeyuan {
    static WebDriver driver;

    public static void main(String[] args) throws Exception {
        //sonbin();
        //sonbin2();

        //handleTwo();
        handleTwosonbin2();

        System.out.println("over");
    }

    /**
     * 在处理的基础上进行二次处理
     * 获取【作者】是【英文】的文献集合
     */
    private static void handleTwo() throws Exception {
        String filePath = "E:\\指标标引\\2022年维护总结\\问题数据项-700多条.xlsx";

        InputStream stream = new FileInputStream(filePath);
        Workbook wb = WorkbookFactory.create(stream);

        Sheet sheet = wb.getSheetAt(0);

        Row row = null;
        int totalRow = sheet.getLastRowNum();

        List<String> cells = new ArrayList<>();
        String[] titles = null;

        short minColIx = 0;
        short maxColIx = 0;
        List<Model> list = new ArrayList<>();

        int needPdfCount = 0;
        int notNeedPdfCount = 0;

        System.out.println("全英文的作者有：");

        for (int r = 1; r <= sheet.getLastRowNum(); r++) {

            cells = new ArrayList<>();
            row = sheet.getRow(r);

            minColIx = row.getFirstCellNum();
            maxColIx = row.getLastCellNum();

            String title = getCellVal(row.getCell(1));
            String author = getCellVal(row.getCell(4));
            String teacher = getCellVal(row.getCell(5));
            String abs = getCellVal(row.getCell(2));
            String pdf = getCellVal(row.getCell(21));

            if (!StrHelper.isContainChinese(author)) {
                JSONObject object = new JSONObject();
                object.put("title", title);
                object.put("author", author);
                object.put("teacher", teacher);
                System.out.println(object.toString());
            }
        }

    }

    public static void handleTwosonbin2() throws Exception {

        //driver = initChrome();

        List<String> nameList = new ArrayList<>();

        // 基于上一次处理结果，继续处理
        String filePath = "E:\\指标标引\\2022年维护总结\\问题数据项-700多条-new.xlsx";
        String newfilePath = "E:\\指标标引\\2022年维护总结\\问题数据项-700多条-new2.xlsx";

        String resultfilePath = "E:\\指标标引\\2022年维护总结\\sta2023_01_16_10_50_12_english.txt";
        String fileContent = FileHelper.readTxtFile(resultfilePath);

        InputStream stream = new FileInputStream(filePath);
        Workbook wb = WorkbookFactory.create(stream);

        Sheet sheet = wb.getSheetAt(0);

        Row row = null;
        int totalRow = sheet.getLastRowNum();

        List<String> cells = new ArrayList<>();
        String[] titles = null;

        short minColIx = 0;
        short maxColIx = 0;
        List<Model> list = new ArrayList<>();

        int needPdfCount = 0;
        int notNeedPdfCount = 0;

        System.out.println("excel标题和匹配到的标题人工对比一下：");

        for (int r = 1; r <= sheet.getLastRowNum(); r++) {

            cells = new ArrayList<>();
            row = sheet.getRow(r);

            minColIx = row.getFirstCellNum();
            maxColIx = row.getLastCellNum();

            String title = getCellVal(row.getCell(1));
            String author = getCellVal(row.getCell(4));
            String teacher = getCellVal(row.getCell(5));
            String abs = getCellVal(row.getCell(2));
            String pdf = getCellVal(row.getCell(21));

            JSONObject jsonObject = getBy(fileContent, title);

            if (jsonObject == null) {
                continue;
            }

            if (jsonObject.get("文献名字匹配").equals("1")) {
                Cell cell = row.createCell(26);
                cell.setCellValue("有数据_导师匹配");

                boolean allSame = jsonObject.get("title").equals(jsonObject.get("title_db"));
                //System.out.println(jsonObject.get("title") + ":" + jsonObject.get("title_db") + ":完成相同" + allSame);

                CellStyle cellHeadStyle = createCellHeadStyle(wb);

                if (StringUtils.isBlank(abs)) {
                    row.getCell(2).setCellStyle(cellHeadStyle);
                    row.getCell(2).setCellValue(jsonObject.getString("zval"));
                }

                if (StringUtils.isBlank(pdf)) {

                    needPdfCount++;

                    cell = row.createCell(21);
                    cell.setCellStyle(cellHeadStyle);
                    //cell.setCellValue(jsonObject.getString("zfile"));

                    String name = findFileMostLike("E:\\指标标引\\2022年维护总结\\topdf-english", title, author);
                    cell.setCellValue(name);

                    if(nameList.contains(name)){
                        System.out.println("包含了name:" + name);
                    }else{
                        nameList.add(name);
                    }
//
                    //下载文献
                   // System.out.println(title + ":" + author);
                    //downloadFile(title);
                } else {
                    notNeedPdfCount++;
                }

            }

            //System.out.println(r);
            //System.out.println(Arrays.deepToString(cells.toArray()));
        }
        OutputStream ops = new FileOutputStream(newfilePath);
        wb.write(ops);
        ops.flush();
        ops.close();
        wb.close();

        System.out.println("需要处理的pdf：" + needPdfCount + " 已经有的pdf：" + notNeedPdfCount);
    }


    public static void sonbin2() throws Exception {

        //driver = initChrome();

        List<String> nameList = new ArrayList<>();

        String filePath = "E:\\指标标引\\2022年维护总结\\问题数据项-700多条.xlsx";
        String newfilePath = "E:\\指标标引\\2022年维护总结\\问题数据项-700多条-new.xlsx";

        //String resultfilePath = "E:\\指标标引\\2022年维护总结\\sta2023_01_13_10_12_07.txt";
        String resultfilePath = "E:\\指标标引\\2022年维护总结\\sta2023_01_13_16_32_22.txt";
        String fileContent = FileHelper.readTxtFile(resultfilePath);

        InputStream stream = new FileInputStream(filePath);
        Workbook wb = WorkbookFactory.create(stream);

        Sheet sheet = wb.getSheetAt(0);

        Row row = null;
        int totalRow = sheet.getLastRowNum();

        List<String> cells = new ArrayList<>();
        String[] titles = null;

        short minColIx = 0;
        short maxColIx = 0;
        List<Model> list = new ArrayList<>();

        int needPdfCount = 0;
        int notNeedPdfCount = 0;

        for (int r = 1; r <= sheet.getLastRowNum(); r++) {

            cells = new ArrayList<>();
            row = sheet.getRow(r);

            minColIx = row.getFirstCellNum();
            maxColIx = row.getLastCellNum();

            String title = getCellVal(row.getCell(1));
            String author = getCellVal(row.getCell(4));
            String teacher = getCellVal(row.getCell(5));
            String abs = getCellVal(row.getCell(2));
            String pdf = getCellVal(row.getCell(21));

            JSONObject jsonObject = getBy(fileContent, title);

            if (jsonObject.get("文献名字匹配").equals("1")) {
                Cell cell = row.createCell(26);
                cell.setCellValue("有数据");

                CellStyle cellHeadStyle = createCellHeadStyle(wb);

                if (StringUtils.isBlank(abs)) {
                    row.getCell(2).setCellStyle(cellHeadStyle);
                    row.getCell(2).setCellValue(jsonObject.getString("zval"));
                }

                if (StringUtils.isBlank(pdf)) {

                    needPdfCount++;

                    cell = row.createCell(21);
                    cell.setCellStyle(cellHeadStyle);
                    //cell.setCellValue(jsonObject.getString("zfile"));

                    String name = findFileMostLike("E:\\指标标引\\2022年维护总结\\Downloads", title, author);
                    cell.setCellValue(name);

                    if (nameList.contains(name)) {
                        System.out.println("包含了name:" + name);
                    } else {
                        nameList.add(name);
                    }

                    System.out.println(title + ":" + author + ":" + name);
                    //downloadFile(title);
                } else {

                    notNeedPdfCount++;

                }

            }

            //System.out.println(r);
            //System.out.println(Arrays.deepToString(cells.toArray()));
        }
        OutputStream ops = new FileOutputStream(newfilePath);
        wb.write(ops);
        ops.flush();
        ops.close();
        wb.close();

        System.out.println("需要处理的pdf：" + needPdfCount + " 已经有的pdf：" + notNeedPdfCount);
    }

    public static String findFileMostLike(String dir, String name, String author) throws IOException {
        File file = new File(dir);
        if (!file.exists()) {
            throw new FileNotFoundException("目录不存在");
        }
        File[] files = file.listFiles();

        float topLike = 0;
        File topLikeFile = null;

        for (int i = 0; i < files.length; i++) {

            String filename = files[i].getName();
            float socre = StrHelper.getSimilarityRatio(filename, name + "_" + author);
            if (socre > topLike) {
                topLike = socre;
                topLikeFile = files[i];
            }
        }

//        if (!topLikeFile.getName().contains(author)) {
//            throw new RuntimeException("不包含作者信息");
//        }

        String newPath = "E:\\指标标引\\2022年维护总结\\toPdf\\" + topLikeFile.getName();
        FileHelper.copyFile(topLikeFile, new File(newPath));

        return topLikeFile.getName().replace(".caj", ".pdf");
    }

    public static JSONObject getBy(String content, String title) {
        for (String line : content.split("\r\n")) {
            JSONObject obj = JSON.parseObject(line);
            if (obj.get("title").equals(title)) {
                return obj;
            }
        }
        return null;
    }

    static boolean okStart = false;

    static void downloadFile(String title) throws InterruptedException {

//        if (!okStart) {
//
//            if (title.equals("城乡一体化下空间农业关键技术研究与应用探索")) {
//                okStart = true;
//            }
//
//            return;
//        }

        String path = "https://kns.cnki.net/kns8/DefaultResult/Index?dbcode=CFLS&kw=123&korder=SU";
        path = path.replace("123", title);
        driver.get(path);

        Thread.sleep(60000);

        List<WebElement> imgs = driver.findElements(By.className("downloadlink"));
        for (WebElement img : imgs) {
            String s = img.toString();
            img.click();
        }
        //$(".result-table-list tr td.operat a.downloadlink").attr('href')
    }

    static WebDriver initChrome() {
         /*
        https://chromedriver.storage.googleapis.com/index.html?path=73.0.3683.68/
        * */

        System.setProperty("webdriver.chrome.driver", "c:/chromedriver_win32/chromedriver.exe");

        ChromeOptions chromeOptions = new ChromeOptions();
        //chromeOptions.addArguments("--headless");
        //chromeOptions.addArguments("--window-size=1280,768");

        WebDriver driver = new ChromeDriver(chromeOptions);

        driver.get("http://www.sqber.com");
        //driver.get("http://10.170.2.161:8161/models");
        //driver.get("http://www.baidu.com");
        driver.manage().window().maximize();

        return driver;
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

    public static void sonbin() throws Exception {

        String filePath = "E:\\指标标引\\2022 年维护总结\\问题数据项-700多条.xlsx";
        InputStream stream = new FileInputStream(filePath);
        Workbook wb = WorkbookFactory.create(stream);

        Sheet sheet = wb.getSheetAt(0);

        Row row = null;
        int totalRow = sheet.getLastRowNum();

        List<String> cells = new ArrayList<>();
        String[] titles = null;

        short minColIx = 0;
        short maxColIx = 0;
        List<Model> list = new ArrayList<>();

        for (int r = 1; r <= sheet.getLastRowNum(); r++) {

            cells = new ArrayList<>();
            row = sheet.getRow(r);

            minColIx = row.getFirstCellNum();
            maxColIx = row.getLastCellNum();


            String title = getCellVal(row.getCell(1));
            String author = getCellVal(row.getCell(4));
            String teacher = getCellVal(row.getCell(5));


            JSONObject object = new JSONObject();
            object.put("title", title);
            object.put("author", author);
            object.put("teacher", teacher);
            System.out.println(object.toString());
            //System.out.println(r);
            //System.out.println(Arrays.deepToString(cells.toArray()));
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
