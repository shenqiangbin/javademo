package xmltest;

import cn.hutool.core.util.XmlUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CpcService {

    static List<CpcModel> getCombinationSet(Document document, String openNumber, String filename) {

        String xpath = "/lexisnexis-patent-document/bibliographic-data/classifications-cpc/combination-set";
        NodeList eventList = XmlUtil.getNodeListByXPath(xpath, document);

        List<CpcModel> resultList = new ArrayList<>();

        List<CpcCodeModel> combinationSetList = new ArrayList<>();
        for (int m = 0; m < eventList.getLength(); m++) {
            Node eventEle = eventList.item(m);
            if (eventEle.getNodeType() == Node.ELEMENT_NODE) {
                Element row = (Element) eventEle;

                NodeList combinationRankList = XmlUtil.getNodeListByXPath("combination-rank", row);
                CpcCodeModel cpcRankModel = handleCombinationRank(combinationRankList);
                combinationSetList.add(cpcRankModel);
            }
        }

        if (combinationSetList.size() > 0) {
            // 根据 code 进行分组
            Map<String, List<CpcCodeModel>> groupBy = combinationSetList.stream().collect(Collectors.groupingBy(CpcCodeModel::getCode));

            for (Map.Entry<String, List<CpcCodeModel>> item : groupBy.entrySet()) {
                String combineCpcVal = item.getValue().stream().map(m -> m.getCpcVal()).collect(Collectors.joining(""));
                String countryCode = item.getKey();

                resultList.add(CpcModel.builder().code(countryCode).cpcVal(combineCpcVal).openNumber(openNumber).filename(filename).build());
            }
        }

        return resultList;
    }

    private static CpcCodeModel handleCombinationRank(NodeList combinationRankList) {
        List<CpcCodeModel> list = new ArrayList<>();
        for (int m = 0; m < combinationRankList.getLength(); m++) {
            Node eventEle = combinationRankList.item(m);
            if (eventEle.getNodeType() == Node.ELEMENT_NODE) {
                Element row = (Element) eventEle;
                CpcCodeModel model = handleOneCombinationRank(row);
                list.add(model);
            }
        }
        if (list.size() > 0) {
            String combineCpcVal = "-" + list.stream().map(m -> m.getCpcVal()).collect(Collectors.joining(";")) + "\n";
            String countryCode = list.get(0).getCode();

            return CpcCodeModel.builder().code(countryCode).cpcVal(combineCpcVal).build();
        }
        return null;
    }

    private static CpcCodeModel handleOneCombinationRank(Element row) {
        String section = getContent(XmlUtil.getNodeListByXPath("classification-cpc/section", row));
        String classVal = getContent(XmlUtil.getNodeListByXPath("classification-cpc/class", row));
        String subClassVal = getContent(XmlUtil.getNodeListByXPath("classification-cpc/subclass", row));
        String mainGroup = getContent(XmlUtil.getNodeListByXPath("classification-cpc/main-group", row));
        String subgroup = getContent(XmlUtil.getNodeListByXPath("classification-cpc/subgroup", row));

        String cpcVal = String.format("%s%s%s%s/%s", section, classVal, subClassVal, mainGroup, subgroup);

        String country = getContent(XmlUtil.getNodeListByXPath("classification-cpc/generating-office/country", row));

        return CpcCodeModel.builder().code(country).cpcVal(cpcVal).build();

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
