package ExcelDemo;

public interface IResultHandler {
    String store(String cellVal, String parentId, boolean isLeaf);
}
