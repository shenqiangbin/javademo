package xmltest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;


@Data
@Builder
@AllArgsConstructor
public class ClaimRefInfo {
    private List<String> idList;
    private String text;
}
