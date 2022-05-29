package ES;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
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

public class ESUtil {

    RestHighLevelClient client;

    /**
     * 初始化
     *
     * @param url 比如：http://localhost:9200
     */
    public ESUtil(String url) {
        client = new RestHighLevelClient(RestClient.builder(HttpHost.create(url)));
    }

    public ESUtil(String url, String userName, String password) {
        RestClientBuilder builder = RestClient.builder(HttpHost.create(url));
        builderSetPwd(builder, userName, password);
        client = new RestHighLevelClient(builder);
    }

    public ESUtil(final String hostname, final int port, final String scheme) {
        RestClientBuilder builder = RestClient.builder(new HttpHost(hostname, port, scheme));
        client = new RestHighLevelClient(builder);
    }

    public ESUtil(final String hostname, final int port, final String scheme, String userName, String password) {
        RestClientBuilder builder = RestClient.builder(new HttpHost(hostname, port, scheme));
        builderSetPwd(builder, userName, password);
        client = new RestHighLevelClient(builder);
    }

    private void builderSetPwd(RestClientBuilder builder, String userName, String password) {
        CredentialsProvider provider = getCredentialProvider(userName, password);
        builder.setHttpClientConfigCallback(f -> f.setDefaultCredentialsProvider(provider));
    }

    private CredentialsProvider getCredentialProvider(String userName, String password) {
        CredentialsProvider provider = new BasicCredentialsProvider();
        provider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(userName, password));
        return provider;
    }

    /**
     * 构建查询对象
     *
     * @param filedsMap 查询条件 (key:查询字段 ,vlues:值)
     * @return
     */
    public BoolQueryBuilder getQueryBuilder(Map<String, String> filedsMap) {

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
     * @param index        索引名
     * @param pageNo       页数
     * @param pagesize     页大小
     * @return
     */
    public List<Map<String, Object>> getPageResultList(QueryBuilder queryBuilder, String index, int pageNo, int pagesize) throws IOException {
        SearchRequest searchRequest = new SearchRequest(index);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        if (pageNo >= 1) {
            searchSourceBuilder.query(queryBuilder).from((pageNo - 1) * pagesize).size(pagesize);
        } else {
            searchSourceBuilder.query(queryBuilder).from(0).size(pagesize);
        }
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = null;

        searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        // 从response中获得结果
        List<Map<String, Object>> list = new LinkedList();
        searchResponse.getHits();

        SearchHits hits = searchResponse.getHits();

        Iterator<SearchHit> iterator = hits.iterator();
        while (iterator.hasNext()) {
            SearchHit next = iterator.next();
            Map<String, Object> sourceAsMap = next.getSourceAsMap();
            sourceAsMap.put("_id", next.getId());
            list.add(sourceAsMap);
        }
        return list;
    }

    public List<LinkedHashMap<String, Object>> getPageResultListLinked(QueryBuilder queryBuilder, String esIndex, int pageNo, int pagesize, List<SortBuilder> sortBuilder, String[] includes, String[] excludes) throws IOException {
        SearchRequest searchRequest = new SearchRequest(esIndex);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        //searchSourceBuilder.query(queryBuilder).from((pageNo - 1) * pagesize).size(pagesize).sort(sortBuilder).fetchSource(includes,excludes);
        searchSourceBuilder.query(queryBuilder).from((pageNo - 1) * pagesize).size(pagesize);

        if (includes != null && includes.length > 0) {
            searchSourceBuilder.fetchSource(includes, excludes);
        }
        if (sortBuilder != null) {
            for (SortBuilder item : sortBuilder) {
                searchSourceBuilder.sort(item);
            }
        }
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = null;

        searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

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
                returnMap.put(objKey.toString(), objValue);
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
     * @param index
     * @return
     */
    public Long getCount(QueryBuilder queryBuilder, String index) throws IOException {
        CountRequest countRequest = new CountRequest(index);
        countRequest.query(queryBuilder);
        CountResponse response = client.count(countRequest, RequestOptions.DEFAULT);
        long length = response.getCount();
        return length;
    }
}
