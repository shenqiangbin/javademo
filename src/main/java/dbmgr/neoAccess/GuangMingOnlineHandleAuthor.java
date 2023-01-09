package dbmgr.neoAccess;

import com.alibaba.fastjson.JSONObject;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import dbmgr.mySqlAccess.MySqlHelper;
import fileDemo.FileHelper;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;


/**
 * 处理作者编码和作者信息，导出到文件里面。
 */
public class GuangMingOnlineHandleAuthor {

    static HikariDataSource dataSource = new HikariDataSource(getConfig());
    static MySqlHelper mySqlHelper = new MySqlHelper(dataSource);
    static List<List<Object>> params = new ArrayList<>();
    static List<String> existsList = new ArrayList<>();
    static StringBuilder mergeBuilder = new StringBuilder();
    static int existsCount = 0;

    /*
    * */
    public static void main(String[] args) throws Exception {
        mysqlToFile();
    }

    static void mysqlToFile() throws Exception {
        String format = "select * from gm_articles_has_author order by id asc";
        String sumsql = "select count(*) as val from gm_articles_has_author";

        String countStr = mySqlHelper.executeScalar(sumsql, null);
        Long totalCount = Long.parseLong(countStr);

        int pageSize = 5000;
        Long totalPage = totalCount / pageSize + (totalCount % pageSize == 0 ? 0 : 1);

        List<LinkedHashMap<String, Object>> list = null;
        String sql = "";

        StringBuilder builder = new StringBuilder();

        for (int pageIndex = 1; pageIndex <= totalPage; pageIndex++) {

            System.out.println("pageIndex:" + pageIndex);

            int startIndex = (pageIndex - 1) * pageSize;
            sql = String.format("%s limit %s,%s", format, startIndex, pageSize);
            list = mySqlHelper.simpleQuery(sql, new String[]{});

            builder = new StringBuilder();
            handleWordsResult(list, builder);

            FileHelper.writeTxtFile(builder.toString(), "D:\\code\\TPI\\大数据产品\\光明国际\\数据处理\\机构作者\\author.txt", false, true);

        }

    }

    private static void handleWordsResult(List<LinkedHashMap<String, Object>> list, StringBuilder builder) {
        for (LinkedHashMap<String, Object> item : list) {
            String year = toEmptyIfNULL(item.get("year"));
            String keyword = toEmptyIfNULL(item.get("keyword"));
            String title = toEmptyIfNULL(item.get("title"));
            String author = toEmptyIfNULL(item.get("author"));
            String org = toEmptyIfNULL(item.get("org"));
            String authorCode = toEmptyIfNULL(item.get("authorCode"));
            String business = toEmptyIfNULL(item.get("business"));
            String file = toEmptyIfNULL(item.get("file"));

            JSONObject obj = new JSONObject();
            obj.put("title", title);
            obj.put("author", author);
            obj.put("authorCode", authorCode);
            obj.put("organization", org);
            obj.put("keyword", keyword);
            obj.put("category", business);
            obj.put("year", year);
            obj.put("id", file);


            String[] codeArr = authorCode.split(";");
            String[] nameArr = author.split(";");

            if(codeArr.length != nameArr.length){
                System.out.println("数量不匹配" + authorCode + ":::" + author);
                continue;
            }

            for (int i = 0; i < codeArr.length; i++) {
                String code = codeArr[i];
                String name = nameArr[i];

                if (StringUtils.isBlank(code)) {
                    continue;
                }

                if (existsList.contains(code)) {
                    continue;
                } else {
                    builder.append(code).append(":::").append(name).append("\r\n");
                    existsList.add(code);
                }
            }

        }
    }

    public static String toEmptyIfNULL(Object str) {
        return str == null ? "" : str.toString();
    }

    public static String getParams(int size) {
        String[] arr = new String[size];
        for (int i = 0; i < size; i++) {
            arr[i] = "?";
        }
        return String.join(",", arr);
    }

    public static HikariConfig getConfig() {

        HikariConfig config = new HikariConfig();

        //config.setJdbcUrl("jdbc:mysql://:3306/bd?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=UTC");
        config.setJdbcUrl("jdbc:mysql://192.168.20.154:13306/gmgj_mid_data?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=UTC");
        config.setUsername("root");
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setPassword("123456");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmptCacheSqlLimit", "2048");

        return config;
    }

}
