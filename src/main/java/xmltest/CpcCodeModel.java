package xmltest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
@AllArgsConstructor
public class CpcCodeModel {
    // 文档编号
    private String code;
    // 公开号
    private String cpcVal;
}
