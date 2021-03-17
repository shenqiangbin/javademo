package dbmgr.kbaseAccess;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import common.P;
import dbmgr.mySqlAccess.MySqlHelper;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.SimpleFormatter;

/*
 * 将kbase中的数据同步到mysql中
 * */
public class KBaseToMySql {
    public static void main(String[] args) throws SQLException, IOException {

        //Sync1();
        ///SyncZhiBiao();
        //SyncZhiBiaoLike();
        //getMoHuFromTxt("abc");
        //getMoHuFromTxt();
        //SyncToSql();
        //SyncToSql();
        System.out.println("----hello world----");
        System.out.println("----end----");
    }

    public static void SyncZhiBiao() throws SQLException {
        String jdbc = "jdbc:kbase://192.168.107.175";
        KBaseHelper kBaseHelper = new KBaseHelper(jdbc, "DBOWN", "");
        //String sql = "SELECT 指标类别,精炼标题,指标 FROM NAVIFREQ_TOP50_CLASSIFY_ALL where 基本指标=1";
        String sql = "SELECT 指标类别,精炼标题,指标 FROM NAVIFREQ_TOP50_CLASSIFY_ALL where 基本指标=1";

        kBaseHelper.query(sql, new IResultHandler() {
            @Override
            public void handle(ResultSet resultSet) throws SQLException, IOException {
                StringBuilder builder = new StringBuilder();
                while (resultSet.next()) {
                    String leibie = resultSet.getString("指标类别");
                    String jinglian = resultSet.getString("精炼标题");
                    String zhibiao = resultSet.getString("指标");
                    builder.append("\"").append(leibie).append("\"").append(",").append("\"").append(jinglian).append("\"").append(",").append("\"").append(zhibiao).append("\"").append("\r\n");
                }
                FileUtils.writeStringToFile(new File("d:/2.csv"), builder.toString());
            }

            @Override
            public boolean isSuccess() {
                return false;
            }
        });
    }

    public static void SyncToSql() throws SQLException, IOException {

        File file = new File("d:/1.csv");
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        String strLine = null;
        int lineCount = 1;

        List<String> list = new ArrayList<>();

        StringBuilder builder = new StringBuilder();
        while (null != (strLine = bufferedReader.readLine())) {
            String zhibiao = strLine.split(",")[1].replace("\"", "");
            String sql = "INSERT INTO nv_normal_value(`CreateUser`, `CreateTime`, `UpdateUser`, `UpdateTime`, `NormalName`, `NormalCode`, `NormalNameCn`, `NormalDescribe`, `NormalDescribeCn`, `StandardID`, `StandardName`, `LableName`, `OrganizationID`) VALUES ('jsc', '2020-06-08 14:24:39', NULL, NULL, '国有企业车船税收入', '', '', '', '', '129', '农科院', '指标标准表', '73');\r\n";
            if(!list.contains(zhibiao)) {
                list.add(zhibiao);
                builder.append(sql.replace("国有企业车船税收入", zhibiao));
            }
            lineCount++;
        }
        FileUtils.writeStringToFile(new File("d:/1-tosql-2.txt"), builder.toString());
    }

    public static boolean findExists(String thezhibiao) throws IOException {
        File file = new File("d:/1.csv");
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        String strLine = null;
        int lineCount = 1;

        while (null != (strLine = bufferedReader.readLine())) {
            String zhibiao = strLine.split(",")[1].replace("\"", "");
            if(zhibiao.equalsIgnoreCase(thezhibiao))
                return true;
            lineCount++;
        }
        return false;

    }

    public static void getMoHuFromTxt() throws IOException {
        String content =FileUtils.readFileToString(new File("d:/INDEXSYNONYMOUS200.txt"),"GBK");
        //System.out.println(content);
        String[] arr = content.split("<REC>");
        StringBuilder builder = new StringBuilder();

        for (String item : arr){
            //System.out.println(item);
            if(StringUtils.isNotEmpty(item)){
                String zhibiao = "";
                String xiangSi = "";
                String[] itemArr = item.split("\r\n");
                for (String itemArrItem : itemArr){
                    if(itemArrItem.startsWith("<指标正式名>")){
                        zhibiao = itemArrItem.substring(8);
                    }
                    if(itemArrItem.startsWith("<指标同义词>")){
                        xiangSi = itemArrItem.substring(8);
                        break;
                    }
                }
                //System.out.println(zhibiao);
                //System.out.println(xiangSi);
                if(StringUtils.isNotEmpty(xiangSi) && findExists(zhibiao)){
                    String finalZhibiao = zhibiao;
                    Arrays.stream(xiangSi.split("@")).forEach(m->{
                        if(StringUtils.isNotEmpty(m)){
                            builder.append("\"").append(m).append("\"").append(",").append("\"").append(finalZhibiao).append("\"").append("\r\n");
                        }
                    });
                }
            }
        }

        FileUtils.writeStringToFile(new File("d:/xiangshi-from-txt.txt"), builder.toString());
    }

    /*同步相似指标*/
    public static void SyncZhiBiaoLike() throws SQLException, IOException {

        File file = new File("d:/1.csv");
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        String strLine = null;
        int lineCount = 1;

        while (null != (strLine = bufferedReader.readLine())) {
            String zhibiao = strLine.split(",")[1].replace("\"", "");
            getMoHu(zhibiao);
            lineCount++;
        }
    }

    private static void getMoHu(String zhibiao) throws SQLException {

        String jdbc = "jdbc:kbase://192.168.107.174";
        KBaseHelper kBaseHelper = new KBaseHelper(jdbc, "DBOWN", "cnkicsyd174@2019");

        //String sql = "SELECT 指标类别,精炼标题,指标 FROM NAVIFREQ_TOP50_CLASSIFY_ALL where 基本指标=1";
        String sql = "SELECT 检索指标 FROM YEARBOOKTABLEDATA2008_FORMAL WHERE 正式指标 = 'GDP'".replace("GDP", zhibiao); // 模糊指标的

        kBaseHelper.query(sql, new IResultHandler() {
            @Override
            public void handle(ResultSet resultSet) throws SQLException, IOException {
                StringBuilder builder = new StringBuilder();

                int count = 0;
                HashSet hs = new HashSet();
                String tmp = "";
                while (resultSet.next()) {
                    count++;

                    if(count>30000)
                        break;

                    String searchZhiBiao = resultSet.getString("检索指标");
                    if(!searchZhiBiao.equalsIgnoreCase(tmp)){
                        System.out.println(searchZhiBiao);
                        tmp = searchZhiBiao;
                        Arrays.stream(searchZhiBiao.split("；")).forEach(m ->
                                {
                                    if (!m.equalsIgnoreCase(zhibiao) && !m.contains("?")){
                                        System.out.println(m.trim());
                                        hs.add(m.trim());
                                    }

                                }
                        );
                    }
                }
                Iterator it = hs.iterator();
                while (it.hasNext()) {
                    String val = it.next().toString();
                    builder.append("\"").append(val).append("\"").append(",").append("\"").append(zhibiao).append("\"").append("\r\n");
                }

                if(StringUtils.isNotEmpty(builder.toString())){
                    FileOutputStream fos= FileUtils.openOutputStream(new File("d:/11.csv"),true);
                    fos.write(builder.toString().getBytes());
                    fos.close();
                }
            }

            @Override
            public boolean isSuccess() {
                return false;
            }
        });
    }

    public static void Sync1() {
        String jdbc = "";

        KBaseHelper kBaseHelper = new KBaseHelper(jdbc, "DBOWN", "");

        String sql = "SELECT 县（区） as area,GPS定位坐标（经度） as lng,GPS定位坐标（纬度） as lat FROM GUIZHOU500";
        List<SyncItem> list = kBaseHelper.query(sql, null, SyncItem.class, new String[]{"area", "lng", "lat"});

        StringBuilder builder = new StringBuilder();
        String now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        now = "2019-01-09 21:46:04";

        HikariDataSource dataSource = new HikariDataSource(getConfig());
        MySqlHelper mySqlHelper = new MySqlHelper(dataSource);

        for (SyncItem item : list) {
            String insertSql = String.format("insert region(name,FullName,RegionLevel,LocationX,LocationY,CreateOn,ParentId,RegionCode,ParentRegionCode,Suffix,UpdateOn) values('%s','',-9,%s,%s,'%s',-9,'','','','%s');", item.getArea(), item.getLng(), item.getLat(), now, now);
            //builder.append(insertSql);

            try {
                mySqlHelper.add(insertSql, null);
                P.print(insertSql);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        P.print(builder.toString());
    }

    public static void Sync2() {
        String jdbc = "jdbc:kbase://192.168.100.92";


        KBaseHelper kBaseHelper = new KBaseHelper(jdbc, "DBOWN", "");

        String sql = "SELECT 序号," +
                "区间（亩）," +
                "大坝ID," +
                "唯一标识码," +
                "市（州）," +
                "县（区）," +
                "乡（镇）," +
                "社区（居委会）、村委会," +
                "GPS定位坐标（经度）," +
                "GPS定位坐标（纬度）," +
                "大坝面积," +
                "分割面积," +
                "耕地," +
                "旱地," +
                "水田," +
                "园地," +
                "基本农田面积：亩," +
                "备注," +
                "农作物种植面积（亩）," +
                "稻谷（亩）," +
                "玉米（亩）," +
                "马铃薯（亩）," +
                "其他粮食作物（亩）," +
                "蔬菜（亩）," +
                "食用菌（亩）," +
                "茶叶（亩）," +
                "中草药材（亩）," +
                "油料（亩）," +
                "其他经济作物（亩）," +
                "涉及户数," +
                "贫困户," +
                "涉及人口数," +
                "贫困人口," +
                "涉及乡镇数," +
                "涉及贫困乡镇数," +
                "是否有极贫乡," +
                "涉及行政村数," +
                "贫困村数," +
                "深度贫困村数," +
                "坝区内机耕道总长," +
                "有效灌溉总面积," +
                "电网覆盖率," +
                "农产品冷库库容," +
                "冷链运输车辆数量," +
                "农产品冷链流通率," +
                "大棚面积," +
                "综合机械化率," +
                "农机拥有量," +
                "农业龙头企业数量," +
                "农民专业合作社数量," +
                "家庭农场数量," +
                "农产品是否有稳定销售渠道," +
                "坝区农产品主要销售区域," +
                "土地流转总面积," +
                "土地流转涉及农户数" +
                " FROM GUIZHOU500";
        kBaseHelper.tmpSync(sql, new String[]{"area", "lng", "lat"}, getConfig());


        //P.print(builder.toString());
    }

    public static HikariConfig getConfig() {

        HikariConfig config = new HikariConfig();


        //config.setJdbcUrl("jdbc:mysql://:3306/bd?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=UTC");
        config.setJdbcUrl("jdbc:mysql://:3306/bd?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=UTC");
        config.setUsername("root");
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setPassword("123456");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmptCacheSqlLimit", "2048");

        return config;
    }

}
