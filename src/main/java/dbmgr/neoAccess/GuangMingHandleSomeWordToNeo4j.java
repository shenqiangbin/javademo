package dbmgr.neoAccess;

import com.alibaba.fastjson.JSONObject;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import dbmgr.mySqlAccess.MySqlHelper;
import fileDemo.FileHelper;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


public class GuangMingHandleSomeWordToNeo4j {

    static Neo4jHelper neo4jHelper = initOnlineHelper();

    static HikariDataSource dataSourceOnline = new HikariDataSource(getOnlineConfig());
    static MySqlHelper mySqlHelperOnline = new MySqlHelper(dataSourceOnline);

    static HikariDataSource dataSource = new HikariDataSource(getConfig());
    static MySqlHelper mySqlHelper = new MySqlHelper(dataSource);
    static List<List<Object>> params = new ArrayList<>();
    static List<String> existsList = new ArrayList<>();
    static StringBuilder mergeBuilder = new StringBuilder();
    static int existsCount = 0;

    /*
       处理一些特殊的词，保证在图谱中有数据
    * */
    public static void main(String[] args) throws Exception {

        for (String word : getWords()) {
            List<String> secondWords = handleWord(word, false);
            for (String secondWord : secondWords) {
                System.out.println("处理:" + secondWord);
                List<String> thirdWords = handleWord(secondWord, true);
            }
        }

    }

    /**
     * 修改这两个地方
     */
    static String batch = "pro2022game-20221213";

    public static String[] getWords() {
        //return new String[]{"浮式风机"};
        //return new String[]{"能量回收装置"};
        //return new String[]{"反渗透海水淡化"};
        return new String[]{"尾流振子模型","海流能","远洋渔业基地","波浪能转换装置","侧扫声呐","长江河口"};
    }


    public static List<String> handleWord(String word, boolean startLimit) throws Exception {
        List<String> records = getRecords(word);
        List<AppearModel> appearModels = calCount(records, word, startLimit);
        // 保存到图数据库
        save(appearModels);

        System.out.println("处理关键词的作者和机构");
        // 处理关键词的作者和机构
        getAuthorOrgRecords(word);

        List<String> nextWords = appearModels.stream().map(m -> m.getWordB()).collect(Collectors.toList());
        return nextWords;
    }


    static List<String> getRecords(String word) throws Exception {
        List<String> result = new ArrayList<>();

        String sql = "select keyword from gm_articles_has_author where keywordhandle like ?";
        String wordPara = "%;;" + word + ";;%";
        List<LinkedHashMap<String, Object>> list = mySqlHelper.simpleQuery(sql, new String[]{wordPara});
        for (LinkedHashMap<String, Object> item : list) {
            String keywordField = toEmptyIfNULL(item.get("keyword"));
            result.addAll(Arrays.asList(keywordField.split(";;")));
        }

        return result;
    }

    private static List<AppearModel> calCount(List<String> records, String word, boolean startLimit) {
        Map<String, Long> result = records.stream().collect(
                Collectors.groupingBy(Function.identity(), Collectors.counting())
        );
        System.out.println(result);

        List<Map.Entry<String, Long>> list = new ArrayList(result.entrySet());
        list.sort((a, b) -> {
            return b.getValue().compareTo(a.getValue());
        });

        if (startLimit) {
            if (list.size() > 10) {
                list = list.subList(0, 10);
            }
        }

        List<AppearModel> appearModelList = new ArrayList<>();

        for (Map.Entry<String, Long> entry : list) {
            String key = entry.getKey();
            Long val = entry.getValue();

            if (key.equals(word)) {
                continue;
            }

            AppearModel appearModel = new AppearModel();
            appearModel.setWordA(word);
            appearModel.setWordB(key);
            appearModel.setCount(val);
            appearModelList.add(appearModel);
        }

        return appearModelList;
    }

    //浮式风机
    private static void save(List<AppearModel> appearModels) {

        for (AppearModel appearModel : appearModels) {

            String wordA = appearModel.getWordA();
            String wordB = appearModel.getWordB();
            System.out.println("处理词：" + wordA + ":::" + wordB);

            if (!exists(appearModel.getWordA())) {
                String sql = "MERGE(n:`20221010` { belong: 'cnki', name:'奶牛乳房炎', type:210, userfocus:0, score: 0, createuser:'javademo' });";
                sql = sql.replace("20221010", batch)
                        .replace("奶牛乳房炎", appearModel.getWordA());
                neo4jHelper.excuteCypherSql(sql);
            }

            if (!exists(appearModel.getWordB())) {
                String sql = "MERGE(n:`20221010` { belong: 'cnki', name:'奶牛乳房炎', type:210, userfocus:0, score: 0, createuser:'javademo' });";
                sql = sql.replace("20221010", batch)
                        .replace("奶牛乳房炎", appearModel.getWordB());
                neo4jHelper.excuteCypherSql(sql);

            }

            if (!existsRel(appearModel.getWordA(), appearModel.getWordB())) {
                String relSql = "MATCH (a:`20221010`{type:210, name:'牛肉'}),(b:`20221010`{type:210, name:'牛羊养殖'}) " +
                        "     CREATE (a)-[r:RELTYPE { count: 900 }] -> (b) RETURN r";
                relSql = relSql.replace("20221010", batch)
                        .replace("牛肉", appearModel.getWordA())
                        .replace("牛羊养殖", appearModel.getWordB())
                        .replace("900", appearModel.getCount().toString());

                neo4jHelper.excuteCypherSql(relSql);
            }

        }

    }

    private static boolean exists(String word) {
        String sql = "MATCH(n:`20221010` { belong: 'cnki', name:'奶牛乳房炎'}) return n";
        sql = sql.replace("20221010", batch)
                .replace("奶牛乳房炎", word);
        List<Map<String, Object>> maps = neo4jHelper.GetValue(sql);
        return maps != null && maps.size() > 0;
    }

    private static boolean existsRel(String wordA, String wordB) {
        String sql = "MATCH (a:`20221010`{type:210, name:'牛肉'}) <-[r]-> (b:`20221010`{type:210, name:'牛羊养殖'}) return r";
        sql = sql.replace("20221010", batch)
                .replace("牛肉", wordA)
                .replace("牛羊养殖", wordB);
        List<Map<String, Object>> maps = neo4jHelper.GetValue(sql);
        return maps != null && maps.size() > 0;
    }

    public static String toEmptyIfNULL(Object str) {
        return str == null ? "" : str.toString();
    }

    public static String getParams(int size) {
        String[] arr = new String[size];
        for (int i = 0; i < size; i++) {
            arr[i] = "?";
        }
        return String.join(",", arr);
    }

    //获取作者机构信息
    static void getAuthorOrgRecords(String word) throws Exception {
        List<String> result = new ArrayList<>();
        List<String> orgresult = new ArrayList<>();

        String sql = "select author,org from gm_articles_has_author where keywordhandle like ?";
        String wordPara = "%;;" + word + ";;%";
        List<LinkedHashMap<String, Object>> list = mySqlHelper.simpleQuery(sql, new String[]{wordPara});
        for (LinkedHashMap<String, Object> item : list) {
            String authorField = toEmptyIfNULL(item.get("author"));
            String orgField = toEmptyIfNULL(item.get("org"));

            result.addAll(Arrays.asList(authorField.split(";")));
            orgresult.addAll(Arrays.asList(orgField.split(";")));
        }

        for(String author: result){
            saveAuthorOrg(word, author, "", "0");
        }
        for(String org : orgresult){
            saveAuthorOrg(word, org, "", "1");
        }

    }

    static void saveAuthorOrg(String word, String name, String score, String type) throws SQLException {

        String sql = "select count(0) from dwd_expert_ins_rel where  word = ? AND name = ?";
        List<Object> countList = new ArrayList<>();
        countList.add(word);
        countList.add(name);
        String s = mySqlHelperOnline.executeScalar(sql, countList);

        // 已存在则跳过
        if(Integer.parseInt(s) > 0){
            return;
        }sql = "INSERT INTO `dwd_expert_ins_rel` (`word`, `name`, `score`, `type`, `master`, `db`, `rel_id`, `createTime`, `moifyTime`) " +
                "VALUES (?, ?, ?, ?,  0, 'javademoBuild', NULL, '2022-11-23 10:03:00', '2022-11-23 10:03:00');";
        List<Object> list = new ArrayList<>();
        list.add(word);
        list.add(name);
        list.add(score);
        list.add(type);
        mySqlHelperOnline.execute(sql, list);
    }


    public static HikariConfig getConfig() {

        HikariConfig config = new HikariConfig();

        //config.setJdbcUrl("jdbc:mysql://:3306/bd?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=UTC");
        config.setJdbcUrl("jdbc:mysql://192.168.20.154:13306/gmgj_mid_data?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=UTC");
        config.setUsername("root");
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setPassword("123456");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmptCacheSqlLimit", "2048");

        return config;
    }

    static Neo4jHelper initOnlineHelper() {
        Driver driver = GraphDatabase.driver("bolt://10.122.109.124:17687", AuthTokens.basic("neo4j", "ABCDabcd1234@"));
        return new Neo4jHelper(driver);
    }

    public static HikariConfig getOnlineConfig() {

        HikariConfig config = new HikariConfig();

        //config.setJdbcUrl("jdbc:mysql://:3306/bd?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=UTC");
        //config.setJdbcUrl("jdbc:mysql://192.168.52.64:3306/gmgj_release_202211?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=UTC");
        config.setJdbcUrl("jdbc:mysql://10.122.109.124:3306/gmgj_release_20221123?useSSL=false&serverTimezone=Asia/Shanghai&allowMultiQueries=true");
        config.setUsername("root");
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setPassword("ABCDabcd1234@");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmptCacheSqlLimit", "2048");

        return config;
    }

}
