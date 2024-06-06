package xmltest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
@AllArgsConstructor
public class CpcModel {
    // 文档编号
    private String filename;
    // 公开号
    private String openNumber;
    // 国家地区代码
    private String code;
    // CPC组合码
    private String cpcVal;


}
