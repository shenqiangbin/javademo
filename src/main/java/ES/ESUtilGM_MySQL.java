package ES;

import MyDate.DateUtil;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import dbmgr.mySqlAccess.MySqlHelper;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class ESUtilGM_MySQL {

    static ESUtil esUtil = new ESUtil("http://192.168.52.65:9200", "admin", "admin");
    static MySqlHelper mySqlHelper = new MySqlHelper(new HikariDataSource(getConfig()));


    // author_kbase_hascode 为 code 字段添加索引
    public static void main(String[] args) throws Exception {
        start();
    }

    static void start() throws Exception {
        //String format = "select KEYWORD,EXPERT_CODE,EXPERT_NAME,DESCRIPTION,BRIEF,EXPERT_ID,BELONG_TYPE,TABLE_NAME,FILETYPE,ARTICLE from author_kbase_hascode ";
        String format = "select EXPERT_CODE,EXPERT_ID from author_kbase_hascode order by EXPERT_CODE desc";
        String sumsql = "select count(*) as val from author_kbase_hascode";

        List<LinkedHashMap<String, Object>> listExt = mySqlHelper.simpleQuery(sumsql, null);

        Long totalCount = Long.parseLong(listExt.get(0).get("val").toString());
        int pageSize = 5000;
        Long totalPage = totalCount / pageSize + (totalCount % pageSize == 0 ? 0 : 1);

        for (int pageIndex = 1; pageIndex <= totalPage; pageIndex++) {
            System.out.println(DateUtil.format(new Date()));

            int startIndex = (pageIndex - 1) * pageSize;
            String sql = String.format("%s limit %s,%s ", format, startIndex, pageSize);
            System.out.println(String.format("%s/%s,%s ", pageIndex, totalPage, sql));
            List<LinkedHashMap<String, Object>> list = mySqlHelper.simpleQuery(sql, null);

            List<String> codeList = list.stream().map(item -> toEmptyIfNULL(item.get("EXPERT_CODE"))).collect(Collectors.toList());
            List<LinkedHashMap<String, Object>> articleList = getArticle(codeList);

            int count = 0;

            List<List<Object>> paramsList = new ArrayList<>();

            for (LinkedHashMap<String, Object> item : list) {
                //System.out.print((count++) + ",");
                String code = toEmptyIfNULL(item.get("EXPERT_CODE"));
                String expertId = toEmptyIfNULL(item.get("EXPERT_ID"));
                List<LinkedHashMap<String, Object>> authorCodeArticleList =
                        articleList.stream()
                                .filter(m -> m.get("authorCode").toString().equals(code))
                                .collect(Collectors.toList());
                int score = getScore(authorCodeArticleList, expertId);

                List<Object> params = new ArrayList<>();
                params.add(score);
                params.add(code);

                paramsList.add(params);
            }

            mySqlHelper.executeSqlBatch("update author_kbase_hascode set score = ? where expert_code = ?", paramsList);

        }
    }

    private static List<LinkedHashMap<String, Object>> getArticle(List<String> codeList) throws Exception {
        String sql = "select * from gm_articles_score_split where authorCode = ";
        List<String> sqlList = codeList.stream().map(item -> sql + "'" + item + "'").collect(Collectors.toList());
        String allSql = String.join(" union all ", sqlList);
        List<LinkedHashMap<String, Object>> titles = mySqlHelper.simpleQuery(allSql, null);
        return titles;
    }

    public static String toEmptyIfNULL(Object str) {
        return str == null ? "" : str.toString();
    }

    public static int getObjVal(Object str) {
        if (str == null) {
            return 0;
        }
        if (StringUtils.isEmpty(str.toString())) {
            return 0;
        }
        return Integer.parseInt(str.toString());
    }

    static int getScore(List<LinkedHashMap<String, Object>> authorCodeArticleList, String expertId) throws IOException {
        int totalScore = 0;

        if (StringUtils.isNotEmpty(expertId)) {
            totalScore += 500000;
        }

        for (Map<String, Object> s : authorCodeArticleList) {
            Integer intNumDownload = getObjVal(s.get("numdownload"));
            Integer intNumCitation = getObjVal(s.get("numcitation"));
            Integer intNumother = getObjVal(s.get("numother"));
            int ffd = s.get("ffd") == null ? 0 : Integer.parseInt(s.get("ffd").toString());

            totalScore += converToScore(intNumDownload);
            totalScore += converToScore(intNumCitation);
            totalScore += converToScore(intNumother);
            totalScore += ffd;
        }

        return totalScore;
    }

    static int converToScore(int intNumDownload) {
        int score1 = 0;
        if (intNumDownload > 100) {
            score1 = 1;
        }
        if (intNumDownload > 300) {
            score1 = 2;
        }
        if (intNumDownload > 500) {
            score1 = 3;
        }
        if (intNumDownload > 700) {
            score1 = 4;
        }
        if (intNumDownload > 900) {
            score1 = 5;
        }
        if (intNumDownload > 1100) {
            score1 = 6;
        }
        if (intNumDownload > 2000) {
            score1 = 7;
        }
        if (intNumDownload > 3000) {
            score1 = 9;
        }
        if (intNumDownload > 4000) {
            score1 = 11;
        }
        if (intNumDownload > 6000) {
            score1 = 13;
        }
        if (intNumDownload > 8000) {
            score1 = 15;
        }
        if (intNumDownload > 10000) {
            score1 = 17;
        }
        if (intNumDownload > 20000) {
            score1 = 27;
        }
        return score1;
    }

    public static HikariConfig getConfig() {

        HikariConfig config = new HikariConfig();

        //config.setJdbcUrl("jdbc:mysql://:3306/bd?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=UTC");
        config.setJdbcUrl("jdbc:mysql://10.120.130.41:3306/gmgj_release_202211?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=UTC");
        config.setUsername("root");
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setPassword("123456");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmptCacheSqlLimit", "2048");

        return config;
    }

}
