package xmltest;

import cn.hutool.core.util.XmlUtil;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jetty.util.ajax.JSON;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class LegalOrigiService {

    static List<LegalOrigiModel> getLegalEvents(Document document, String openNumber) {

        String xpath = "/lexisnexis-patent-document/legal-data/legal-event";
        NodeList eventList = XmlUtil.getNodeListByXPath(xpath, document);

        List<LegalOrigiModel> list = new ArrayList<>();

        for (int m = 0; m < eventList.getLength(); m++) {
            Node eventEle = eventList.item(m);
            if (eventEle.getNodeType() == Node.ELEMENT_NODE) {
                Element row = (Element) eventEle;

                String sequenceVal = row.getAttribute("sequence") != null ? row.getAttribute("sequence") : "";

                String date = getContent(XmlUtil.getNodeListByXPath("publication-date/date", row));
                String eventCode = getContent(XmlUtil.getNodeListByXPath("event-code-1", row));

                String id = String.format("%s_%s_%s", openNumber, sequenceVal, eventCode);

                String desc = getContent(XmlUtil.getNodeListByXPath("legal-description", row));
                String eventClassCode = getAttr(XmlUtil.getNodeListByXPath("event-class", row), "code");
                String eventClassContent = getContent(XmlUtil.getNodeListByXPath("event-class", row));
                String statusIdentifier = getContent(XmlUtil.getNodeListByXPath("status-identifier", row));
                String docDbPublicationNumber = getContent(XmlUtil.getNodeListByXPath("docdb-publication-number", row));
                String docDbApplicationId = getContent(XmlUtil.getNodeListByXPath("docdb-application-id", row));
                String freeTextDesc = getContent(XmlUtil.getNodeListByXPath("free-text-description", row));
                String requesterName = getContent(XmlUtil.getNodeListByXPath("requester-name", row));
                String newOwner = getContent(XmlUtil.getNodeListByXPath("new-owner", row));
                String effectiveDate = getContent(XmlUtil.getNodeListByXPath("effective-date/date", row));

                LegalOrigiModel model = LegalOrigiModel.builder()
                        .date(date).eventCode(eventCode).sequence(sequenceVal)
                        .id(id)
                        .openNumber(openNumber)
                        .desc(desc)
                        .eventClassCode(eventClassCode)
                        .eventClassContent(eventClassContent)
                        .statusIdentifier(statusIdentifier)
                        .docDbPublicationNumber(docDbPublicationNumber)
                        .docDbApplicationId(docDbApplicationId)
                        .freeTextDesc(freeTextDesc)
                        .requesterName(requesterName)
                        .effectiveDate(effectiveDate)
                        .newOwner(newOwner)

                        .build();

                list.add(model);

            }
        }

        return list;
    }

    /**
     * 获取指定节点的 content 内容
     *
     * @param dateEleList
     * @return
     */
    private static String getContent(NodeList dateEleList) {
        List<String> arr = new ArrayList<>();
        for (int m = 0; m < dateEleList.getLength(); m++) {
            Node eventEle = dateEleList.item(m);
            arr.add(eventEle.getTextContent());
        }
        return String.join(";;", arr);
    }

    /**
     * 获取节点的属性值
     *
     * @param dateEleList
     * @param attr
     * @return
     */
    private static String getAttr(NodeList dateEleList, String attr) {
        List<String> arr = new ArrayList<>();
        for (int m = 0; m < dateEleList.getLength(); m++) {
            Node eventEle = dateEleList.item(m);
            Element row = (Element) eventEle;
            String attrVal = row.getAttribute(attr) != null ? row.getAttribute(attr) : "";
            arr.add(attrVal);
        }
        return String.join(";;", arr);
    }



}
