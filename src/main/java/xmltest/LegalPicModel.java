package xmltest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
@AllArgsConstructor
public class LegalPicModel {
    // id
    private String id;
    // 公开号
    private String openNumber;
    // 专利种类
    private String patentType;
    // 专利类型
    private String patentCategory;

    // 专利名称
    private String patentName;

    // 图片名称
    private String pic;
    // 图片类型
    private String picCategory;
    // alt
    private String alt;
    // 格式
    private String format;

    public void setVal(String patentName, String patentType, String patentCategory){
        this.patentName = patentName;
        this.patentType = patentType;
        this.patentCategory = patentCategory;
    }
}
