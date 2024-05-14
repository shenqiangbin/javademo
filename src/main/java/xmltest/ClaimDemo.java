package xmltest;

import cn.hutool.core.util.XmlUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class ClaimDemo {
    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {

        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        String file = "C:\\Users\\cnki52\\Desktop\\US20110315756A9.xml";
        file = "C:\\Users\\cnki52\\Desktop\\US20050000007A1.xml";
        //file = "C:\\Users\\cnki52\\Desktop\\CN117322164A.xml";

        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = builder.parse(new FileInputStream(file));

        String openNumber = "US20050000007A1";
        //openNumber = "CN117322164A";

        List<ClaimModel> list = getClaims(document, openNumber);

        list.forEach(m -> m.setValByOpenNubmer(openNumber, list));


        System.out.println(list);
    }

    static List<ClaimModel> getClaims(Document document, String openNumber) {
        String xpath = "/lexisnexis-patent-document/claims[@lang='eng']/claim";
        if (openNumber.startsWith("CN")) {
            xpath = "/lexisnexis-patent-document/claims[@lang='chi']/claim";
        }

        NodeList claimList = XmlUtil.getNodeListByXPath(xpath, document);

        List<ClaimModel> list = new ArrayList<>();

        for (int m = 0; m < claimList.getLength(); m++) {
            Node claimEle = claimList.item(m);
            if (claimEle.getNodeType() == Node.ELEMENT_NODE) {
                Element row = (Element) claimEle;

                String numVal = row.getAttribute("num") != null ? row.getAttribute("num") : "";
                String idVal = row.getAttribute("id") != null ? row.getAttribute("id") : "";
                String independentVal = row.getAttribute("independent") != null ? row.getAttribute("independent") : "";
                String claimTextContent = getClaimTextContent(row);
                List<ClaimRefInfo> refList = getRefInfo(row);

                ClaimModel model = ClaimModel.builder()
                        .num(numVal).id(idVal).independent(independentVal).claimText(claimTextContent).refList(refList)
                        .build();
                list.add(model);
            }
        }

        return list;
    }

    /**
     * 获取 claim-text 中的内容
     *
     * @param claimEle
     * @return
     */
    static String getClaimTextContent(Element claimEle) {
        NodeList claimTextEles = claimEle.getElementsByTagName("claim-text");
        StringBuilder builder = new StringBuilder();
        for (int m = 0; m < claimTextEles.getLength(); m++) {
            String content = XmlUtil.toStr(claimTextEles.item(m));
            content = content.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "");
            // 去掉回车和空格
            content = content.replace("\r\n", "").replace("        ", "");
            builder.append(content);
        }
        return builder.toString();
    }

    /**
     * 获取依赖的权利要求 id
     *
     * @param claimEle
     * @return
     */
    static List<ClaimRefInfo> getRefInfo(Element claimEle) {
        NodeList eles = claimEle.getElementsByTagName("claim-ref");
        List<ClaimRefInfo> idrefList = new ArrayList<>();
        for (int m = 0; m < eles.getLength(); m++) {
            Element row = (Element) eles.item(m);
            String idref = row.getAttribute("idref") != null ? row.getAttribute("idref") : "";
            String[] idrefArr = idref.split(" ");

            String content = row.getTextContent();

            idrefList.add(new ClaimRefInfo(Arrays.asList(idrefArr), content));
        }
        return idrefList;
    }


}

