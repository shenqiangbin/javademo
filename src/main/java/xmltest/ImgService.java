package xmltest;

import cn.hutool.core.util.XmlUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

public class ImgService {

    static List<LegalPicModel> getImgs(Document document, String openNumber) {

        String xpath = "/lexisnexis-patent-document/drawings/figure/img";
        NodeList eventList = XmlUtil.getNodeListByXPath(xpath, document);

        List<LegalPicModel> list = new ArrayList<>();

        for (int m = 0; m < eventList.getLength(); m++) {
            Node eventEle = eventList.item(m);
            if (eventEle.getNodeType() == Node.ELEMENT_NODE) {
                Element row = (Element) eventEle;

                String file = row.getAttribute("file") != null ? row.getAttribute("file") : "";
                String alt = row.getAttribute("alt") != null ? row.getAttribute("alt") : "";
                String imgContent = row.getAttribute("img-content") != null ? row.getAttribute("img-content") : "";
                String imgFormat = row.getAttribute("img-format") != null ? row.getAttribute("img-format") : "";
                String original = row.getAttribute("original") != null ? row.getAttribute("original") : "";

                String id = file.replace("." + imgFormat.toUpperCase(), "");

                String picCategory = getPicCategory(alt, file, document);

                LegalPicModel model = LegalPicModel.builder()
                        .id(id)
                        .openNumber(openNumber)
                        .pic(file)
                        .alt(alt)
                        .format(imgFormat)
                        .picCategory(picCategory)
                        .build();

                list.add(model);

            }
        }

        return list;
    }

    private static String getPicCategory(String alt, String file, Document document) {
        if (alt.equals("clipped image")) return "摘要附图";
        if (alt.equals("drawing sheet")) return "附图";
        if (alt.equals("thumbnail image")) return "摘要附图缩略图";
        if (alt.equals("image")) {
            if (belongModule("/lexisnexis-patent-document/abstract", document, file)) return "摘要插图";
            if (belongModule("/lexisnexis-patent-document/description", document, file)) return "说明书插图";
            if (belongModule("/lexisnexis-patent-document/claims", document, file)) return "权利要求插图";
        }
        throw new RuntimeException("unkown type: 未知类型的插图: " + file);
    }

    private static boolean belongModule(String xpath, Document document, String destStr) {
        NodeList eventList = XmlUtil.getNodeListByXPath(xpath, document);
        String content = getContent(eventList);
        return content != null && content.contains(destStr);
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
            arr.add(XmlUtil.toStr(eventEle));
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
