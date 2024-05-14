package xmltest;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Notification {
    private String code;
    private String content;
}
