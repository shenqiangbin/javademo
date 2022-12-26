package dbmgr.kbaseAccess;

import com.fasterxml.jackson.core.json.async.NonBlockingJsonParser;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import common.P;
import dbmgr.mySqlAccess.MySqlHelper;
import org.apache.commons.lang3.StringUtils;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class KBaseHelperGuanMing {

    static KBaseHelper kBaseHelper = new KBaseHelper("jdbc:kbase://10.120.150.189", "DBOWN", "kbase189");
    static MySqlHelper mysqlHelper = getMysqlHelper();
    static int sqlcount = 0;
    static int alreayCount = 0;

    public static void main(String[] args) throws Exception {
        P.print("ok");
        //guangmingAuthor();
        //getArticles();
        setAuthorGHNum();
        //testSaveAuthor();
        kBaseHelper.dispose();
        System.out.println("sqlcount:" + sqlcount);
        System.out.println("alreayCount:" + alreayCount);
    }

    public static void getArticles() throws SQLException {
        String word = "超高效液相色谱-串联质谱法";
        getArticlesByWord(word);
    }

    public static void getArticlesByWord(String word) throws SQLException {
        String format = "select 作者代码,机构代码,篇名,作者,第一责任人,第一作者H指数,作者简介,新作者代码,机构,第一机构,新机构作者代码 from cjfdtotal" +
                " where 关键词 = '%s' and 年 >= 2018";
        String sql = String.format(format, word);
        String[] fields = new String[]{"篇名", "作者", "第一责任人", "第一作者H指数", "作者简介", "新作者代码", "机构", "第一机构", "新机构作者代码",};
        fields = new String[]{"作者", "新作者代码", "作者代码", "机构代码", "机构", "新机构作者代码",};

        List<LinkedHashMap<String, Object>> list = kBaseHelper.query(sql, fields);
        System.out.println("总共的数量为：" + list.size());
        for (LinkedHashMap<String, Object> item : list) {
            //P.print(item.toString());
            saveAuthor(word, item, list);
        }
    }

    public static void saveAuthor(String word, LinkedHashMap<String, Object> item, List<LinkedHashMap<String, Object>> list) throws SQLException {
        Object author = item.get("作者");
        Object authorNewCode = item.get("新作者代码");
        Object authorCode = item.get("作者代码");
        Object orgCode = item.get("机构代码");
        Object org = item.get("机构");
        Object orgAuthorNewCode = item.get("新机构作者代码");

        String firstAuthor = ((String) author).split(";")[0];
        String firstAuthorCode = null;
        if (authorCode != null) {
            firstAuthorCode = ((String) authorCode).split(";")[0];
        }
        String firstAuthorNewCode = null;
        if (authorNewCode != null) {
            // 使用的新编码
            firstAuthorNewCode = ((String) authorNewCode).split(";")[0];
        }
        String firstOrgCode = null;
        if (orgCode != null) {
            firstOrgCode = ((String) orgCode).split(";")[0];
        }
        String firstOrgNewCode = null;
        if (orgAuthorNewCode != null) {
            String firstOrgAuthorNewCode = ((String) orgAuthorNewCode).split(";")[0];
            firstOrgNewCode = firstOrgAuthorNewCode.split(":")[0];
        }

        int appearCount = calCountInList(firstAuthorCode, firstAuthorNewCode, list);

        saveAuthor(word, firstAuthor, firstAuthorCode, firstAuthorNewCode, appearCount);

        System.out.println(author);

    }

    public static int calCountInList(String firstAuthorCode, String firstAuthorNewCode, List<LinkedHashMap<String, Object>> list) {
        int count = 0;
        for (LinkedHashMap<String, Object> item : list) {
            //P.print(item.toString());
            Object authorNewCode = item.get("新作者代码");
            if (authorNewCode != null
                    && StringUtils.isNotBlank(authorNewCode.toString())
                    && StringUtils.isNotBlank(firstAuthorNewCode)
            ) {
                if (authorNewCode.toString().contains(firstAuthorNewCode)) {
                    count++;
                    continue;
                }
            }
            Object authorCode = item.get("作者代码");
            if (authorCode != null
                    && StringUtils.isNotBlank(authorCode.toString())
                    && StringUtils.isNotBlank(firstAuthorCode)
            ) {
                if (authorCode.toString().contains(firstAuthorCode)) {
                    count++;
                    continue;
                }
            }
        }
        return count;
    }

    public static void testSaveAuthor() {
        try {
            saveAuthor("超高效液相色谱-串联质谱法", "张三", "12313", null,1);
            saveAuthor("超高效液相色谱-串联质谱法", "张三", "123134", null, 1);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void saveAuthor(String word, String author, String authorCode, String authorNewCode, int appearCount) throws SQLException {
        String sql = "insert data_word_author(word,author,authorCode,authorNewCode,articleNum) values(?,?,?,?,?)";
        List<Object> insertList = new ArrayList<>();
        insertList.add(word);
        insertList.add(author);
        insertList.add(authorCode);
        insertList.add(authorNewCode);
        insertList.add(appearCount);
        sqlcount++;
        try {
            mysqlHelper.execute(sql, insertList);
        } catch (SQLIntegrityConstraintViolationException e) {
            if (e.toString().contains("Duplicate entry")) {
                // do nothing
                System.out.println("already exists");
                alreayCount++;
            } else {
                throw e;
            }
        }
    }

    public static MySqlHelper getMysqlHelper() {

        HikariConfig config = new HikariConfig();

        //config.setJdbcUrl("jdbc:mysql://:3306/bd?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=UTC");
        config.setJdbcUrl("jdbc:mysql://192.168.52.64:3306/guangming_test?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=UTC");
        config.setUsername("root");
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setPassword("123456");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmptCacheSqlLimit", "2048");

        HikariDataSource dataSource = new HikariDataSource(config);
        MySqlHelper mySqlHelper = new MySqlHelper(dataSource);
        return mySqlHelper;
    }

    public static void guangmingAuthor() throws SQLException {

        String authors = "励建荣;谢晶;孔保华;江连洲;赵谋明;熊善柏;徐岩;王兴国;白卫东;杨贤庆;姜绍通;李琳;李杨;陈卫;陈复生;李来好;赵改名;吴燕燕;徐幸莲;张和平;孙宝国;李洪军;王锡昌;张灏;任发政;赵思明;李崎;金青哲;薛长湖;潘思轶";
        authors = "谢晶;孔保华;江连洲;赵谋明;熊善柏;徐岩;王兴国;白卫东;杨贤庆;姜绍通;李琳;李杨;陈卫;陈复生;李来好;赵改名;吴燕燕;徐幸莲;张和平;孙宝国;李洪军;王锡昌;张灏;任发政;赵思明;李崎;金青哲;薛长湖;潘思轶";

        String format = "select 专家姓名,学者,H指数,G指数,当前职称,学者职称,职称级别,研究方向,研究领域,第一作者篇数,第一学者篇数,是否专家数据 from newauthor1,newauthor2,newauthor3,newauthor4,newauthor5,newauthor6,newauthor7, newauthorl where 专家姓名 = '励建荣' limit 10";

        for (String author : authors.split(";")) {
            String sql = format.replace("励建荣", author);

            String[] args = new String[]{"专家姓名", "学者", "H指数", "G指数", "当前职称", "学者职称", "职称级别", "研究方向", "研究领域", "第一作者篇数", "第一学者篇数",
                    "是否专家数据"};
            args = new String[]{"专家姓名", "学者", "H指数", "G指数", "当前职称", "学者职称", "是否专家数据"};
            List<LinkedHashMap<String, Object>> list = kBaseHelper.query(sql, args);
            for (LinkedHashMap<String, Object> item : list) {
                P.print(item.toString());
            }
        }


    }


    // 获取作者其他属性

    public static void setAuthorGHNum() throws Exception {
        String sql = "select * from data_word_author";

        List<LinkedHashMap<String, Object>> list = mysqlHelper.simpleQuery(sql, null);
        System.out.println("总共的数量为：" + list.size());
        for (LinkedHashMap<String, Object> item : list) {
            Object id = item.get("id");
            Object author = item.get("author");
            Object authorCode = item.get("authorCode");
            Object authorNewCode = item.get("authorNewCode");

           if(authorNewCode != null){
               String format = "select 专家姓名,学者,H指数,G指数,当前职称,学者职称,职称级别,研究方向,研究领域,第一作者篇数,第一学者篇数,是否专家数据 from " +
                       "newauthor1,newauthor2,newauthor3,newauthor4,newauthor5,newauthor6,newauthor7, newauthorl " +
                       "where 专家编号 = '000026151548' or  学者代码 = '000026151548' limit 10";

               String sql2 = format.replace("000026151548", authorNewCode.toString());

               String[] args = new String[]{"专家姓名", "学者", "H指数", "G指数", "当前职称", "学者职称", "职称级别", "研究方向", "研究领域", "第一作者篇数", "第一学者篇数",
                       "是否专家数据"};
               args = new String[]{"专家姓名", "学者", "H指数", "G指数", "当前职称", "学者职称", "是否专家数据"};
               List<LinkedHashMap<String, Object>> authorList = kBaseHelper.query(sql2, args);
               for (LinkedHashMap<String, Object> authorItem : authorList) {
                   P.print(authorItem.toString());

                   Object hNum = authorItem.get("H指数");
                   Object gNum = authorItem.get("G指数");
                   Object masterFlag = authorItem.get("是否专家数据");
                   updateAuthor(id, hNum, gNum, masterFlag);
               }
           }

        }
    }

    public static void updateAuthor(Object id,Object hNum,Object gNum,Object masterFlag) throws SQLException {
        String sql = "update data_word_author set hNum = ?,gNum = ?,masterFlag = ? where id = ?";
        mysqlHelper.update(sql,new Object[]{hNum, gNum, masterFlag,id});

    }
}


