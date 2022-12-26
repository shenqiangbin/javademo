package ExcelDemo;

import PDF.PathUtil;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import dbmgr.mySqlAccess.MySqlHelper;
import de.siegmar.fastcsv.reader.CsvReader;
import de.siegmar.fastcsv.reader.CsvRow;
import io.netty.util.internal.StringUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvMapReader;
import org.supercsv.io.ICsvMapReader;
import org.supercsv.prefs.CsvPreference;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class ExcelTest2 {
    public static void main(String[] args) throws Exception {

        //hangye();
        //test();
        //sonbin();
        // 把 EXCEL 按 Sheet 页进行拆分
        //test3();
        test6();
    }

    public static void test6() throws IOException, InvalidFormatException {
        //String file = "D:\\code\\TPI\\大数据产品\\光明国际\\农业食品词语.xlsx";
        String file = "D:\\code\\TPI\\大数据产品\\光明国际\\热词.xlsx";
        List<Object> zhibiao = new ArrayList<>();

        CommonExcel commonExcel = new CommonExcel(file, new ICommonResultHandler() {

            @Override
            public boolean validateTilte(List<String> titles) {
                return true;
            }

            @Override
            public String store(List<String> cellVals, List<String> titles) {
                //System.out.println(cellVals);
                //System.out.println(titles);
                for (int i = 0; i < 30; i++) {
                    try {
                        if (cellVals.get(i) == null)
                            continue;
                    } catch (IndexOutOfBoundsException e) {
                        continue;
                    }
                    String val = cellVals.get(i).trim();
                    if (!StringUtil.isNullOrEmpty(val)) {
                        zhibiao.add(val);
                    }
                }
                //zhibiao.addAll(cellVals);
                return "ok";
            }

            @Override
            public void done() {
                System.out.println("done");
            }
        });
        commonExcel.handle();

        List<List<Object>> objects = new ArrayList<>();
        for (Object item : zhibiao) {
            List<Object> list = new ArrayList<>();
            list.add(item);
            objects.add(list);
        }

        Excel2007Utils.writeExcelData("E:\\", "abc", "Sheet1", objects);

        System.out.println("ok");
    }

    public static void test5() throws IOException, InvalidFormatException {
        String file = "D:\\code\\TPI\\大数据产品\\光明国际\\农业食品词语.xlsx";
        List<String> zhibiao = new ArrayList<>();

        CommonExcel commonExcel = new CommonExcel(file, new ICommonResultHandler() {

            @Override
            public boolean validateTilte(List<String> titles) {
                return true;
            }

            @Override
            public String store(List<String> cellVals, List<String> titles) {
                //System.out.println(cellVals);
                //System.out.println(titles);
                String val = cellVals.get(0).trim();
                val = cellVals.get(4).trim();
                zhibiao.add(val);
                if (val.equalsIgnoreCase("k")) {
                    System.out.println("here");
                }
                return "ok";
            }

            @Override
            public void done() {
                System.out.println("done");
            }
        });
        commonExcel.handle();

        System.out.println("ok");
    }

    public static void test4() throws IOException, InvalidFormatException {
        String file = "D:\\code\\TPI\\大数据产品\\光明国际\\农业食品词语.xlsx";
        List<String> zhibiao = new ArrayList<>();

        CommonExcel commonExcel = new CommonExcel(file, new ICommonResultHandler() {

            @Override
            public boolean validateTilte(List<String> titles) {
                return true;
            }

            @Override
            public String store(List<String> cellVals, List<String> titles) {
                //System.out.println(cellVals);
                //System.out.println(titles);
                String val = cellVals.get(0).trim();
                val = cellVals.get(4).trim();
                zhibiao.add(val);
                if (val.equalsIgnoreCase("k")) {
                    System.out.println("here");
                }
                return "ok";
            }

            @Override
            public void done() {
                System.out.println("done");
            }
        });
        commonExcel.handle();

        System.out.println("ok");
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

    public static void test3() throws IOException, InvalidFormatException {
        InputStream stream = new FileInputStream("K:\\新建文件夹\\2021年工程类项目工作量汇总 - 副本.xlsx");
        Workbook wb = WorkbookFactory.create(stream);

        for (int i = 0; i < wb.getNumberOfSheets(); i++) {
            Sheet sheet = wb.getSheetAt(i);
            //System.out.println(sheet.getSheetName());

            String path = "K:\\新建文件夹\\1.xlsx".replace("1", sheet.getSheetName());
            System.out.println(sheet.getSheetName() + "," + path);

            FileUtils.copyFile(new File("K:\\新建文件夹\\2021年工程类项目工作量汇总 - 副本.xlsx"), new File(path));

            handle(path, sheet.getSheetName());
        }
    }

    public static void handle(String path, String sheetName) throws IOException, InvalidFormatException {
        InputStream stream = new FileInputStream(path);
        Workbook wb = WorkbookFactory.create(stream);

        List<Integer> data = new ArrayList<>();
        for (int i = 0; i < wb.getNumberOfSheets(); i++) {
            Sheet sheet = wb.getSheetAt(i);
            if (!sheet.getSheetName().equals(sheetName)) {
                data.add(i);
            }
        }
        for (int i = data.size() - 1; i > -1; i--) {
            wb.removeSheetAt(data.get(i));
        }
        new File(path).delete();

        wb.write(new FileOutputStream(path));
    }


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

    public static void sonbin() throws Exception {

        String filePath = "d:/编码表.xlsx";
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

            Model model = null;
            List<Model> second = new ArrayList<>();
            List<Model> third = new ArrayList<>();

            Cell cell = row.getCell(1);
            String cellVal = getCellVal(cell);

            model = new Model(getCellVal(row.getCell(1)),
                    getCellVal(row.getCell(2)),
                    getCellVal(row.getCell(3)),
                    getCellVal(row.getCell(4)),
                    getCellVal(row.getCell(5)),
                    getCellVal(row.getCell(6)));

            System.out.println(model);
            list.add(model);
            //System.out.println(r);
            //System.out.println(Arrays.deepToString(cells.toArray()));
        }

        HikariDataSource dataSource = new HikariDataSource(getConfig());
        MySqlHelper mySqlHelper = new MySqlHelper(dataSource);
        handleModels(list, mySqlHelper);
    }

    private static void handleModels(List<Model> list, MySqlHelper mySqlHelper) throws Exception {
        List<Model> orgList = list.stream().filter(m -> m.getCategory().equalsIgnoreCase("单位")).collect(Collectors.toList());
        List<Model> deptList = list.stream().filter(m -> m.getCategory().equalsIgnoreCase("部门")).collect(Collectors.toList());
        List<Model> sysList = list.stream().filter(m -> m.getCategory().equalsIgnoreCase("业务系统")).collect(Collectors.toList());

//        for (Model item : orgList) {
//            List<LinkedHashMap<String, Object>> countList = mySqlHelper.simpleQuery("select * from org where OrgFullName = ?", new Object[]{item.getCategoryName()});
//            if (countList.size() > 0) {
//                System.out.printf("已存在:" + item.getCategoryName() + " - " + countList.size());
//            } else {
//                int id = mySqlHelper.add("INSERT INTO `org` (`OrgName`, `OrgFullName`, `Status`, `CreateUser`, `CreateTime`, `ModifyUser`, `ModifyTime`) VALUES (?, ?, '1', 'sa', '2021-03-11 00:00:00', 'sa', '2021-05-25 17:56:37');"
//                        , new Object[]{item.getCategoryName(), item.getCategoryName()});
//            }
//        }

        //addDept(mySqlHelper, deptList);
        addSys(mySqlHelper, sysList);

        System.out.println("ok");
    }

    private static void addDept(MySqlHelper mySqlHelper, List<Model> deptList) throws Exception {
        List<LinkedHashMap<String, Object>> orgList = mySqlHelper.simpleQuery("select * from org", new Object[]{});
        for (LinkedHashMap<String, Object> orgItem : orgList) {
            String orgId = orgItem.get("OrgID").toString();
            for (Model item : deptList) {
                List<LinkedHashMap<String, Object>> countList = mySqlHelper.simpleQuery("select * from department where DepartmentName = ? and orgId = ?", new Object[]{item.getCategoryName(), orgId});
                if (countList.size() > 0) {
                    System.out.printf("已存在:" + item.getCategoryName() + "-" + orgId + " - " + countList.size());
                } else {
                    int id = mySqlHelper.add("INSERT INTO `department` (`OrgID`, `DepartmentName`, `Status`, `CreateUser`, `CreateTime`, `ModifyUser`, `ModifyTime`) VALUES (?, ?, '1', 'sa', '2021-03-11 00:00:00', 'sa', '2021-05-25 17:56:37');"
                            , new Object[]{orgId, item.getCategoryName()});
                }
            }
        }
    }

    private static void addSys(MySqlHelper mySqlHelper, List<Model> sysList) throws Exception {
        List<LinkedHashMap<String, Object>> orgList = mySqlHelper.simpleQuery("select * from department order by DepartmentID desc", new Object[]{});
        List<List<Object>> paramsList = new ArrayList<>();

        int i = 0;
        for (LinkedHashMap<String, Object> orgItem : orgList) {
            String orgId = orgItem.get("OrgID").toString();
            String deptId = orgItem.get("DepartmentID").toString();
            System.out.println("current:" + (i++));
            for (Model item : sysList) {

                paramsList.add(Arrays.asList(new Object[]{item.getCategoryName(), orgId, deptId, item.getCategoryName(), orgId, deptId}));

                List<LinkedHashMap<String, Object>> countList = mySqlHelper.simpleQuery("select * from systeminfo where SystemName = ? and orgId = ? and DepartmentId = ?", new Object[]{item.getCategoryName(), orgId, deptId});
                if (countList.size() > 0) {
                    System.out.println("已存在:" + item.getCategoryName() + "-" + orgId + "-" + deptId + " - " + countList.size());
                } else {

                    int id = mySqlHelper.add("INSERT INTO `systeminfo` (`SystemName`, `SystemShortName`, `OrgID`, `DepartmentId`, `Status`, `CreateUser`, `CreateTime`, `ModifyUser`, `ModifyTime`) VALUES (?,?,?, ?, '1', 'sa', '2021-03-11 00:00:00', 'sa', '2021-05-25 17:56:37');"
                            , new Object[]{item.getCategoryName(), item.getCategoryName(), orgId, deptId});
                }
            }
        }

//        List<Model2> list = mySqlHelper.simpleQueryBatch("select ? as col1,? as col2,? as col3 from systeminfo where SystemName = ? and orgId = ? and DepartmentId = ?", paramsList, Model2.class);
//
//        List<List<Object>> result = new ArrayList<>();
//        for (List<Object> item : paramsList) {
//
//            long count = list.stream().filter(m -> m.getCol1().equalsIgnoreCase(item.get(0).toString())
//                    && m.getCol2().equalsIgnoreCase(item.get(1).toString())
//                    && m.getCol3().equalsIgnoreCase(item.get(1).toString()))
//                    .count();
//            if (count == 0) {
//                result.add(item);
//            }
//        }
//
//        mySqlHelper.executeSqlBatch("INSERT INTO `systeminfo` (`SystemName`, `SystemShortName`, `OrgID`, `DepartmentId`, `Status`, `CreateUser`, `CreateTime`, `ModifyUser`, `ModifyTime`) VALUES (?,?,?, ?, '1', 'sa', '2021-03-11 00:00:00', 'sa', '2021-05-25 17:56:37');",
//                result);
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


    public static HikariConfig getConfig() {

        HikariConfig config = new HikariConfig();

        //config.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/test?useUnicode=true&characterEncoding=utf8&useSSL=false");

        // config.setJdbcUrl("jdbc:mysql://192.168.100.92:3306/bd?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=UTC");
        config.setJdbcUrl("jdbc:mysql://192.168.52.64:3306/bd_fj?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=UTC");
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setUsername("root");
        config.setPassword("123456");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmptCacheSqlLimit", "2048");

        return config;
    }


}
