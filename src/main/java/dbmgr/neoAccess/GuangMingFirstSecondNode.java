package dbmgr.neoAccess;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import dbmgr.mySqlAccess.MySqlHelper;
import fileDemo.FileHelper;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 导出图谱上面一级二级词。
 */
public class GuangMingFirstSecondNode {
    static Neo4jHelper neo4jHelper = initOnlineHelper();
    //    static HikariDataSource dataSource = new HikariDataSource(getConfig());
    static MySqlHelper mySqlHelper = new MySqlHelper(null);
    static List<String> wordbuilder = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        String batch = "pro2022game-1126";
        batch = "pro2022game-20221213";
        mergeDbFirstLevel(batch);

        System.out.println("================");

        batch = "merge-20221124";
        batch = "merge-20221213";
        mergeDbFirstLevel(batch);

        //System.out.println("word list:");
        String str = wordbuilder.stream().distinct().collect(Collectors.joining("\r\n"));
        //System.out.println(str);

        String file = "D:\\code\\TPI\\大数据产品\\光明国际\\数据处理\\机构作者\\word.txt";
        //FileHelper.writeTxtFile(str, file, false, false);

    }

    public static String getNeoSQl(String batch) {

        // CNKi 数据 （cnki这里）
        String sql = String.format("MATCH (a:`%s`) where  a.type = %s and a.check = 1 and a.usershow = 1 return a  order by a.score desc limit 1000",
                batch,
                210);

        // 包含 merge、被认为是看光明国际自己的数据
        if (batch.contains("merge")) {
            sql = String.format("MATCH (a:`%s`) where  a.type = %s and a.userfocus = 1 and a.check = 1 return a  order by a.score desc limit 1000",
                    batch,
                    210);
        }

        return sql;
    }

    /* 用户 merge 库； 用户热词；图谱一级词汇 */
    public static void mergeDbFirstLevel(String batch) throws Exception {
        String sql = getNeoSQl(batch);

        List<Map<String, Object>> cnkiList = neo4jHelper.GetValue(sql);
        int i = 1;
        for (Map<String, Object> item : cnkiList) {
            String uuid = item.get("uuid").toString();
            String neoType = item.get("neoType").toString();
            String name = item.get("name").toString();
            System.out.println("1:" + name);
            //sixiangxianHasVal(name);
            wordbuilder.add(name);
        }

        List<String> firstLevelNodesIds = cnkiList.stream().filter(m -> m.get("neoType").equals(Neo4jHelper.STR_NODE)).map(m -> m.get("uuid").toString()).collect(Collectors.toList());

        for (Map<String, Object> item : cnkiList) {
            String uuid = item.get("uuid").toString();
            String neoType = item.get("neoType").toString();
            String name = item.get("name").toString();
//            wordbuilder.add("--------------" + name);
            List<Map<String, Object>> secondItems = getNextLevelData(batch, uuid, name, true, firstLevelNodesIds);

            for (Map<String, Object> item2 : secondItems) {
                String uuid2 = item2.get("uuid").toString();
                String neoType2 = item2.get("neoType").toString();
                String name2 = item2.get("name").toString();

                System.out.println("1:" + name + ":2:" + name2);

                if (name2.equals("遗传变异")) {

                }

                List<Map<String, Object>> thirdItems = getNextLevelData(batch, uuid2, name2, true, firstLevelNodesIds);
                for (Map<String, Object> item3 : thirdItems) {
                    String uuid3 = item3.get("uuid").toString();
                    String neoType3 = item3.get("neoType").toString();
                    String name3 = item3.get("name").toString();

                    System.out.println("1:" + name + ":2:" + name2 + ":3:" + name3);
                }
            }
        }

    }

    public static List<Map<String, Object>> getNextLevelData(String batch, String nodeid, String parentName, boolean hasChildren, List<String> expectNodeIds) {
        if (expectNodeIds == null || expectNodeIds.size() == 0) {
            expectNodeIds = new ArrayList<>();
            expectNodeIds.add("-1000000000");
        }

        expectNodeIds = expectNodeIds.stream().filter(m -> !m.trim().equalsIgnoreCase(nodeid)).collect(Collectors.toList());

        // 获取下级 20 个词，正式图谱展示时，虽然只是展示10个，但因为会过滤所以有可能某些词包含不上，因此这里获取20个。
        String sql = String.format("MATCH p = (a:`%s`) <- [r] ->(b)  where id(a) = %s and a.check = 1 and none(x in nodes(p) where id(x) in [%s]) return * order by b.score desc  limit %s",
                batch, nodeid, String.join(",", expectNodeIds), 20);
        List<Map<String, Object>> cnkiList = neo4jHelper.GetValue(sql);

        // node 节点去重，且不包含自己
        List<Map<String, Object>> collect = cnkiList.stream()
                .distinct()
                .filter(m -> m != null && m.get("uuid") != null
                        && !m.get("uuid").toString().equals(nodeid)
                        && m.get("neoType").toString().equalsIgnoreCase("node")
                )
                .collect(Collectors.toList());

        for (Map<String, Object> item : collect) {
            if (item == null || item.get("uuid") == null) {
                continue;
            }
            String uuid = item.get("uuid").toString();
            String neoType = item.get("neoType").toString();
            if (neoType.equalsIgnoreCase("node")) {
                String name = item.get("name").toString();
                if (wordbuilder.contains(name)) {
                    continue;
                }
                wordbuilder.add(name);

//                if(hasChildren){
//                    //System.out.println((i++) + ":" + name);
//                    //sixiangxianHasVal(name);
//                    getNextLevelData(batch,uuid, name, false);
//                }
            }
        }

        return collect;
    }

    // 看这个词在四象限是否有数据
    public static void sixiangxianHasVal(String word) throws Exception {

        String sql = "SELECT count(*) FROM coordinate WHERE centor_nodes = ? ORDER BY id DESC LIMIT 10";
        List<Object> params = Arrays.asList(new String[]{word});
        String s = mySqlHelper.executeScalar(sql, params);
        //System.out.println(s);
        if (Integer.parseInt(s) == 0) {
//            boolean fileHasWord = readfile(word);
//            if (fileHasWord) {
//                System.out.println("no data but file has data:" + word);
//            } else {
//                System.out.println("no data and file no data:" + word);
//            }
            System.out.println("no data:" + word);
        } else {
            //System.out.println("has data:" + word);
        }
    }

    static Neo4jHelper initOnlineHelper() {
        Driver driver = GraphDatabase.driver("bolt://10.122.109.124:17687", AuthTokens.basic("neo4j", "ABCDabcd1234@"));
        return new Neo4jHelper(driver);
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
}
