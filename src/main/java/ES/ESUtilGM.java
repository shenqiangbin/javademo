package ES;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import dbmgr.mySqlAccess.MySqlHelper;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ESUtilGM {

    static ESUtil esUtil = new ESUtil("http://192.168.52.65:9200", "admin", "admin");
    static MySqlHelper mySqlHelper = new MySqlHelper(new HikariDataSource(getConfig()));


    // author_kbase_hascode 为 code 字段添加索引
    public static void main(String[] args) throws Exception {
        getScore("000026337210");
    }

    static void start () throws Exception {
        //String format = "select KEYWORD,EXPERT_CODE,EXPERT_NAME,DESCRIPTION,BRIEF,EXPERT_ID,BELONG_TYPE,TABLE_NAME,FILETYPE,ARTICLE from author_kbase_hascode ";
        String format = "select EXPERT_CODE,EXPERT_ID from author_kbase_hascode order by EXPERT_CODE desc";
        String sumsql = "select count(*) as val from author_kbase_hascode";

        List<LinkedHashMap<String, Object>> listExt = mySqlHelper.simpleQuery(sumsql, null);

        Long totalCount = Long.parseLong(listExt.get(0).get("val").toString());
        int pageSize = 5000;
        Long totalPage = totalCount / pageSize + (totalCount % pageSize == 0 ? 0 : 1);

        for (int pageIndex = 1; pageIndex <= totalPage; pageIndex++) {

            int startIndex = (pageIndex - 1) * pageSize;
            String sql = String.format("%s limit %s,%s ", format, startIndex, pageSize);
            System.out.println(String.format("%s/%s,%s ", pageIndex, totalPage, sql));
            List<LinkedHashMap<String, Object>> list = mySqlHelper.simpleQuery(sql, null);

            int count = 0;
            for (LinkedHashMap<String, Object> item : list) {
                System.out.print((count++)+",");
                String code = toEmptyIfNULL(item.get("EXPERT_CODE"));
                int score = getScore(code);
                String expertId = toEmptyIfNULL(item.get("EXPERT_ID"));
                if (StringUtils.isNotEmpty(expertId)) {
                    score += 500000;
                }

                updateScore(code, score);
            }

        }
    }

    static void updateScore(String authorCode, int score) throws SQLException {
        String format = "update author_kbase_hascode set score = ? where expert_code = ?";
        mySqlHelper.update(format, new Object[] { score, authorCode});
    }

    public static String toEmptyIfNULL(Object str) {
        return str == null ? "" : str.toString();
    }

    public static int getObjVal(Object str) {
        if(str == null) {return 0;}
        if(StringUtils.isEmpty(str.toString())){ return 0;}
        return Integer.parseInt(str.toString());
    }

    static int getScore(String authorCode) throws IOException {

//        String authorCode = "000026337210";
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.wildcardQuery("authorcode", "*" + authorCode + "*"));

//        System.out.println("es query:" + boolQueryBuilder.toString());
        List<Map<String, Object>> articleList = esUtil.getPageResultList(boolQueryBuilder, "gm_articles", 1, 10000, null, null, null, false);
        int totalScore = 0;
        for (Map<String, Object> s : articleList) {
//            System.out.println(s.get("title"));
//            System.out.println(s.get("numdownload"));
//            System.out.println(s.get("numcitation"));
//            System.out.println(s.get("numother"));
//            System.out.println();

            Integer intNumDownload = getObjVal(s.get("numdownload"));
            Integer intNumCitation =  getObjVal(s.get("numcitation"));
            Integer intNumother =  getObjVal(s.get("numother"));
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
