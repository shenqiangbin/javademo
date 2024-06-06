package xmltest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
@AllArgsConstructor
public class LegalOrigiModel {
    // id
    private String id;
    // 公开号
    private String openNumber;
    // 日期
    private String date;
    // 事件编码
    private String eventCode;
    // 序号
    private String sequence;

    // 法律描述
    private String desc;

    // 类别 code
    private String eventClassCode;
    // 类别
    private String eventClassContent;

    // 状态标识符
    private String statusIdentifier;
    // 公开编码
    private String docDbPublicationNumber;
    // 申请编号
    private String docDbApplicationId;

    // 自由文本
    private String freeTextDesc;
    // 请求者
    private String requesterName;

    // 生效日期
    private String effectiveDate;
    // 新拥有者
    private String newOwner;

    // 附加信息
    //private String additionalInfo;


}
