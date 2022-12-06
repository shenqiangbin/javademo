package dbmgr.neoAccess;

import ExcelDemo.Excel2007Utils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import common.P;
import dbmgr.mySqlAccess.MySqlHelper;
import fileDemo.FileHelper;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class GuangMing {

    //static Neo4jHelper neo4jHelper = initHelper();
    static Neo4jHelper neo4jHelper = initOnlineHelper();
    static List<List<Object>> objects = new ArrayList<>();
    static HikariDataSource dataSource = new HikariDataSource(getConfig());
    static MySqlHelper mySqlHelper = new MySqlHelper(dataSource);

    public static void main(String[] args) throws Exception {
        //getSecondWord();

        handleShowWord();
        //mergeDbFirstLevel();
        //readfile();

//        String[] color = new String[]{"color1","color2","color3","color4"};
//        String[] arr = new String[]{"1", "2", "3", "4", "5", "6", "7","8","9"};
//        int loop = arr.length % 4 == 0 ? arr.length / 4 : arr.length / 4 + 1;
//        for (int i = 1; i <= 4; i++) {
//            for(int j = (i-1)*loop; j < Math.min(arr.length, i*loop); j++){
//                System.out.println(arr[j] + ":" + color[i-1]);
//            }
//        }
    }

    // 看提供给 贾亚飞 的数据里，关键词有没有某些词
    public static boolean readfile(String word) throws Exception {

        String filePath = "C:\\Users\\cnki52\\Desktop\\光明\\jsons\\part-00000-69640f91-4e6f-4b06-ae21-4881e0e07600-c000-贾亚非跑数据的基础数据\\part-00000-69640f91-4e6f-4b06-ae21-4881e0e07600-c000.json";

        int i = 0;
        String result = null;
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;
        try {
            File fileName = new File(filePath);
            fileReader = new FileReader(fileName);
            bufferedReader = new BufferedReader(fileReader);
            String read = null;
            while ((read = bufferedReader.readLine()) != null) {
                //result = result + read + "\r\n";
                //System.out.println("current i:" + (i++));
                JSONObject jsonObject = JSON.parseObject(read);
                Object keyword = jsonObject.get("keyword");
                Object id = jsonObject.get("id");
                Object title = jsonObject.get("title");

                if(keyword.toString().contains(word)){
                    return true;
                }
                //System.out.println(title);
            }
        } finally {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (fileReader != null) {
                fileReader.close();
            }
        }

       return false;
    }

    static List<String> wordbuilder = new ArrayList<>();

    /* 用户 merge 库； 用户热词；图谱一级词汇 */
    public static void mergeDbFirstLevel() throws Exception {
        String batch = "merge20221123";
        String sql = String.format("MATCH (a:`%s`) where  a.type = %s and a.userfocus = 1 and a.check = 1 return a  order by a.score desc limit 1000",
                batch,
                210);
        List<Map<String, Object>> cnkiList = neo4jHelper.GetValue(sql);
        int i = 1;
        for (Map<String, Object> item : cnkiList) {
            String uuid = item.get("uuid").toString();
            String neoType = item.get("neoType").toString();
            String name = item.get("name").toString();
            //System.out.println((i++) + ":" + name);
            sixiangxianHasVal(name);
            wordbuilder.add("--------------" + name);
            wordbuilder.add(name);
            //getNextLevelData(batch,uuid);
        }

        System.out.println("word list:");
        String str = wordbuilder.stream().distinct().collect(Collectors.joining("\r\n"));
        System.out.println(str);
    }

    public static void getNextLevelData(String batch, String nodeid){
        List<String> expectNodeIds = null;
        if (expectNodeIds == null || expectNodeIds.size() == 0) {
            expectNodeIds = new ArrayList<>();
            expectNodeIds.add("-1000000000");
        }        String sql = String.format("MATCH p = (a:`%s`) <- [r] ->(b)  where id(a) = %s and a.check = 1 and none(x in nodes(p) where id(x) in [%s]) return * order by b.score desc  limit %s",
                batch, nodeid, String.join(",", expectNodeIds), 20);
        List<Map<String, Object>> cnkiList = neo4jHelper.GetValue(sql);
        for (Map<String, Object> item : cnkiList) {
            if(item == null || item.get("uuid")== null){
               continue;
            }
            String uuid = item.get("uuid").toString();
            String neoType = item.get("neoType").toString();
            if(neoType.equalsIgnoreCase("node")){
                String name = item.get("name").toString();
                wordbuilder.add(name);
            }
            //System.out.println((i++) + ":" + name);
            //sixiangxianHasVal(name);
            //getNextLevelData(batch,uuid);
        }
    }

    // 看这个词在四象限是否有数据
    public static void sixiangxianHasVal(String word) throws Exception {

        String sql = "SELECT count(*) FROM coordinate WHERE centor_nodes = ? ORDER BY id DESC LIMIT 10";
        List<Object> params = Arrays.asList(new String[]{word});
        String s = mySqlHelper.executeScalar(sql, params);
        //System.out.println(s);
        if (Integer.parseInt(s) == 0) {
            boolean fileHasWord = readfile(word);
            if(fileHasWord){
                System.out.println("no data but file has data:" + word);
            }else{
                System.out.println("no data and file no data:" + word);

            }
        } else {
            //System.out.println("has data:" + word);
        }
    }

    public static HikariConfig getConfig() {

        HikariConfig config = new HikariConfig();

        //config.setJdbcUrl("jdbc:mysql://:3306/bd?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=UTC");
        config.setJdbcUrl("jdbc:mysql://192.168.52.64:3306/guangming_test?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=UTC");
        config.setUsername("root");
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setPassword("123456");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmptCacheSqlLimit", "2048");

        return config;
    }

    public static void handleShowWord() throws Exception {
        //test1("farming", "C:\\Users\\cnki52\\Desktop\\光明\\user-show\\farming 20词.txt");
        //test1("fishery", "C:\\Users\\cnki52\\Desktop\\光明\\user-show\\fishery 20词.txt");
        test1("food", "C:\\Users\\cnki52\\Desktop\\光明\\user-show\\food 20词.txt");
        //test1("livestock", "C:\\Users\\cnki52\\Desktop\\光明\\user-show\\livestock.txt - 畜牧业");
    }

    // 展示用户特定的数据。
    // 先总的全部赋值：   MATCH (n:`pro2022game`) set n.usershow = 0,n.belongdb = ''
    // where r is null  或者 has (n.someproperty)
    public static void test1(String belongDb, String file) throws Exception {
        String content = FileHelper.readTxtFile(file);
        content = content.replace("null", "");
        String[] split1 = content.split("\r\n");
        for (String item : split1) {
            handle(item, belongDb);
        }
        System.out.println("over");
    }

    // MATCH (n:`pro2022game`) where n.usershow = 1 and n.belongdb = 'farming'
    public static void handle(String word, String belongDb) {
        String sql = "MATCH (n:`pro2022game`) where n.name = '生物防治' RETURN n LIMIT 250".replace("生物防治", word);
        List<Map<String, Object>> maps = neo4jHelper.GetValue(sql);
        if (maps != null && maps.size() > 0) {
            System.out.println("handle");
            if(word.equals("营养物质消化率")){
                System.out.println("here");
            }
            for (Map<String, Object> map : maps) {
                Iterable<String> list = (Iterable<String>)map.get("sysLabels");
                ArrayList<String> listArray = Lists.newArrayList(list);
                String belongDbInNode = map.get("belongdb").toString();
                if(!listArray.contains(belongDbInNode)){
//                    System.out.println(map);
//                    String id = map.get("uuid").toString();
//                    sql = "MATCH (n:`pro2022game`) where n.name = '土壤有机碳' and id(n) = 123 set n.usershow = 0"
//                        .replace("土壤有机碳", word)
//                       .replace("123", id);
//                    neo4jHelper.excuteCypherSql(sql);
                }else{
                    if(maps.size() > 1){
                        System.out.println(maps);
                    }
                }
                //System.out.println(map.toString());
//                sql = "MATCH (n:`pro2022game`) where n.name = '土壤有机碳' set n.usershow = 1,n.belongdb = 'farming'"
//                        .replace("土壤有机碳", word)
//                        .replace("farming", belongDb);
//                neo4jHelper.excuteCypherSql(sql);
            }
        } else {
            System.out.println("not found:" + word);
        }
    }

    static void getSecondWord() throws IOException {
        String wordStr = "生物防治,团聚体稳定性,Le Bissonnais法,联合收获机,亚洲柑橘木虱,水盐分布,放水冲刷,轻组有机碳,土壤系统分类,CSLE模型,自动移栽机,RWEQ模型,气力式排种器,土壤养分,工程堆积体,腐殖质组成,有机碳组分,小麦叶锈菌,农业信息处理,长期施肥,土壤理化参数,秸秆还田,土壤侵蚀,水动力学参数,碳库管理指数,超高效液相色谱-串联质谱法,血清生化,啤酒酵母,草地贪夜蛾,亚高山森林,生长性能,红酵母,基因克隆,遗传多样性,表达分析,预测微生物学,环糊精葡萄糖基转移酶,原核表达,纳豆芽孢杆菌,高通量测序,猪繁殖与呼吸综合征病毒(PRRSV),乳酸菌,胆盐水解酶,Zener-Hollomon参数,高效液相色谱-串联质谱法(HPLC-MS\\/MS),R2R3-MYB转录因子,直投式发酵剂,全基因组关联分析(GWAS),Le Bissonnais法,Plackett-Burman试验,South China Sea,海上风电,陆海统筹,大洋钻探计划,海洋经济,潮汐不对称,海洋教育,全球海洋中心城市,IODP 349,SWAN模式,罗氏沼虾,海水淡化,Kuroshio intrusion,辐射沙脊群,波浪能,海洋经济强省,线粒体DNA控制区,海洋动力环境,遗传多样性,海洋产业,能量回收装置,SWAN模型,声速剖面,俘获宽度比,海洋地质调查,非洲猪瘟病毒(ASFV),大肠埃希氏菌,血清生化指标,非洲猪瘟病毒,产蛋性能,血清生化,猪繁殖与呼吸综合征病毒(PRRSV),禽致病性大肠杆菌,伪狂犬病病毒(PRV),猪流行性腹泻病毒(PEDV),新城疫病毒(NDV),免疫过氧化物酶单层细胞试验,纳米抗体,猪圆环病毒3型,犬细小病毒(CPV),TaqMan荧光定量PCR,F1L基因,H9N2亚型禽流感病毒,阻断ELISA,血清4型禽腺病毒,Marc-145细胞,脂质代谢,VP2蛋白,ORF5基因,ORF3基因";
        String[] arr = wordStr.split(",");
        for (String word : arr) {
            getSecondWord(word);
        }
        save();
    }

    static void getSecondWord(String word) {
        //neoType -> NODE
        String sql = "MATCH (n:`20221109`)<-[r]->(b) where n.name = '生物防治' RETURN n,b LIMIT 250".replace("生物防治", word);
        List<Map<String, Object>> maps = neo4jHelper.GetValue(sql);
        List<Map<String, Object>> nodes = maps.stream().distinct().collect(Collectors.toList());

        String relSql = "MATCH (n:`20221109`)<-[r]->(b) where n.name = '生物防治' RETURN r LIMIT 250".replace("生物防治", word);
        List<Map<String, Object>> relMaps = neo4jHelper.GetValue(relSql);

        //System.out.println(maps);

        for (Map<String, Object> rel : relMaps) {

            String sourceid = rel.get("sys_sourceid").toString();
            String targetid = rel.get("sys_targetid").toString();
            String count = rel.get("count").toString();

            Map<String, Object> node1 = getNode(nodes, sourceid);
            String name1 = node1.get("name").toString();
            String uuid1 = node1.get("uuid").toString();
            String score1 = node1.get("score").toString();

            Map<String, Object> node2 = getNode(nodes, targetid);
            String name2 = node2.get("name").toString();
            String uuid2 = node2.get("uuid").toString();
            String score2 = node2.get("score").toString();

            List<Object> oneRecord = new ArrayList<>();

            if (name1.equals(word)) {
                oneRecord.addAll(Arrays.asList(new Object[]{word, name2, Long.parseLong(count), Double.parseDouble(score2)}));
            }
            if (name2.equals(word)) {
                oneRecord.addAll(Arrays.asList(new Object[]{word, name1, Long.parseLong(count), Double.parseDouble(score1)}));
            }

            objects.add(oneRecord);
        }
    }

    static void save() throws IOException {
        Excel2007Utils.writeExcelData("E:\\", "guangming", "Sheet1", objects);
    }

    static Map<String, Object> getNode(List<Map<String, Object>> nodes, String targetUUID) {
        for (Map<String, Object> node : nodes) {
            String name = node.get("name").toString();
            String uuid = node.get("uuid").toString();
            String score = node.get("score").toString();

            if (uuid.equals(targetUUID)) {
                return node;
            }
        }
        return null;
    }

    static Neo4jHelper initHelper() {
        Driver driver = GraphDatabase.driver("bolt://192.168.52.63:17687", AuthTokens.basic("neo4j", "ABCDabcd1234@"));
        return new Neo4jHelper(driver);
    }

    static Neo4jHelper initOnlineHelper() {
        Driver driver = GraphDatabase.driver("bolt://10.122.109.124:17687", AuthTokens.basic("neo4j", "ABCDabcd1234@"));
        return new Neo4jHelper(driver);
    }
}
