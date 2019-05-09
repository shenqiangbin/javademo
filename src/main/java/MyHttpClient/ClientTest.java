package MyHttpClient;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import common.P;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class ClientTest {
    public static void main(String[] args){
        //saveFileToLocal();
        try {
            String serverUrl = "http://192.168.100.92:8080/api/Resource/UploadFile";
            //serverUrl = "http://192.168.105.89:8080/api/Resource/UploadFile";
            String fileName= "D:/3.png";
            fileName = "D:/shen2.rar";

            Map<String, String> map = new HashMap<String, String>();
            map.put("filename", "asfsadf.png");
            uploadFile(serverUrl,
                    fileName,"23142.png",map);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //输出文件到浏览器

    //保存文件到本地
    public static void saveFileToLocal(){
        File newFile = new File("d:/abc.png");
        try {
            newFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream stream = null;
        try {

            stream = new FileOutputStream(newFile);
            String url = "http://oimagec6.ydstatic.com/image?id=7347104849285270631&product=dict-homepage&w=&h=&fill=0&cw=&ch=&sbc=0&cgra=CENTER&of=jpeg";
            httpDownloadFile(url,stream);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            try {
                if(stream!=null)
                    stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void httpDownloadFile(String url,OutputStream stream){
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpGet httpGet = new HttpGet(url);
            CloseableHttpResponse response = httpclient.execute(httpGet);

            P.print("status:" + response.getStatusLine());
            HttpEntity httpEntity = response.getEntity();
            //long contentLength = httpEntity.getContentLength();
            InputStream is = httpEntity.getContent();

            byte[] buffer = new byte[4096];
            int r;
            while((r = is.read(buffer)) > 0) {
                stream.write(buffer, 0, r);
            }

            stream.flush();

            httpclient.close();

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

//    public void httpDownloadFile(String url, HttpServletResponse response) {
//
//        CloseableHttpClient httpclient = HttpClients.createDefault();
//        try {
//            HttpGet httpGet = new HttpGet(url);
//            CloseableHttpResponse response1 = httpclient.execute(httpGet);
//            try {
//                System.out.println(response1.getStatusLine());
//                HttpEntity httpEntity = response1.getEntity();
//                long contentLength = httpEntity.getContentLength();
//                InputStream is = httpEntity.getContent();
//
//                response.setContentLengthLong(contentLength);
//                //String fileName = image.getName();
//                //String fileType = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
//                response.setHeader("Content-Type","image/jpeg");
//                OutputStream toClient = response.getOutputStream();
//
//                //将请求返回的页面的流先放到buffer中，放一个buffer，输出response就写入一个buffer。
//                byte[] buffer = new byte[4096];
//                int r = 0;
//                while((r = is.read(buffer)) > 0) {
//                    toClient.write(buffer, 0, r);
//                }
//
//                toClient.flush();
//
//            } finally {
//                response1.close();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                httpclient.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }

    public static String uploadFile(String serverUrl, String localFilePath,
                                 String serverFieldName, Map<String, String> params)
            throws Exception {
        String respStr = null;
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpPost httppost = new HttpPost(serverUrl);
            FileBody binFileBody = new FileBody(new File(localFilePath));

            MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder
                    .create();
            // add the file params
            multipartEntityBuilder.addPart(serverFieldName, binFileBody);
            // 设置上传的其他参数
            setUploadParams(multipartEntityBuilder, params);

            HttpEntity reqEntity = multipartEntityBuilder.build();
            httppost.setEntity(reqEntity);

            CloseableHttpResponse response = httpclient.execute(httppost);
            try {
                System.out.println(response.getStatusLine());
                HttpEntity resEntity = response.getEntity();
                respStr = getRespString(resEntity);
                EntityUtils.consume(resEntity);
            }catch (Exception e) {
                e.printStackTrace();
            }finally {
                response.close();
            }
        } finally {
            httpclient.close();
        }
        System.out.println("resp=" + respStr);
        return respStr;
    }

    /**
     * 设置上传文件时所附带的其他参数
     *
     * @param multipartEntityBuilder
     * @param params
     */
    private static void setUploadParams(MultipartEntityBuilder multipartEntityBuilder,
                                 Map<String, String> params) {
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
     * 将返回结果转化为String
     *
     * @param entity
     * @return
     * @throws Exception
     */
    private static String getRespString(HttpEntity entity) throws Exception {
        if (entity == null) {
            return null;
        }
        InputStream is = entity.getContent();
        StringBuffer strBuf = new StringBuffer();
        byte[] buffer = new byte[4096];
        int r = 0;
        while ((r = is.read(buffer)) > 0) {
            strBuf.append(new String(buffer, 0, r, "GB2312"));
        }
        return strBuf.toString();
    }
}
