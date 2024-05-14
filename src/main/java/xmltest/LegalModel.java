package xmltest;

import cn.hutool.core.util.XmlUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Data
@Builder
@AllArgsConstructor
public class LegalModel {
    // 日期
    private String date;
    // 事件编码
    private String eventCode;
    // 序号
    private String sequence;
    // 法律效力
    private String legalForce;
    // 法律状态
    private String legalStatus;
    // 法律状态信息
    private String legalStatusMsg;
    // 附加信息
    private String additionalInfo;
    // 变更前内容
    private String infoBefore;
    // 变更后内容
    private String infoAfter;
    // 分类
    private String category;


    //无效宣告决定日
    private String invalidatioDecisionDate;
    //无效宣告决定号
    private String invalidatioDecisionNum;
    //委内编号
    private String internalNumber;
    //授权日
    private String grantDate;
    //授权公告日
    private String grantPubDate;
    //受让人 被许可人
    private String assignee;
    //让与人 许可人
    private String assignor;
    // 许可种类
    private String grantType;
    //审查结论
    private String reviewConclusion;
    //申请人
    private String appliant;
    //申请公布日
    private String applicationPublicationDate;
    //其他有关事项
    private String otherRelatedMatters;
    //期满终止日期
    private String expirationDate;
    //解除日
    private String releaseDate;
    //合同履行期限
    private String contractDate;
    //合同备案号
    private String contractRecordNumber;
    //公开日
    private String publicationDate;
    //公告说明
    private String announcementDesc;
    //更正项目
    private String correctionItem;
    //发明名称
    private String nameOfInvention;
    //登记生效日
    private String effectiveDateOfRegistration;
    //登记解除日
    private String cancelDateOfRegistration;
    //登记号
    private String registrationNumber;
    //出质人
    private String pledgor;
    //备案日期
    private String filingDate;

    //转移_转让人
    private String changeBeforePerson;
    // 转移_受让人
    private String changeAfterPerson;

}
