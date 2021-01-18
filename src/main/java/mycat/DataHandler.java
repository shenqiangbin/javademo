package mycat;

public class DataHandler {

    private String[] fields;
    private String outFile;
    private String table;

    public DataHandler(String[] fields, String outFile, String table) {
        this.fields = fields;
        this.outFile = outFile;
        this.table = table;
    }

    public String[] getFields() {
        return fields;
    }

    public void setFields(String[] fields) {
        this.fields = fields;
    }

    public String getOutFile() {
        return outFile;
    }

    public void setOutFile(String outFile) {
        this.outFile = outFile;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String generateExportSQL(){
        String sql = ("select user,pwd,email,createtime,updatetime,status from oauser \n" +
                "INTO OUTFILE 'e:/file.txt' FIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '\\\"' LINES TERMINATED BY '\\r\\n';")
                .replace("user,pwd,email,createtime,updatetime,status", String.join(",",fields))
                .replace("oauser",table)
                .replace("e:/file.txt",outFile);
        return sql;
    }

    public String generateImportSQL(){
        String sql = "load data INFILE 'e:/file.txt' into TABLE oauser FIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '\\\"' LINES TERMINATED BY '\\r\\n' (user,pwd,email,createtime,updatetime,status);"
                .replace("e:/file.txt",outFile)
                .replace("oauser",table)
                .replace("user,pwd,email,createtime,updatetime,status", String.join(",",fields));
        return sql;
    }
}
