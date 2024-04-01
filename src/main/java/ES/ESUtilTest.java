package ES;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ESUtilTest {
    public static void main(String[] args) throws IOException {
        getScore();
    }

    static void test1() throws IOException {
        ESUtil esUtil = new ESUtil("http://localhost:9200");

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        List<Map<String, Object>> articleList = esUtil.getPageResultList(QueryBuilders.matchAllQuery(), "article", 2, 5, null, null, null, false);

        for (Map<String, Object> s : articleList) {
            System.out.println(s.get("Title"));
        }

        List<Map<String, Object>> list2 = esUtil.getPageResultList(QueryBuilders.matchAllQuery(), "article", 2, 5, null, null, null, true);
        for (Map<String, Object> s : list2) {
            System.out.println(s.get("Title"));
        }

        long count = esUtil.getCount(QueryBuilders.matchAllQuery(), "article");
        System.out.println("the total count is :" + count);
    }

    static int getScore() throws IOException {

        ESUtil esUtil = new ESUtil("http://192.168.52.65:9200","admin","admin");

        String authorCode = "000026337210";
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.wildcardQuery("authorcode", "*" + authorCode + "*"));

        System.out.println("es query:" + boolQueryBuilder.toString());
        List<Map<String, Object>> articleList = esUtil.getPageResultList(boolQueryBuilder, "gm_articles", 1, 10000, null, null, null, false);
        int totalScore = 0;
        for (Map<String, Object> s : articleList) {
            System.out.println(s.get("title"));
            System.out.println(s.get("numdownload"));
            System.out.println(s.get("numcitation"));
            System.out.println(s.get("numother"));
            System.out.println();

            Integer intNumDownload = s.get("numdownload")==null ? 0 : Integer.parseInt(s.get("numdownload").toString());
            Integer intNumCitation = s.get("numcitation")==null ? 0 : Integer.parseInt(s.get("numcitation").toString());
            Integer intNumother = s.get("numother")==null ? 0 : Integer.parseInt(s.get("numother").toString());

            int ffd =  s.get("ffd") == null ? 0 : Integer.parseInt(s.get("ffd").toString());;

            totalScore += converToScore(intNumDownload);
            totalScore += converToScore(intNumCitation);
            totalScore += converToScore(intNumCitation);
            totalScore += ffd;
        }

        return totalScore;

    }

    static int converToScore(int intNumDownload){
        int score1 = 0;
        if(intNumDownload > 100){
            score1 = 1;
        }
        if(intNumDownload > 300){
            score1 = 2;
        }
        if(intNumDownload > 500){
            score1 = 3;
        }
        if(intNumDownload > 700){
            score1 = 4;
        }
        if(intNumDownload > 900){
            score1 = 5;
        }
        if(intNumDownload > 1100){
            score1 = 6;
        }
        if(intNumDownload > 2000){
            score1 = 7;
        }
        if(intNumDownload > 3000){
            score1 = 9;
        }
        if(intNumDownload > 4000){
            score1 = 11;
        }
        if(intNumDownload > 6000){
            score1 = 13;
        }
        if(intNumDownload > 8000){
            score1 = 15;
        }
        if(intNumDownload > 10000){
            score1 = 17;
        }
        if(intNumDownload > 20000){
            score1 = 27;
        }
        return score1;
    }


}
