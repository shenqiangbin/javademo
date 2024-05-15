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

public class LegalService_CN {

    static List<LegalModel> getLegalEvents_cn(Document document, String openNumber) {

        String xpath = "/lexisnexis-patent-document/national-notifications/national-notification[@lang='chi']";
        NodeList eventList = XmlUtil.getNodeListByXPath(xpath, document);

        List<LegalModel> list = new ArrayList<>();

        for (int m = 0; m < eventList.getLength(); m++) {
            Node eventEle = eventList.item(m);
            if (eventEle.getNodeType() == Node.ELEMENT_NODE) {
                Element row = (Element) eventEle;

                String dateVal = row.getAttribute("date") != null ? row.getAttribute("date") : "";
                String eventCodeVal = row.getAttribute("event") != null ? row.getAttribute("event") : "";
                String sequenceVal = row.getAttribute("sequence") != null ? row.getAttribute("sequence") : "";

                NodeList notificationList = XmlUtil.getNodeListByXPath("notification", row);
                List<Notification> notificationModelList = convertToModel(notificationList);

                String legalForce = getTheCodeValFromNotificationModelList(notificationModelList, "法律效力");
                String legalStatus = getTheCodeValFromNotificationModelList(notificationModelList, "法律状态");
                String legalStatusMsg = getTheCodeValFromNotificationModelList(notificationModelList, "法律状态信息");

                String additionalInfo = getAdditionalInfo(notificationModelList);
                String infoBefore = getInfoBefore(notificationModelList);
                String infoAfter = getInfoAfter(notificationModelList);
                String category = getCategory(notificationModelList, legalStatus);

                String invalidatioDecisionDate = getTheCodeValFromNotificationModelList(notificationModelList, "无效宣告决定日");
                String invalidatioDecisionNum = getTheCodeValFromNotificationModelList(notificationModelList, "无效宣告决定号");
                String internalNumber = getTheCodeValFromNotificationModelList(notificationModelList, "委内编号");
                String grantDate = getTheCodeValFromNotificationModelList(notificationModelList, "授权日");
                String grantPubDate = getTheCodeValFromNotificationModelList(notificationModelList, "授权公告日");
                String assignee = getTheCodeValFromNotificationModelList(notificationModelList, "受让人"); // 被许可人
                String assignor = getTheCodeValFromNotificationModelList(notificationModelList, "让与人"); // 许可人
                String grantType = getTheCodeValFromNotificationModelList(notificationModelList, "许可种类");
                String reviewConclusion = getTheCodeValFromNotificationModelList(notificationModelList, "审查结论");
                String appliant = getTheCodeValFromNotificationModelList(notificationModelList, "申请人");
                String applicationPublicationDate = getTheCodeValFromNotificationModelList(notificationModelList, "申请公布日");
                String otherRelatedMatters = getTheCodeValFromNotificationModelList(notificationModelList, "其他有关事项");
                String expirationDate = getTheCodeValFromNotificationModelList(notificationModelList, "期满终止日期");
                String releaseDate = getTheCodeValFromNotificationModelList(notificationModelList, "解除日");
                String contractDate = getTheCodeValFromNotificationModelList(notificationModelList, "合同履行期限");
                String contractRecordNumber = getTheCodeValFromNotificationModelList(notificationModelList, "合同备案号");
                String publicationDate = getTheCodeValFromNotificationModelList(notificationModelList, "公开日");
                String announcementDesc = getTheCodeValFromNotificationModelList(notificationModelList, "公告说明");
                String correctionItem = getTheCodeValFromNotificationModelList(notificationModelList, "更正项目");
                String nameOfInvention = getTheCodeValFromNotificationModelList(notificationModelList, "发明名称");
                String effectiveDateOfRegistration = getTheCodeValFromNotificationModelList(notificationModelList, "登记生效日");
                String cancelDateOfRegistration = getTheCodeValFromNotificationModelList(notificationModelList, "登记解除日");
                String registrationNumber = getTheCodeValFromNotificationModelList(notificationModelList, "登记号");
                String pledgor = getTheCodeValFromNotificationModelList(notificationModelList, "出质人");
                String filingDate = getTheCodeValFromNotificationModelList(notificationModelList, "备案日期");

                String changeBeforePerson1 = getTheCodeValFromNotificationModelList(notificationModelList, "变更前:专利权人姓名", true);
                String changeBeforePerson2 = getTheCodeValFromNotificationModelList(notificationModelList, "变更前:专利权人名称", true);
                String changeBeforePerson3 = getTheCodeValFromNotificationModelList(notificationModelList, "变更前:专利权人", true);
                String changeBeforePerson4 = getTheCodeValFromNotificationModelList(notificationModelList, "变更前:申请人名称", true);
                String changeBeforePerson5 = getTheCodeValFromNotificationModelList(notificationModelList, "变更前:申请人姓名", true);
                String changeBeforePerson6 = getTheCodeValFromNotificationModelList(notificationModelList, "变更前:申请人", true);
                String changeBeforePerson7 = getTheCodeValFromNotificationModelList(notificationModelList, "变更前:共同专利权人", true);
                String changeBeforePerson8 = getTheCodeValFromNotificationModelList(notificationModelList, "变更前:共同申请人", true);

                String[] arr = {changeBeforePerson1, changeBeforePerson2, changeBeforePerson3, changeBeforePerson4,
                        changeBeforePerson5, changeBeforePerson6, changeBeforePerson7, changeBeforePerson8};
                // 转移_转让人
                String changeBeforePerson = Arrays.stream(arr).filter(n -> StringUtils.isNotEmpty(n)).collect(Collectors.joining(";;"));
                if(!legalStatus.contains("转移")){
                    changeBeforePerson = "";
                }

                String changeAfterPerson1 = getTheCodeValFromNotificationModelList(notificationModelList, "变更后:专利权人姓名", true);
                String changeAfterPerson2 = getTheCodeValFromNotificationModelList(notificationModelList, "变更后:专利权人名称", true);
                String changeAfterPerson3 = getTheCodeValFromNotificationModelList(notificationModelList, "变更后:专利权人", true);
                String changeAfterPerson4 = getTheCodeValFromNotificationModelList(notificationModelList, "变更后:申请人名称", true);
                String changeAfterPerson5 = getTheCodeValFromNotificationModelList(notificationModelList, "变更后:申请人姓名", true);
                String changeAfterPerson6 = getTheCodeValFromNotificationModelList(notificationModelList, "变更后:申请人", true);
                String changeAfterPerson7 = getTheCodeValFromNotificationModelList(notificationModelList, "变更后:共同专利权人", true);
                String changeAfterPerson8 = getTheCodeValFromNotificationModelList(notificationModelList, "变更后:共同申请人", true);

                String[] arr2 = {changeAfterPerson1, changeAfterPerson2, changeAfterPerson3, changeAfterPerson4,
                        changeAfterPerson5, changeAfterPerson6, changeAfterPerson7, changeAfterPerson8};
                // 转移_受让人
                String changeAfterPerson = Arrays.stream(arr2).filter(n -> StringUtils.isNotEmpty(n)).collect(Collectors.joining(";;"));
                if(!legalStatus.contains("转移")){
                    changeAfterPerson = "";
                }

                LegalModel model = LegalModel.builder()
                        .date(dateVal).eventCode(eventCodeVal).sequence(sequenceVal)
                        .legalForce(legalForce).legalStatus(legalStatus).legalStatusMsg(legalStatusMsg)
                        .additionalInfo(additionalInfo).infoBefore(infoBefore).infoAfter(infoAfter)
                        .category(category)
                        .invalidatioDecisionDate(invalidatioDecisionDate)
                        .invalidatioDecisionNum(invalidatioDecisionNum)
                        .internalNumber(internalNumber)
                        .grantDate(grantDate)
                        .grantPubDate(grantPubDate)
                        .assignee(assignee)
                        .assignor(assignor)
                        .grantType(grantType)
                        .reviewConclusion(reviewConclusion)
                        .appliant(appliant)
                        .applicationPublicationDate(applicationPublicationDate)
                        .otherRelatedMatters(otherRelatedMatters)
                        .expirationDate(expirationDate)
                        .releaseDate(releaseDate)
                        .contractDate(contractDate)
                        .contractRecordNumber(contractRecordNumber)
                        .publicationDate(publicationDate)
                        .announcementDesc(announcementDesc)
                        .correctionItem(correctionItem)
                        .nameOfInvention(nameOfInvention)
                        .effectiveDateOfRegistration(effectiveDateOfRegistration)
                        .cancelDateOfRegistration(cancelDateOfRegistration)
                        .registrationNumber(registrationNumber)
                        .pledgor(pledgor)
                        .filingDate(filingDate)
                        .changeBeforePerson(changeBeforePerson)
                        .changeAfterPerson(changeAfterPerson)
                        .build();

                list.add(model);

            }
        }

        return list;
    }

    static List<Notification> convertToModel(NodeList notificationList) {
        List<Notification> result = new ArrayList<>();
        for (int m = 0; m < notificationList.getLength(); m++) {
            Node eventEle = notificationList.item(m);
            Element row = (Element) eventEle;
            String codeVal = row.getAttribute("code") != null ? row.getAttribute("code") : "";
            String content = row.getTextContent();

            result.add(Notification.builder().code(codeVal).content(content).build());
        }
        return result;
    }

    /**
     * 获取指定 code 的值
     *
     * @param code
     * @return
     */
    static String getTheCodeValFromNotificationModelList(List<Notification> notificationModelList, String code) {
        return getTheCodeValFromNotificationModelList(notificationModelList, code, false);
    }

    /**
     * 获取指定 code 的值
     *
     * @param code
     * @return
     */
    static String getTheCodeValFromNotificationModelList(List<Notification> notificationModelList, String code, boolean fuzzy) {
        if (fuzzy) {
            List<Notification> list = notificationModelList.stream().filter(m -> m.getCode().contains(code)).collect(Collectors.toList());
            if (list.size() > 0) {
                return list.stream().map(m -> m.getContent()).collect(Collectors.joining(";;"));
            }
        } else {
            Optional<Notification> first = notificationModelList.stream().filter(m -> m.getCode().equals(code)).findFirst();
            if (first.isPresent()) {
                return first.get().getContent();
            }
        }
        return "";
    }

    /**
     * 获取附加信息
     *
     * @param notificationModelList
     * @return
     */
    static String getAdditionalInfo(List<Notification> notificationModelList) {
        List<String> codes = Arrays.asList(new String[]{"法律效力", "法律状态", "法律状态信息"});
        List<Notification> list = notificationModelList.stream().filter(m -> !codes.contains(m.getCode())).collect(Collectors.toList());
        List<Map<String, Object>> collect = list.stream().map(m -> getObjectToMap(m)).collect(Collectors.toList());
        return JSON.toString(collect);
    }

    /**
     * 获取变更前信息
     *
     * @param notificationModelList
     * @return
     */
    static String getInfoBefore(List<Notification> notificationModelList) {
        List<Notification> list = notificationModelList.stream().filter(m -> m.getCode().contains("变更前")).collect(Collectors.toList());
        List<Map<String, Object>> collect = list.stream().map(m -> getObjectToMap(m)).collect(Collectors.toList());
        return JSON.toString(collect);
    }

    /**
     * 获取变更后信息
     *
     * @param notificationModelList
     * @return
     */
    static String getInfoAfter(List<Notification> notificationModelList) {
        List<Notification> list = notificationModelList.stream().filter(m -> m.getCode().contains("变更后")).collect(Collectors.toList());
        List<Map<String, Object>> collect = list.stream().map(m -> getObjectToMap(m)).collect(Collectors.toList());
        return JSON.toString(collect);
    }

    /**
     * 获取类别
     *
     * @param notificationModelList
     * @return
     */
    static String getCategory(List<Notification> notificationModelList, String legalStatus) {
        List<String> codeList = notificationModelList.stream().map(m -> m.getCode()).collect(Collectors.toList());
        if (oneItemContain(codeList, "出质人") && oneItemContain(codeList, "质权人")) {
            return "质押";
        } else if (oneItemContain(codeList, " 变更后:申请人") || oneItemContain(codeList, "变更前:专利权人")) {
            if(legalStatus.contains("专利申请权、专利权的转移")) {
                return "转移";
            }
        } else if (oneItemContain(codeList, "受让人") && oneItemContain(codeList, "让与人")) {
            return "实施许可";
        }
        return "";
    }

    static boolean oneItemContain(List<String> codeList, String str) {
        for (String item : codeList) {
            if (item.contains(str))
                return true;
        }
        return false;
    }

    //Object转Map
    public static Map<String, Object> getObjectToMap(Object obj) {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        Class<?> clazz = obj.getClass();
        System.out.println(clazz);
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            String fieldName = field.getName();
            Object value = null;
            try {
                value = field.get(obj);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            if (value == null) {
                value = "";
            }
            map.put(fieldName, value);
        }
        return map;
    }
}
