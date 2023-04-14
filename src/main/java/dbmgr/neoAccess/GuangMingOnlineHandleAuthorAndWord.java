package dbmgr.neoAccess;

import com.alibaba.fastjson.JSONObject;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import dbmgr.mySqlAccess.MySqlHelper;
import fileDemo.FileHelper;
import org.apache.commons.lang3.StringUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;


/**
 * 获取关键词-作者 信息，解决按关键词查询学者时没有数据展示的问题。
 */
public class GuangMingOnlineHandleAuthorAndWord {

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
        String format = "select * from gm_articles_has_author where db = '博士' or  db = '硕士' order by id asc";
        String sumsql = "select count(*) as val from gm_articles_has_author where db = '博士' or  db = '硕士'";

        String countStr = mySqlHelper.executeScalar(sumsql, null);
        Long totalCount = Long.parseLong(countStr);

        int pageSize = 5000;
        Long totalPage = totalCount / pageSize + (totalCount % pageSize == 0 ? 0 : 1);

        List<LinkedHashMap<String, Object>> list = null;
        String sql = "";

        StringBuilder builder = new StringBuilder();
        StringBuilder sqlbuilder = new StringBuilder();

        for (int pageIndex = 1; pageIndex <= totalPage; pageIndex++) {

            System.out.println(String.format("pageIndex:%s/%s", pageIndex,totalPage));

            int startIndex = (pageIndex - 1) * pageSize;
            sql = String.format("%s limit %s,%s", format, startIndex, pageSize);
            list = mySqlHelper.simpleQuery(sql, new String[]{});

            builder = new StringBuilder();
            sqlbuilder = new StringBuilder();
            handleWordsResult(list, builder, sqlbuilder);

            FileHelper.writeTxtFile(builder.toString(), "E:\\author_keyword.txt", false, true);
            FileHelper.writeTxtFile(sqlbuilder.toString(), "E:\\author_keyword_sql.txt", false, true);

        }


         // 最后再保存一遍
        save();
    }

    private static void handleWordsResult(List<LinkedHashMap<String, Object>> list, StringBuilder builder, StringBuilder sqlbuilder) throws SQLException {
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
            String[] orgArr = org.split(";");

//            if (codeArr.length != nameArr.length) {
//                System.out.println("数量不匹配" + authorCode + ":::" + author);
//                continue;
//            }

            for (int i = 0; i < nameArr.length; i++) {
                //String code = codeArr[i];
                String code = "";
                String name = nameArr[i];
                String orgItem = orgArr[i];

//                if (StringUtils.isBlank(code)) {
//                    continue;
//                }

                splitWorlds(keyword, code, name, orgItem, builder, sqlbuilder);
                // 只记录第一作者
                //break;
            }

        }
    }

    static void splitWorlds(String keyword, String code, String name, String org, StringBuilder builder, StringBuilder sqlbuilder) throws SQLException {
        String[] keywordArr = keyword.split(";;");
        for (String item : keywordArr) {
            String key = code + name + item;
            if (!existsList.contains(key)) {
                builder.append(code).append(":::").append(name).append(":::").append(item).append("\r\n");

                String sql = "INSERT INTO `dwd_expert_ins_rel` (`word`, `name`, `score`, `type`, `master`, `db`, `rel_id`, `createTime`, `moifyTime`) " +
                        "VALUES ('" + item + "', '" + name + "', '" + 1 + "', " + 0 + "," +
                        " 0, '" + "author-keyword" + "', NULL, '2022-11-23 10:03:00', '2022-11-23 10:03:00');";
                sqlbuilder.append(sql).append("\r\n");

                params.add(Arrays.asList(item,name, org));

                if (params.size() > 0 && params.size() % 5000 == 0) {
                    save();
                }


            } else {
                existsList.add(key);
            }
        }
    }

    static void save() throws SQLException {
        if (params.size() > 0) {
            String sql = "INSERT INTO `dwd_expert_ins_rel_ext` (`word`, `name`, `org`,  `score`, `type`, `master`, `db`, `rel_id`, `createTime`, `moifyTime`) " +
                    "VALUES (?,?,?, 1,0,0,'author-keyword', NULL, '2022-11-23 10:03:00', '2022-11-23 10:03:00')";
            mySqlHelper.executeSqlBatch(sql, params);
            params.clear();
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
        config.setJdbcUrl("jdbc:mysql://10.120.130.175:13306/gmgj_mid_data?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=UTC");
        config.setUsername("root");
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setPassword("123456");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmptCacheSqlLimit", "2048");

        return config;
    }

}
