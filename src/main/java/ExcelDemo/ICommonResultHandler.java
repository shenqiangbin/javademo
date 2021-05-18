package ExcelDemo;

import java.util.List;

public interface ICommonResultHandler {

    boolean validateTilte(List<String> titles);

    String store(List<String> cellVals, List<String> titles);

    void done();

}
