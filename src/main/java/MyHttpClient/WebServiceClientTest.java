package MyHttpClient;

import java.util.HashMap;

public class WebServiceClientTest {
    public static void main(String[] args) {
        test2();
    }

    static void test1() {
        // TODO Auto-generated method stub
        // 该WebService文档==>http://ws.webxml.com.cn/webservices/DomesticAirline.asmx
        WebServiceClient ws = new WebServiceClient("http://WebXml.com.cn/",
                "http://ws.webxml.com.cn/webservices/DomesticAirline.asmx",
                //"http://schemas.xmlsoap.org/wsdl/",
                "DomesticAirline", "DomesticAirlineSoap12",
                "getDomesticAirlinesTime", "getDomesticAirlinesTimeResult");
        HashMap<String, String> inMsg = new HashMap<String, String>();
        inMsg.put("startCity", "宁波");
        inMsg.put("lastCity", "青岛");
        inMsg.put("theDate", "2021-02-26");

        try {
            String ret = ws.sendMessage(inMsg);
            System.out.println(ret.toString()); // 没有对结果做处理
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void test2() {
        // TODO Auto-generated method stub
        // 该WebService文档==>http://ws.webxml.com.cn/webservices/DomesticAirline.asmx
        WebServiceClient ws = new WebServiceClient("http://tempuri.org/",
                "http://10.120.151.2/Ocrwebapi/MapOcrService.asmx",
                //"http://schemas.xmlsoap.org/wsdl/",
                "MapOcrService", "MapOcrServiceSoap12",
                "GetLayOut", "GetLayOutResult");
        HashMap<String, String> inMsg = new HashMap<String, String>();
        inMsg.put("startCity", "宁波");
        inMsg.put("lastCity", "青岛");
        inMsg.put("theDate", "2021-02-26");

        try {
            String ret = ws.sendMessage(inMsg);
            System.out.println(ret.toString()); // 没有对结果做处理
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
