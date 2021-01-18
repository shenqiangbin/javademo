package mycat;

public class DataHandlerTester {
    public static void main(String[] args) {
        String[] fields = new String[]{"name","status"};
        DataHandler dataHandler = new DataHandler(fields,"d:/video.txt","video");
        String exportSql = dataHandler.generateExportSQL();
        String importSql = dataHandler.generateImportSQL();

        System.out.println(exportSql);
        System.out.println(importSql);
    }
}
