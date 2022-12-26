package ES;

import dbmgr.mySqlAccess.model.PagedResponse;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.client.core.CountResponse;
import org.elasticsearch.common.xcontent.XContentHelper;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

public class ESUtil {

    RestHighLevelClient client;

    private Logger log = LoggerFactory.getLogger(ESUtil.class);

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

    public PagedResponse<Map<String, Object>> queryPage(QueryBuilder queryBuilder, String index, int pageNo, int pagesize) throws IOException {
        List<Map<String, Object>> list = this.getPageResultList(queryBuilder, index, pageNo, pagesize, null, null, null, true);
        Long count = this.getCount(queryBuilder, index);

        PagedResponse<Map<String, Object>> result = new PagedResponse<>();
        result.setList(list);
        result.setTotalCount(count);

        long totalPage = count / pagesize + (count % pagesize == 0 ? 0 : 1);
        result.setTotalPage((int) totalPage);

        return result;
    }

    public List<Map<String, Object>> getPageResultList(QueryBuilder queryBuilder, String esIndex, int pageNo, int pagesize, List<SortBuilder> sortBuilder, String[] includes, String[] excludes, boolean orderedMap) throws IOException {
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
        log.debug(searchSourceBuilder.toString());
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
            Map<String, Object> map = orderedMap ? getOrderedMap(next) : getMap(next);
            list.add(map);
        }
        return list;
    }

    private Map<String, Object> getMap(SearchHit searchHit) {
        Map<String, Object> map = searchHit.getSourceAsMap();
        map.put("_id", searchHit.getId());
        return map;
    }

    private Map<String, Object> getOrderedMap(SearchHit searchHit) {
        Map<String, Object> map = XContentHelper.convertToMap(searchHit.getSourceRef(), true, null).v2();
        map.put("_id", searchHit.getId());
        return map;
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
        log.debug("es count:{}", queryBuilder.toString());
        CountResponse response = client.count(countRequest, RequestOptions.DEFAULT);
        long length = response.getCount();
        log.debug("es count val:{}", length);

        return length;
    }
}
