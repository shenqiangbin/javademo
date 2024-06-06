package xmltest.portal;

import cn.hutool.core.util.XmlUtil;
import io.minio.Xml;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import xmltest.LegalOrigiModel;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Test {
    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {

        String file = "d:/xml/hasTable.xml";

        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = builder.parse(new FileInputStream(file));

        NodeList figureNodes = document.getElementsByTagName("figure");

        List<ImgModel> list = new ArrayList<>();

        for (int m = 0; m < figureNodes.getLength(); m++) {
            Node eventEle = figureNodes.item(m);

            if (eventEle.getNodeType() != Node.ELEMENT_NODE)
                continue;

            Element row = (Element)eventEle;

            String id = "";
            NodeList eleImages = row.getElementsByTagName("image");
            if (eleImages.getLength() > 0) {
                Element oneImage = (Element) eleImages.item(0);
                id = oneImage.getAttribute("id") != null ? row.getAttribute("id") : "";
            }

            String title = "";
            NodeList eleTitles = row.getElementsByTagName("title");
            if (eleTitles.getLength() > 0) {
                Element oneTitle = (Element) eleTitles.item(0);
                title = oneTitle.getTextContent();
            }

            list.add(new ImgModel(id, title));
        }

        System.out.println(list);
    }
}
