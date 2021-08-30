package ExcelDemo;

import java.util.List;

public class Model {
    private String name;
    private String code;
    private String parentCode;

    private String category;
    private String categoryCode;
    private String categoryName;
    private String sysName;
    private String sysCate;
    private String bianma;


    private List<Model> children;

    public Model(String name, String code, String parentCode) {
        this.name = name;
        this.code = code;
        this.parentCode = parentCode;
    }

    public Model(String category, String categoryCode, String categoryName, String sysName, String sysCate, String bianma) {
        this.category = category;
        this.categoryCode = categoryCode;
        this.categoryName = categoryName;
        this.sysName = sysName;
        this.sysCate = sysCate;
        this.bianma = bianma;
    }

    @Override
    public String toString() {
        return "Model{" +
                "category='" + category + '\'' +
                ", categoryCode='" + categoryCode + '\'' +
                ", categoryName='" + categoryName + '\'' +
                ", sysName='" + sysName + '\'' +
                ", sysCate='" + sysCate + '\'' +
                ", bianma='" + bianma + '\'' +
                '}';
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getSysName() {
        return sysName;
    }

    public void setSysName(String sysName) {
        this.sysName = sysName;
    }

    public String getSysCate() {
        return sysCate;
    }

    public void setSysCate(String sysCate) {
        this.sysCate = sysCate;
    }

    public String getBianma() {
        return bianma;
    }

    public void setBianma(String bianma) {
        this.bianma = bianma;
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


