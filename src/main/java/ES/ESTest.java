package ES;

import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.client.core.CountResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortBuilder;

import java.io.IOException;
import java.util.*;

public class ESTest {

    /**
     * 引入的 POM
     * <dependency>
     * <groupId>org.elasticsearch</groupId>
     * <artifactId>elasticsearch</artifactId>
     * <version>7.5.1</version>
     * </dependency>
     * <!-- https://mvnrepository.com/artifact/org.elasticsearch.client/elasticsearch-rest-high-level-client -->
     * <dependency>
     * <groupId>org.elasticsearch.client</groupId>
     * <artifactId>elasticsearch-rest-high-level-client</artifactId>
     * <version>7.5.1</version>
     * </dependency>
     *
     * @param args
     */
    static RestHighLevelClient client;

    public static void main(String[] args) throws IOException {
//        client = new RestHighLevelClient(
//                RestClient.builder(
//                        new HttpHost("localhost", 9200, "http"),
//                        new HttpHost("localhost", 9201, "http")));

        client = new RestHighLevelClient(RestClient.builder(HttpHost.create("http://localhost:9200")));

        //client.close();

        Map<String, String> filedsMap = new HashMap<String, String>();
        //filedsMap.put("area","Botswana");
        //filedsMap.put("item","Roots, Other");
        //filedsMap.put("indicatorname","Quantity");
        BoolQueryBuilder queryBuilder = getQueryBuilder(filedsMap);
        List<Map<String, Object>> list = getPageResultList(queryBuilder, "article", 2, 5);
        System.out.println(list.size());
        for (Map<String, Object> s : list) {
            System.out.println(s.get("Title"));
        }
        System.out.println("count:" + getResultCount(queryBuilder, "article"));
    }

    /**
     * 构建查询对象
     *
     * @param filedsMap 查询条件 (key:查询字段 ,vlues:值)
     * @return
     */
    public static BoolQueryBuilder getQueryBuilder(Map<String, String> filedsMap) {

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        Set<String> strings = filedsMap.keySet();
        for (String string : strings) {
            boolQueryBuilder.must(QueryBuilders.wildcardQuery(string, "*" + filedsMap.get(string) + "*"));
        }
        return boolQueryBuilder;
    }

    /**
     * 获取分页后的结果集
     *
     * @param queryBuilder 查询对象
     * @param esIndex      索引名
     * @param pageNo       页数
     * @param pagesize     页大小
     * @return
     */
    public static List<Map<String, Object>> getPageResultList(QueryBuilder queryBuilder, String esIndex, int pageNo, int pagesize) {
        SearchRequest searchRequest = new SearchRequest(esIndex);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        if (pageNo >= 1) {
            searchSourceBuilder.query(queryBuilder).from((pageNo - 1) * pagesize).size(pagesize);
        } else {
            searchSourceBuilder.query(queryBuilder).from(0).size(pagesize);
        }
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = null;

        try {
            searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 从response中获得结果
        List<Map<String, Object>> list = new LinkedList();
        searchResponse.getHits();

        SearchHits hits = searchResponse.getHits();

        Iterator<SearchHit> iterator = hits.iterator();
        while (iterator.hasNext()) {
            SearchHit next = iterator.next();
            list.add(next.getSourceAsMap());
        }
        return list;
    }

    public List<LinkedHashMap<String, Object>> getPageResultListLinked(QueryBuilder queryBuilder, String esIndex, int pageNo, int pagesize, List<SortBuilder> sortBuilder, String[] includes, String[] excludes) {
        SearchRequest searchRequest = new SearchRequest(esIndex);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        //searchSourceBuilder.query(queryBuilder).from((pageNo - 1) * pagesize).size(pagesize).sort(sortBuilder).fetchSource(includes,excludes);
        if (sortBuilder != null) {
            searchSourceBuilder.query(queryBuilder).from((pageNo - 1) * pagesize).size(pagesize);
            for (SortBuilder item : sortBuilder) {
                searchSourceBuilder.sort(item);
            }
        } else {
            searchSourceBuilder.query(queryBuilder).from((pageNo - 1) * pagesize).size(pagesize);
        }
        if (includes != null && includes.length > 0) {
            searchSourceBuilder.fetchSource(includes, excludes);
        }
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = null;

        try {
            searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 从response中获得结果
        List<LinkedHashMap<String, Object>> list = new LinkedList();
        searchResponse.getHits();

        SearchHits hits = searchResponse.getHits();

        Iterator<SearchHit> iterator = hits.iterator();
        while (iterator.hasNext()) {
            SearchHit next = iterator.next();
            list.add(getMapValueForLinkedHashMap(next.getSourceAsMap()));
        }
        return list;
    }

    public static LinkedHashMap getMapValueForLinkedHashMap(Map dataMap) {
        LinkedHashMap returnMap = new LinkedHashMap();
        Iterator iterator = dataMap.keySet().iterator();
        while (iterator.hasNext()) {
            Object objKey = iterator.next();
            Object objValue = dataMap.get(objKey);
            if (objValue instanceof Map) {
                returnMap.put(objKey, getMapValueForLinkedHashMap((Map) objValue));
            } else {
                returnMap.put(toLowerCaseFirstOne(objKey.toString()), objValue);
            }
        }
        return returnMap;
    }

    private static String toLowerCaseFirstOne(String s) {
        if (Character.isLowerCase(s.charAt(0)))
            return s;
        else
            return (new StringBuilder()).append(Character.toLowerCase(s.charAt(0))).append(s.substring(1)).toString();
    }

    /**
     * 获取结果总数
     *
     * @param queryBuilder
     * @param esIndex
     * @return
     */
    public static Long getResultCount(QueryBuilder queryBuilder, String esIndex) {
        CountRequest countRequest = new CountRequest(esIndex);
        countRequest.query(queryBuilder);
        try {
            CountResponse response = client.count(countRequest, RequestOptions.DEFAULT);
            long length = response.getCount();
            return length;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0L;
    }

}
