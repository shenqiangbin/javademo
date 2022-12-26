package MyHttpClient;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.alibaba.fastjson.JSON;
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

import static org.apache.xmlbeans.impl.store.Public2.test;

public class ClientTest {
    public static void main(String[] args) throws Exception {
        //testUpload();
        //saveFileToLocal();
        //saveHtmlToLocal();
        testGet();
    }

    public static void testUpload() throws Exception {
        String serverUrl = "http://localhost:8182//normalValue/fileImport";
        String localFilePath = "e:/abc.png";
        Map<String, String> map = new HashMap<>();
        map.put("key", "value");
        String result = HttpHelper.uploadFile(serverUrl, localFilePath, "file", map, null);
        System.out.println(result);

        Reulst theResult = JSON.parseObject(result, Reulst.class);
        if (theResult.getStatus() != 0) {
            throw new Exception("上传失败了");
        }
    }

    //输出文件到浏览器
    /* 将第三方图片在自己页面上展示，（url地址如果能直接用更好）*/
    /* 将接口获取的文件输出到浏览器 */
//    @GetMapping("file")
//    public void httpDownloadFile(HttpServletResponse response) throws IOException {
//        String url = "http://oimagec6.ydstatic.com/image?id=7347104849285270631&product=dict-homepage&w=&h=&fill=0&cw=&ch=&sbc=0&cgra=CENTER&of=jpeg";
//        //response.setContentLengthLong(contentLength);
//        response.setHeader("Content-Type","image/jpeg");
//        ServletOutputStream outputStream = response.getOutputStream();
//        HttpHelper.downloadFile(url, null, outputStream);
//    }

    // 浏览器上下载文件
    // 在 指标Web 中可以查找

    //保存文件到本地
    public static void saveFileToLocal() throws IOException {
        String url = "http://oimagec6.ydstatic.com/image?id=7347104849285270631&product=dict-homepage&w=&h=&fill=0&cw=&ch=&sbc=0&cgra=CENTER&of=jpeg";
        HttpHelper.downloadFile(url, null, "e:/abc.png");
    }


    public static void saveHtmlToLocal() throws IOException {
        String url = "http://blog.sqber.com/articles/mac-can-not-use-harddisk.html";
        HttpHelper.downloadFile(url, null, "/Users/adminqian/shen/mac-can-not-use-harddisk.html");
    }

    public static void testGet() throws IOException {
        String url = "https://bigdata.cnki.net/table/tableapi/audit/share/v/excelPickUpDynamic?databaseCode=gatjnj&currentPage=1&pageSize=2&startEsTime=2002-02-18%2010:11:27&endEsTime=2052-02-18%2010:11:27";
        String val = HttpHelper.httpGet(url, null);
        System.out.println(val);
    }


}

class Reulst {
    private Integer status;
    private String message;
    private Object data;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
