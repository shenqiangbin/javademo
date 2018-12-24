package MyHttpClient;

import java.io.*;

import common.P;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class ClientTest {
    public static void main(String[] args){
        saveFileToLocal();
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
}
