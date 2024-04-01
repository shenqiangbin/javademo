package xmltest;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPathConstants;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.XmlUtil;

public class Demo01 {
    public static void main(String[] args) {
        int total = 1000000;
        int same = 10;
        double percent = same * 100 / (double)total;
        String formattedPercentage = String.format("%.2f", percent);

        String xml = "<Table><TableFormula><TableGroup cols=\"2\"><Colspec colName=\"c001\" colWidth=\"45%\"></Colspec><Colspec colName=\"c002\" colWidth=\"54%\"></Colspec><Tbody><Row><entry moreRows=\"1\">成分</entry><entry moreRows=\"1\">浓度范围(mg·L<Sup>-1</Sup>)</entry></Row><Row><entry moreRows=\"1\">硝酸钾</entry><entry moreRows=\"1\">800-1100</entry></Row><Row><entry moreRows=\"1\">硝酸铵</entry><entry moreRows=\"1\">800-900</entry></Row><Row><entry moreRows=\"1\">二水氯化钙</entry><entry moreRows=\"1\">200-250</entry></Row><Row><entry moreRows=\"1\">硫酸镁</entry><entry moreRows=\"1\">150-200</entry></Row><Row><entry moreRows=\"1\">磷酸二氢钾</entry><entry moreRows=\"1\">80-90</entry></Row></Tbody></TableGroup></TableFormula></Table>";
        String expected = "<table class='inserted-table' border='1' cellspacing='0' width='100%' style='border: 1px solid;table-layout: fixed;border-collapse: separate;'><col width='45%'/><col width='54%'/><thead></thead><tfoot></tfoot><tbody><tr style=''><td width='45%' rowspan='1' style='text-align: center;'>成分</td><td width='54%' rowspan='1' style='text-align: center;'>浓度范围(mg·L<Sup>-1</Sup>)</td></tr><tr style=''><td width='45%' rowspan='1' style='text-align: center;'>硝酸钾</td><td width='54%' rowspan='1' style='text-align: center;'>800-1100</td></tr><tr style=''><td width='45%' rowspan='1' style='text-align: center;'>硝酸铵</td><td width='54%' rowspan='1' style='text-align: center;'>800-900</td></tr><tr style=''><td width='45%' rowspan='1' style='text-align: center;'>二水氯化钙</td><td width='54%' rowspan='1' style='text-align: center;'>200-250</td></tr><tr style=''><td width='45%' rowspan='1' style='text-align: center;'>硫酸镁</td><td width='54%' rowspan='1' style='text-align: center;'>150-200</td></tr><tr style=''><td width='45%' rowspan='1' style='text-align: center;'>磷酸二氢钾</td><td width='54%' rowspan='1' style='text-align: center;'>80-90</td></tr></tbody></table>";
        String cnTableHtml = getCNTableHtml(xml);
        System.out.println(cnTableHtml);
    }

public static String getCNTableHtml(String xmlStr){
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    try {

        String xmlString =  "<?xml version=\"1.0\" encoding=\"utf-8\"?>" + xmlStr;
        // 使用Hutool的XmlUtil工具类将XML字符串转换成Document对象
        Document document = XmlUtil.readXML(xmlString);
        // 现在可以遍历和操作Document对象了，例如获取元素、属性等
        Element rootElement = document.getDocumentElement();
        Object byXPath = XmlUtil.getByXPath("//Table//TableFormula//TableGroup", rootElement, XPathConstants.STRING);
        NodeList tgroups = rootElement.getElementsByTagName("TableGroup");

        StringBuilder tableHtmls = new StringBuilder();
        for (int i = 0; i < tgroups.getLength(); i++) {
            tableHtmls.append("<table class='inserted-table' border='1' cellspacing='0' width='100%' style='border: 1px solid;table-layout: fixed;border-collapse: separate;'>");
            Element tgroup = (Element) tgroups.item(i);
            NodeList cols = tgroup.getElementsByTagName("Colspec");
            List<DocBookTableColumn> colList = new ArrayList<>();
            int tableWidth = 0;
            for (int j = 0; j < cols.getLength(); j++) {
                Element col = (Element) cols.item(j);
                String colname = col.getAttribute("colName") != null ? col.getAttribute("colName") : "";
                String colwidth = col.getAttribute("colWidth") != null ? col.getAttribute("colWidth") : "";
                String tdValue = col.getTextContent();
                String sValue = "";
                Pattern pattern = Pattern.compile("\\d+");
                Matcher matcher = pattern.matcher(colwidth);
                if (matcher.find()) {
                    sValue = matcher.group();
                }
                int iValue = 0;
                try {
                    iValue = Integer.parseInt(sValue);
                } catch (NumberFormatException e) {
                    // handle exception
                }
                tableWidth += iValue;
                colList.add(new DocBookTableColumn(colname, iValue, tdValue));
                if (iValue > 0) {
                    tableHtmls.append("<col width='" + (iValue + "%") + "'/>");
                } else {
                    tableHtmls.append("<col/>");
                }
            }

            //2.获取thead信息
            NodeList theadRows = XmlUtil.getNodeListByXPath("Thead//Row",tgroup);
            tableHtmls.append("<thead>");
            for (int k = 0; k < theadRows.getLength(); k++) {
                Node theadRow = theadRows.item(k);
                if (theadRow.getNodeType() == Node.ELEMENT_NODE) {
                    tableHtmls.append("<tr>");
                    Element row = (Element) theadRow;
                    int index = 0;
                    NodeList entrys = row.getElementsByTagName("entry");
                    for (int l = 0; l < entrys.getLength(); l++) {
                        Element entry = (Element) entrys.item(l);
                        String namest = entry.getAttribute("namest") != null ? entry.getAttribute("namest") : "";
                        String nameend = entry.getAttribute("nameend") != null ? entry.getAttribute("nameend") : "";
                        String moreRows = entry.getAttribute("moreRows") != null ? entry.getAttribute("moreRows") : "";
                        String tdValue = entry.getTextContent();
                        AppendCNTableHtml(tableHtmls, colList, index, namest, nameend, moreRows, tdValue);
                        index++;
                    }
                    tableHtmls.append("</tr>");
                }
            }
            tableHtmls.append("</thead>");

            //3.获取tfoot信息
            NodeList tfootRows = XmlUtil.getNodeListByXPath("Tfoot//Row",tgroup);
            tableHtmls.append("<tfoot>");
            for (int m = 0; m < tfootRows.getLength(); m++) {
                Node tfootRow = tfootRows.item(m);
                if (tfootRow.getNodeType() == Node.ELEMENT_NODE) {
                    tableHtmls.append("<tr>");
                    Element row = (Element) tfootRow;
                    int index = 0;
                    NodeList entrys = row.getElementsByTagName("entry");
                    for (int n = 0; n < entrys.getLength(); n++) {
                        Element entry = (Element) entrys.item(n);
                        String namest = entry.getAttribute("namest") != null ? entry.getAttribute("namest") : "";
                        String nameend = entry.getAttribute("nameend") != null ? entry.getAttribute("nameend") : "";
                        String moreRows = entry.getAttribute("moreRows") != null ? entry.getAttribute("moreRows") : "";
                        String tdValue = entry.getTextContent();
                        AppendCNTableHtml(tableHtmls, colList, index, namest, nameend, moreRows, tdValue);
                        index++;
                    }
                    tableHtmls.append("</tr>");
                }
            }
            tableHtmls.append("</tfoot>");

            //4.获取表格信息
            NodeList tbodyRows = XmlUtil.getNodeListByXPath("Tbody//Row",tgroup);
            tableHtmls.append("<tbody>");
            for (int o = 0; o < tbodyRows.getLength(); o++) {
                Node tbodyRow = tbodyRows.item(o);
                if (tbodyRow.getNodeType() == Node.ELEMENT_NODE) {
                    tableHtmls.append("<tr style=''>");
                    Element row = (Element) tbodyRow;
                    int index = 0;
                    NodeList entrys = row.getElementsByTagName("entry");
                    for (int p = 0; p < entrys.getLength(); p++) {
                        Element entry = (Element) entrys.item(p);
                        String namest = entry.getAttribute("namest") != null ? entry.getAttribute("namest") : "";
                        String nameend = entry.getAttribute("nameend") != null ? entry.getAttribute("nameend") : "";
                        String moreRows = entry.getAttribute("moreRows") != null ? entry.getAttribute("moreRows") : "";
                        String tdValue = entry.getTextContent();
                        AppendCNTableHtml(tableHtmls, colList, index, namest, nameend, moreRows, tdValue);
                        index++;
                    }
                    tableHtmls.append("</tr>");
                }
            }
            tableHtmls.append("</tbody>");
            tableHtmls.append("</table>");
        }
        return tableHtmls.toString();
    } catch (Exception e) {
        return xmlStr;
    }
}

private static void AppendCNTableHtml(StringBuilder tableHtmls, List<DocBookTableColumn> colList, int index, String namest, String nameend, String moreRows, String tdValue) {
    //目前中国专利table的td的moreRows和标注定义不同
    String rowspanAttribute = "";
    if (moreRows != null && !moreRows.trim().isEmpty()) {
        int rowspan = 0;
        try {
            rowspan = Integer.parseInt(moreRows);
            rowspanAttribute = String.format("rowspan='%d'", rowspan);
        } catch (NumberFormatException e) {
            // handle exception
        }
    }
    int tdwidth = 0;
    //定义了开始行和结束行，则累加所有宽度，并计算百分比
    if (namest != null && !namest.trim().isEmpty() && nameend != null && !nameend.trim().isEmpty()) {
        int colSpan = 0;
        int startIndex = -1;
        int endIndex = -1;
        for (int i = 0; i < colList.size(); i++) {
            if (colList.get(i).colname.equals(namest)) {
                startIndex = i;
            }
            if (colList.get(i).colname.equals(nameend)) {
                endIndex = i;
            }
        }
        if (startIndex >= 0 && endIndex >= 0) {
            for (int i = startIndex; i <= endIndex; i++) {
                colSpan++;
                tdwidth += colList.get(i).colwidth;
            }
        }
        tableHtmls.append(String.format("<td width='%s' colspan='%d' %s style='text-align: center;'>%s</td>", tdwidth + "%", colSpan, rowspanAttribute, tdValue));
    } else {
        tdwidth = 0;
        if (index < colList.size()) {
            tdwidth = colList.get(index).colwidth;
        }
        tableHtmls.append(String.format("<td width='%s' %s style='text-align: center;'>%s</td>", tdwidth + "%", rowspanAttribute, tdValue));
    }
}

}

class DocBookTableColumn {
    String colname;
    int colwidth;
    String tdValue;

    public DocBookTableColumn(String colname, int colwidth, String tdValue) {
        this.colname = colname;
        this.colwidth = colwidth;
        this.tdValue = tdValue;
    }
}