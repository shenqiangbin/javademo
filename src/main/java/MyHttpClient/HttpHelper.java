package MyHttpClient;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class HttpHelper {

    public static String httpGet(String url) throws IOException {
        return httpGet(url, null);
    }

    public static String httpGet(String url, Map<String, String> headMap) throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpGet httpGet = new HttpGet(url);
            httpGet.setConfig(getRequestConfig());
            setHead(httpGet, headMap);

            CloseableHttpResponse response = httpclient.execute(httpGet);
            return getResult(response);
        } finally {
            httpclient.close();
        }
    }

    /**
     * http的post请求
     *
     * @param url
     * @param paramsMap
     * @return
     */
    public static String httpPost(String url, Map<String, String> paramsMap, Map<String, String> headMap) throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setConfig(getRequestConfig());
            setHead(httpPost, headMap);

            setPostParams(httpPost, paramsMap);
            CloseableHttpResponse response = httpclient.execute(httpPost);
            return getResult(response);
        } finally {
            httpclient.close();
        }
    }

    public static String httpPostJSON(String url, String json, Map<String, String> headMap) throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpPost httpPost = new HttpPost(url);
            setHead(httpPost, headMap);
            httpPost.setConfig(getRequestConfig());

            StringEntity se = new StringEntity(json);
            httpPost.setEntity(se);

            CloseableHttpResponse response = httpclient.execute(httpPost);
            return getResult(response);
        } finally {
            httpclient.close();
        }
    }

    /**
     * 上传文件
     *
     * @param url
     * @param localFilePath
     * @param serverFieldName
     * @param paramsMap
     * @param headMap
     * @return
     * @throws IOException
     */
    public static String uploadFile(String url, String localFilePath, String serverFieldName, Map<String, String> paramsMap, Map<String, String> headMap) throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setConfig(getRequestConfig());
            setHead(httpPost, headMap);

            MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
            // add the file params
            FileBody binFileBody = new FileBody(new File(localFilePath));
            multipartEntityBuilder.addPart(serverFieldName, binFileBody);
            // 这样可以上传多个文件
            // multipartEntityBuilder.addPart(serverFieldName, binFileBody);
            // 设置上传的其他参数
            setUploadParams(multipartEntityBuilder, paramsMap);

            HttpEntity reqEntity = multipartEntityBuilder.build();
            httpPost.setEntity(reqEntity);

            CloseableHttpResponse response = httpclient.execute(httpPost);
            return getResult(response);
        } finally {
            httpclient.close();
        }
    }

    /**
     * 下载文件到本地
     * @param url
     * @param headMap
     * @param filePath
     * @throws IOException
     */
    public static void downloadFile(String url, Map<String, String> headMap, String filePath) throws IOException {
        File newFile = new File(filePath);
        if (!newFile.exists()) {
            newFile.createNewFile();
        }

        FileOutputStream stream = null;
        try {
            stream = new FileOutputStream(newFile);
            downloadFile(url, headMap, stream);
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
    }

    /**
     * 下载文件
     *
     * @param url
     * @param stream
     */
    public static void downloadFile(String url, Map<String, String> headMap, OutputStream stream) throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpGet httpGet = new HttpGet(url);
            httpGet.setConfig(getRequestConfig());
            setHead(httpGet, headMap);

            CloseableHttpResponse response = httpclient.execute(httpGet);

            HttpEntity httpEntity = response.getEntity();
            //long contentLength = httpEntity.getContentLength();
            InputStream is = httpEntity.getContent();

//            byte[] byteArr = EntityUtils.toByteArray(httpEntity);
//            stream.write(byteArr, 0, byteArr.length);

            byte[] buffer = new byte[4096];
            int r;
            while ((r = is.read(buffer)) > 0) {
                stream.write(buffer, 0, r);
            }
            stream.flush();

        } finally {
            httpclient.close();
        }
    }

    /**
     * 设置http的HEAD
     *
     * @param httpGetPost
     * @param headMap
     */
    private static void setHead(HttpRequestBase httpGetPost, Map<String, String> headMap) {
        if (headMap != null && headMap.size() > 0) {
            Set<String> keySet = headMap.keySet();
            for (String key : keySet) {
                httpGetPost.addHeader(key, headMap.get(key));
            }
        }
    }

    /**
     * 设置POST的参数
     *
     * @param httpPost
     * @param map
     * @throws Exception
     */
    private static void setPostParams(HttpPost httpPost, Map<String, String> map) {
        if (map != null && map.size() > 0) {
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            for (String key : map.keySet()) {
                nvps.add(new BasicNameValuePair(key, map.get(key)));
            }
            httpPost.setEntity(new UrlEncodedFormEntity(nvps, StandardCharsets.UTF_8));
        }
    }

    /**
     * 从 Response 中获取返回的结果字符串
     *
     * @param response
     * @return
     * @throws IOException
     */
    private static String getResult(CloseableHttpResponse response) throws IOException {
        String responseContent = null;
        try {
            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                throw new IOException("状态码不是200:" + response.getStatusLine().getStatusCode());
            }
            HttpEntity entity = response.getEntity();
            responseContent = EntityUtils.toString(response.getEntity(), "UTF-8");
            EntityUtils.consume(entity);
        } finally {
            response.close();
        }
        return responseContent;
    }

    /**
     * 设置上传文件时所附带的其他参数
     *
     * @param multipartEntityBuilder
     * @param params
     */
    private static void setUploadParams(MultipartEntityBuilder multipartEntityBuilder, Map<String, String> params) {
        if (params != null && params.size() > 0) {
            Set<String> keys = params.keySet();
            for (String key : keys) {
                multipartEntityBuilder
                        .addPart(key, new StringBody(params.get(key),
                                ContentType.TEXT_PLAIN));
            }
        }
    }

    /**
     * 设置接口调用超时配
     *
     * @return RequestConfig
     */
    private static RequestConfig getRequestConfig() {
        //setSocketTimeout 获取数据的超时时间
        return RequestConfig.custom()
                .setConnectTimeout(5000)
                .setConnectionRequestTimeout(1000)
                .setSocketTimeout(5000)
                .build();
    }
}
