package ES;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ESUtilTest {
    public static void main(String[] args) throws IOException {
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
}
