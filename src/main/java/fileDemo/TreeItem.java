package fileDemo;

public class TreeItem {

    private String code;
    private String name;
    private String icon;
    private boolean isleaf;

    public TreeItem(String code, String name, boolean isleaf) {
        this.code = code;
        this.name = name;
        this.isleaf = isleaf;
        this.icon = "";
    }

    public TreeItem(String code, String name, boolean isleaf, String icon)  {
        this.code = code;
        this.name = name;
        this.isleaf = isleaf;
        this.icon = icon;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isIsleaf() {
        return isleaf;
    }

    public void setIsleaf(boolean isleaf) {
        this.isleaf = isleaf;
    }

    @Override
    public String toString() {
        return "TreeItem{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", icon='" + icon + '\'' +
                ", isleaf=" + isleaf +
                '}';
    }
}
