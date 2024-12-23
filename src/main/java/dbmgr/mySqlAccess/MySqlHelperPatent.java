package dbmgr.mySqlAccess;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import common.P;
import dbmgr.kbaseAccess.KBaseHelper;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Stream;

public class MySqlHelperPatent {

    static HikariDataSource dataSource = new HikariDataSource(getConfig());
    static KBaseHelper kBaseHelper = new KBaseHelper("jdbc:kbase://10.31.68.71", "DBOWN", "");

    public static void main(String[] args) throws Exception {

        searchZip();

//        List<String> xmls = searchXml();
//        findXmlHasAbstractFigure(xmls);
    }

    /**
     * 查询出哪个专利的有摘要附图信息
     */
    private static void findXmlHasAbstractFigure(List<String> xmls) throws Exception {
        String sql = "select 原文摘要XML,公开号 from EKR_PATENT_CN_NEW where 公开号 = " + String.join(" + ", xmls);
        List<LinkedHashMap<String, Object>> result = kBaseHelper.query(sql, new String[]{"原文摘要XML", "公开号"});
        if (result != null && result.size() > 0) {
            for (LinkedHashMap<String, Object> map : result) {
                String abstr = map.get("原文摘要XML") + "";
                if(abstr.contains("img")){
                    System.out.println("has abstract:" + map.get("公开号"));
                }
            }
        }

    }

    /**
     * E:\aaa专利\6123837 - 附图  查找所有的 xml
     *
     * @throws Exception
     */
    public static List<String> searchXml() throws Exception {
        Stream<Path> files = Files.walk(Paths.get("E:\\aaa专利\\6123838 - 附图"));
        List<String> list = new ArrayList<>();
        files.filter(m -> Files.isRegularFile(m)).forEach(
                m -> {
                    String filenameWithoutExt = FilenameUtils.getBaseName(m.toString());
                    String extension = FilenameUtils.getExtension(m.toString());
                    if (extension.equals("xml")) {
                        list.add(filenameWithoutExt);
                    }
                });
        return list;
    }

    /**
     * 查询某个 xml 在哪个 zip 文件里面。
     * <p>
     * 查找 压缩包 信息
     * SELECT * from ekr_dataprocess_xml_zipinfo where id = 1051
     *
     * @throws Exception
     */
    public static void searchZip() throws Exception {
        MySqlHelper mySqlHelper = new MySqlHelper(dataSource);

        for (int i = 0; i <= 29; i++) {
            System.out.println("i:" + i);
            List<LinkedHashMap<String, Object>> result = mySqlHelper.simpleQuery("select * from ekr_dataprocess_xml_xmlinfo_" + i + " where FILENAME = 'CN1249456C.xml' limit 10", null);
            if (result != null && result.size() > 0) {
                for (LinkedHashMap<String, Object> map : result) {
                    String zipId = map.get("ZIPID") + "";
                    System.out.println("zipId:" + zipId);
                }
                break;
            }
        }
    }


    public static HikariConfig getConfig() {

        HikariConfig config = new HikariConfig();

        config.setJdbcUrl("jdbc:mysql://10.31.68.13:3306/pat_xml_process_release?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=UTC");
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setUsername("root");
        config.setPassword("123456");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmptCacheSqlLimit", "2048");

        return config;
    }

}
