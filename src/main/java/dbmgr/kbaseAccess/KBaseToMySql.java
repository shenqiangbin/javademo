package dbmgr.kbaseAccess;

import com.zaxxer.hikari.HikariConfig;
import common.P;
import dbmgr.mySqlAccess.MySqlHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.SimpleFormatter;

/*
* 将kbase中的数据同步到mysql中
* */
public class KBaseToMySql {
    public static void main(String[] args){

        Sync1();

    }

    public static void Sync1(){
        String jdbc = "";

        KBaseHelper kBaseHelper = new KBaseHelper(jdbc,"DBOWN","");

        String sql = "SELECT 县（区） as area,GPS定位坐标（经度） as lng,GPS定位坐标（纬度） as lat FROM GUIZHOU500";
        List<SyncItem> list = kBaseHelper.query(sql,null,SyncItem.class,new String[]{"area","lng","lat"});

        StringBuilder builder = new StringBuilder();
        String now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        now = "2019-01-09 21:46:04";

        MySqlHelper mySqlHelper = new MySqlHelper(getConfig());

        for(SyncItem item : list){
            String insertSql = String.format("insert region(name,FullName,RegionLevel,LocationX,LocationY,CreateOn,ParentId,RegionCode,ParentRegionCode,Suffix,UpdateOn) values('%s','',-9,%s,%s,'%s',-9,'','','','%s');", item.getArea(),item.getLng(),item.getLat(),now,now);
            //builder.append(insertSql);

            try {
                mySqlHelper.insert(insertSql);
                P.print(insertSql);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        P.print(builder.toString());
    }

    public  static void   Sync2(){
        String jdbc = "jdbc:kbase://192.168.100.92";


        KBaseHelper kBaseHelper = new KBaseHelper(jdbc,"DBOWN","");

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
        kBaseHelper.tmpSync(sql,new String[]{"area","lng","lat"},getConfig());



        //P.print(builder.toString());
    }

    public static HikariConfig getConfig(){

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
