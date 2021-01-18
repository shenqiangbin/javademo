package mycat;

public class DataHandlerTester {
    public static void main(String[] args) {
        demo01();
    }

    static void test1(){
        String[] fields = new String[]{"name","status"};
        DataHandler dataHandler = new DataHandler(fields,"d:/video.txt","video");
        String exportSql = dataHandler.generateExportSQL();
        String importSql = dataHandler.generateImportSQL();

        System.out.println(exportSql);
        System.out.println(importSql);
    }

    // mycat 的 mysql 连接方式必须是 native 才可以使用 load data
    static void demo01(){

        StringBuilder exportBuilder = new StringBuilder();
        StringBuilder importBuilder = new StringBuilder();
        // 查看列的语句
        // select concat("\"",column_name,"\",") from information_schema.columns where  table_name = 'nv_excel_pickup' and table_schema = 'statistic18' order by ordinal_position
        // 把 自增id列 和 esTime列 去掉了
        String[] fields = new String[]{"CreateUser","CreateTime","UpdateUser","UpdateTime","Value","ValueType","ValueTypeNormal","ValueClassify","ValueClassifyNormal","UnitName","UnitNameNormal","UnitCode","UnitCodeNormal","IndicatorName","IndicatorNameNormal","IndicatorNameGroup","IndicatorNameGroupNormal","IndicatorNameGroups","IndicatorNameGroupsNormal","IndicatorNameCode","IndicatorNameCodeNormal","IndicatorNameGroupCode","IndicatorNameGroupCodeNormal","IndicatorNameGroupsCode","IndicatorNameGroupsCodeNormal","ValueDate","ValueDateNormal","ValueYear","ValueYearNormal","ValueMonth","ValueMonthNormal","ValueDay","ValueDayNormal","ValueYearCode","ValueYearCodeNormal","ValueMonthCode","ValueMonthCodeNormal","PredictDate","PredictDateNormal","PredictYear","PredictYearNormal","PredictMonth","PredictMonthNormal","PredictDay","PredictDayNormal","PredictYearCode","PredictYearCodeNormal","PredictMonthCode","PredictMonthCodeNormal","ProductSpecifications","ProductSpecificationsNormal","ProductName","ProductNameNormal","ProductNameGroup","ProductNameGroupNormal","ProductSpecificationsCode","ProductSpecificationsCodeNormal","ProductNameCode","ProductNameCodeNormal","ProductNameGroupCode","ProductNameGroupCodeNormal","RegionName","RegionNameNormal","CountryName","CountryNameNormal","ProvinceName","ProvinceNameNormal","CityName","CityNameNormal","TownName","TownNameNormal","RegionNameCode","RegionNameCodeNormal","CountryNameCode","CountryNameCodeNormal","ProvinceNameCode","ProvinceNameCodeNormal","CityNameCode","CityNameCodeNormal","TownNameCode","TownNameCodeNormal","SectorName","SectorNameNormal","SectorCode","SectorCodeNormal","CounterpartCountryName","CounterpartCountryNameNormal","CounterpartCountryCode","CounterpartCountryCodeNormal","CounterpartSectorName","CounterpartSectorNameNormal","CounterpartSectorCode","CounterpartSectorCodeNormal","CompanyName","CompanyNameNormal","CompanyNameCode","CompanyNameCodeNormal","ValueBaseLine","ValueBaseLineNormal","Rate","RateNormal","Note","Flag","ValueSource","ValueSourceNormal","Purpose","PurposeNormal","PurposeCode","PurposeCodeNormal","Glossary","GlossaryNormal","GlossaryCode","GlossaryCodeNormal","Ontology","OntologyNormal","Title","TableSheetName","ValueAddress","MetadataSysID","MetadataID","DataState","RecordMd5","TaskState","ClassCode"};
        for (int i = 1; i <= 10; i++) {
            String prefix = "statistic2";
            String table = "nv_excel_pickup";
            String outFile = String.format("d:/%s%s_%s.txt",prefix,i,table);
            DataHandler dataHandler = new DataHandler(fields,outFile,"nv_excel_pickup");
            String exportSql = dataHandler.generateExportSQL();
            String importSql = dataHandler.generateImportSQL();

            String useDb = String.format("use %s%s",prefix,i);
            exportBuilder.append(useDb).append(";\r\n");
            exportBuilder.append(exportSql).append("\r\n");
            importBuilder.append(importSql).append("\r\n");
        }

        System.out.println(exportBuilder.toString());
        System.out.println(importBuilder.toString());
    }
}
