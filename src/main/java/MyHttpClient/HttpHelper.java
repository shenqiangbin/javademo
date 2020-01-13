package MyHttpClient;

import common.P;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class HttpHelper {
    public String httpPost(String url, Map<String, String> headMap, Map<String, String> paramsMap) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setConfig(RequestConfig.custom().setConnectTimeout(5000).setConnectionRequestTimeout(1000).build());
            setGetHead(httpPost, headMap);

            if (paramsMap != null && paramsMap.size() > 0) {
                List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                Set<String> keySet = paramsMap.keySet();
                for (String key : keySet) {
                    nvps.add(new BasicNameValuePair(key, paramsMap.get(key)));
                }
                httpPost.setEntity(new UrlEncodedFormEntity(nvps));
            }

            CloseableHttpResponse response1 = httpclient.execute(httpPost);
            try {
                HttpEntity httpEntity = response1.getEntity();
                //InputStream is = httpEntity.getContent();
                String content = EntityUtils.toString(httpEntity);
                EntityUtils.consume(httpEntity);
                return content;
            } catch (Exception e) {
                log(e.toString());
            } finally {
                response1.close();
            }
        } catch (Exception e) {
            log(e.toString());
            e.printStackTrace();
        } finally {
            try {
                httpclient.close();
            } catch (IOException e) {
                log(e.toString());
                e.printStackTrace();
            }
        }
        return "";
    }

    private void setGetHead(HttpPost httpGet, Map<String, String> headMap) {
        if (headMap != null && headMap.size() > 0) {
            Set<String> keySet = headMap.keySet();
            for (String key : keySet) {
                httpGet.addHeader(key, headMap.get(key));
            }
        }
    }

    private void log(String msg){
        P.print(msg);
    }
}
