package ThreadDemo;


public class PageInfoResult {
    private String pageInfo;
    private String imgName;

    public PageInfoResult(String pageInfo, String imgName) {
        this.pageInfo = pageInfo;
        this.imgName = imgName;
    }

    public String getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(String pageInfo) {
        this.pageInfo = pageInfo;
    }

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }
}
