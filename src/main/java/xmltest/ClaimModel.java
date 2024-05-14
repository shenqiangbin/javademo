package xmltest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;



@Data
@Builder
@AllArgsConstructor
public class ClaimModel {
    private String num;
    private String id;
    private String independent;
    private String claimText;
    private List<ClaimRefInfo> refList;

    private String kbaseId;
    private String kbaseCategory;
    private List<String> kbaseRefIdList;
    // 从属于权利要求文字
    private List<String> kbaseRefIdTxtList;

    // 从属权利要求 ID
    private List<String> childRefIdList;

    public void setValByOpenNubmer(String openNumber, List<ClaimModel> list){
        this.kbaseId = openNumber + this.id;
        this.kbaseCategory = this.independent.equals("true") ? "独立权利要求" : "从属权利要求";

        List<String> refIdList = new ArrayList<>();
        refList.forEach(m -> refIdList.addAll(m.getIdList()));
        this.kbaseRefIdList = refIdList.stream().map(m -> openNumber + m).collect(Collectors.toList());

        this.kbaseRefIdTxtList = refList.stream().map(m -> m.getText()).collect(Collectors.toList());

        List<ClaimModel> child = list.stream().filter(m ->
                m.isRef(this.id)
        ).collect(Collectors.toList());

        this.childRefIdList = child.stream().map(m -> openNumber + m.id).collect(Collectors.toList());
    }

    private boolean isRef(String id){
        List<String> refIdList = new ArrayList<>();
        this.refList.forEach(m -> refIdList.addAll(m.getIdList()));

        return refIdList.contains(id);
    }
}
