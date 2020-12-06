package ExcelDemo;

import java.util.List;

public class Model {
    private String name;
    private String code;
    private String parentCode;

    private List<Model> children;

    public Model(String name, String code, String parentCode) {
        this.name = name;
        this.code = code;
        this.parentCode = parentCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public List<Model> getChildren() {
        return children;
    }

    public void setChildren(List<Model> children) {
        this.children = children;
    }
}


