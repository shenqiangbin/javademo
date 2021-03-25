package MyHttpClient;

import fileDemo.FileHelper;
import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.soap.SOAP11Constants;
import org.apache.axis2.AxisFault;
import org.apache.axis2.Constants;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.transport.http.HTTPConstants;

import javax.xml.namespace.QName;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Base64;

public class WebServiceClient2 {

    public static void main(String[] args) {
        try {
            getPrivilege();
        } catch (AxisFault axisFault) {
            axisFault.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void getPrivilege() throws IOException {
        ServiceClient serviceClient = new ServiceClient();
        //创建服务地址WebService的URL,注意不是WSDL的URL
        String url = "http://10.120.151.2/Ocrwebapi/MapOcrService.asmx";
        EndpointReference targetEPR = new EndpointReference(url);

        Options options = new Options();
        options.setSoapVersionURI(SOAP11Constants.SOAP_ENVELOPE_NAMESPACE_URI);
        options.setAction("http://tempuri.org/GetLayOut");
        options.setTo(targetEPR);
        options.setTransportInProtocol(Constants.TRANSPORT_HTTP);
        options.setProperty(HTTPConstants.CHUNKED, "false");//设置不受限制.
        //options.setProperty(HTTPConstants.PROXY, buildProxy());
        options.setProperty(Constants.Configuration.HTTP_METHOD, HTTPConstants.HTTP_METHOD_POST);
        options.setExceptionToBeThrownOnSOAPFault(false);

        options.setUserName("medicine");
        options.setPassword("test");

        serviceClient.setOptions(options);



//        Options options = serviceClient.getOptions();
//        options.setUserName("medicine");
//        options.setPassword("test");
//        options.setTo(targetEPR);
//        //确定调用方法（wsdl 命名空间地址 (wsdl文档中的targetNamespace) 和 方法名称 的组合）
//        options.setAction("http://tempuri.org/GetLayOut");

        OMFactory fac = OMAbstractFactory.getOMFactory();
        /*
         * 指定命名空间，参数：
         * uri--即为wsdl文档的targetNamespace，命名空间
         * perfix--可不填
         */
        OMNamespace omNs = fac.createOMNamespace("http://tempuri.org/", "");
        // 指定方法
        OMElement method = fac.createOMElement("GetLayOut", omNs);        //为方法指定参数

        OMElement theRegionCode = fac.createOMElement("fileName", omNs);
        theRegionCode.setText("1.jpg");

        OMElement theRegionCode1 = fac.createOMElement("getByte", omNs);
        String abc = FileHelper.fileToByteConent("e:/1.jpg");
        theRegionCode1.setText(abc);

        OMElement userName = fac.createOMElement("UserName", omNs);
        userName.setText("medicine");
        OMElement pwd = fac.createOMElement("PassWord", omNs);
        pwd.setText("test");

        serviceClient.addHeader(userName);
        serviceClient.addHeader(pwd);

        method.addChild(theRegionCode);
        method.addChild(theRegionCode1);

        method.addChild(method);

        method.build();

        //远程调用web服务
        OMElement result = serviceClient.sendReceive(method);
        System.out.println(result);
    }


    public static byte[] fileToByteConent(String path) throws IOException {
        FileInputStream fileInputStream = null;
        fileInputStream = new FileInputStream(path);
        //ByteArrayInputStream inputStream = new ByteArrayInputStream(fileInputStream);

        StringBuilder builder = new StringBuilder();

        byte[] byteData = new byte[463524];
        //fileInputStream.read(byteData,0, byteData.length);

        int index = 0;
        int ch;
        while ((ch = fileInputStream.read()) != -1) {
            byteData[index++] = (byte) ch;
        }
        fileInputStream.close();
        //return arrayOutputStream.toByteArray();
        return byteData;
    }

}
