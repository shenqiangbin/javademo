package ExcelDemo;

public interface ICommonResultHandler {
    String store(String[] cellVals, String[] titles);

    void done();
}
