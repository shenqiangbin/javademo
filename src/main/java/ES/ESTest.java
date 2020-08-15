package ES;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.IOException;

public class ESTest {

    /**
     * 引入的 POM
     *         <dependency>
     *             <groupId>org.elasticsearch</groupId>
     *             <artifactId>elasticsearch</artifactId>
     *             <version>7.5.1</version>
     *         </dependency>
     *         <!-- https://mvnrepository.com/artifact/org.elasticsearch.client/elasticsearch-rest-high-level-client -->
     *         <dependency>
     *             <groupId>org.elasticsearch.client</groupId>
     *             <artifactId>elasticsearch-rest-high-level-client</artifactId>
     *             <version>7.5.1</version>
     *         </dependency>
     *
     * @param args
     */
    public static void main(String[] args) throws IOException {
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("localhost", 9200, "http"),
                        new HttpHost("localhost", 9201, "http")));
        client.close();
    }
}
