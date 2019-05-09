package dbmgr.microsoftAccess.model;

import java.util.List;

public class PageResult<T> {

    private List<T> data;
    private int totalCount;
    private int totalPage;

    @Override
    public String toString() {

        StringBuilder builder = new StringBuilder();
        if(data!=null)
        {
            for(T item : data){
                builder.append(item);
            }
        }
        return "PageResult{" +
                ", totalCount=" + totalCount +
                ", totalPage=" + totalPage +
                "data=" + builder.toString() +
                '}';
    }

    public List<T> getData() {
        return data;
    }
    public void setData(List<T> data) {
        this.data = data;
    }

    public int getTotalCount() {
        return totalCount;
    }
    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getTotalPage() {
        return this.totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }
}