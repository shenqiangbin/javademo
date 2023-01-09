package dbmgr.neoAccess;

import ExcelDemo.Excel2007Utils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import dbmgr.mySqlAccess.MySqlHelper;
import fileDemo.FileHelper;
import org.apache.commons.lang.StringUtils;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 展示用户指定要求展示的关键词
 */
public class GuangMingShowUser {

    //static Neo4jHelper neo4jHelper = initHelper();
    static Neo4jHelper neo4jHelper = initOnlineHelper();
    static List<List<Object>> objects = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        handleShowWord();
    }

    public static void handleShowWord() throws Exception {
        String batch = "pro2022game-20221213";
        //test1(batch,"farming", "C:\\Users\\cnki52\\Desktop\\光明\\user-show\\farming 20词.txt");
        test1(batch,"fishery", "C:\\Users\\cnki52\\Desktop\\光明\\user-show\\fishery 20词.txt");
        //test1(batch,"food", "C:\\Users\\cnki52\\Desktop\\光明\\user-show\\food 20词.txt");
        //test1(batch,"livestock", "C:\\Users\\cnki52\\Desktop\\光明\\user-show\\livestock.txt - 畜牧业");
    }

    // 展示用户特定的数据。
    // 先总的全部赋值：   MATCH (n:`pro2022game-20221213`) set n.usershow = 0,n.belongdb = ''
    // where r is null  或者 has (n.someproperty)
    public static void test1(String batch,String belongDb, String file) throws Exception {
        String content = FileHelper.readTxtFile(file);
        content = content.replace("null", "");
        String[] split1 = content.split("\r\n");
        for (String item : split1) {
            handle(item, belongDb, batch);
        }
        System.out.println("over");
    }

    // MATCH (n:`pro2022game`) where n.usershow = 1 and n.belongdb = 'farming'
    public static void handle(String word, String belongDb, String batch) {
        String sql = "MATCH (n:`pro2022game`) where n.name = '生物防治' RETURN n LIMIT 250"
                .replace("pro2022game", batch)
                .replace("生物防治", word);
        List<Map<String, Object>> maps = neo4jHelper.GetValue(sql);
        if (maps != null && maps.size() > 0) {
            System.out.println("handle");
            if(word.equalsIgnoreCase("侧扫声呐")){
                System.out.println("here");
            }
            for (Map<String, Object> map : maps) {
                Iterable<String> list = (Iterable<String>)map.get("sysLabels");
                ArrayList<String> listArray = Lists.newArrayList(list);
                String belongDbInNode = map.get("belongdb").toString();
                if(StringUtils.isBlank(belongDbInNode)){
                    if(maps.size() > 1){
                        System.out.println("有多个值");
                        System.out.println(maps);
                    }
                    if(listArray.contains(belongDb)){
                        System.out.println(map.toString());
                        String id = map.get("uuid").toString();

                        sql = "MATCH (n:`pro2022game`) where n.name = '土壤有机碳'  and id(n) = {id} set n.usershow = 1,n.belongdb = 'farming'"
                                .replace("pro2022game", batch)
                                .replace("{id}", id)
                                .replace("土壤有机碳", word)
                                .replace("farming", belongDb);
                        neo4jHelper.excuteCypherSql(sql);
                    }
                }else {

                    if (!listArray.contains(belongDbInNode)) {
                        System.out.println("here");
                        // 保证没更新其它图谱，四个不同的图谱可能有相同的词

//                    System.out.println(map);
//                    String id = map.get("uuid").toString();
//                    sql = "MATCH (n:`pro2022game`) where n.name = '土壤有机碳' and id(n) = 123 set n.usershow = 0"
//                        .replace("土壤有机碳", word)
//                       .replace("123", id);
//                    neo4jHelper.excuteCypherSql(sql);
                    } else {

                    }
                }
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
