package dbmgr.mySqlAccess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 运行程序查看转化效果
 */
public class ColUtil {

    public static void main(String[] args) {
        String content = "报纸_报纸名称              \n" +
                "报纸_年                 \n" +
                "报纸_期号                \n" +
                "报纸_版号                \n" +
                "期刊_期刊名称              \n" +
                "期刊_机构                \n" +
                "期刊_年                 \n" +
                "期刊_期                 \n" +
                "期刊_ISSN              \n" +
                "期刊_CN                \n" +
                "图书_ISBN              \n" +
                "图书_版次                \n" +
                "一手分类                 ";
        System.out.println(toSplitWith(content));
        System.out.println(toKbasePara(content));

        // 去掉前后空格，然后生成 update 语句中的内容
        System.out.println(handleContent("", content, "= '%s', ", false).replace("\r\n", ""));


        content = "    newspaper_name\n" +
                "    newspaper_year\n" +
                "    newspaper_qihao\n" +
                "    newspaper_banhao\n" +
                "    periodical_name\n" +
                "    periodical_org\n" +
                "    periodical_year\n" +
                "    periodical_period\n" +
                "    periodical_issn\n" +
                "    periodical_cn\n" +
                "    book_isbn\n" +
                "    book_edition\n" +
                "    category";
        System.out.println(handleContent("model.get", content, "(),", true));

        String cols = "sys_fld_sysid,标题,作者,来源,关键词,摘要,全文,所属专题,课题组,数据类型,状态,发布时间,上传人,上传时间,引证标题,引证内容,语言,SYS_FLD_DIGITFILENAME," +
                "报纸_报纸名称,报纸_年,报纸_期号,报纸_版号,期刊_期刊名称,期刊_机构,期刊_年,期刊_期,期刊_ISSN,期刊_CN,图书_ISBN,图书_版次,一手分类";
        String colNames = "    id\n" +
                "    title\n" +
                "    author\n" +
                "    fromVal\n" +
                "    keyWord\n" +
                "    summary\n" +
                "    fullText\n" +
                "    belongSubject\n" +
                "    subjectGroup\n" +
                "    dataState\n" +
                "    state\n" +
                "    publicTime\n" +
                "    uploadUser\n" +
                "    uploadTime\n" +
                "    referTitle\n" +
                "    referContent\n" +
                "    language\n" +
                "    fileName\n" +
                "    newspaper_name\n" +
                "    newspaper_year\n" +
                "    newspaper_qihao\n" +
                "    newspaper_banhao\n" +
                "    periodical_name\n" +
                "    periodical_org\n" +
                "    periodical_year\n" +
                "    periodical_period\n" +
                "    periodical_issn\n" +
                "    periodical_cn\n" +
                "    book_isbn\n" +
                "    book_edition\n" +
                "    category\n";

        String r = concat(cols, colNames, "as");
        System.out.println(r);

        String some = "keyWord\n" +
                "关键词\n" +
                "summary\n" +
                "摘要\n" +
                "fullText\n" +
                "全文\n" +
                "belongSubject\n" +
                "所属专题\n" +
                "publicTime\n" +
                "发布时间/成果完成时间\n" +
                "referTitle\n" +
                "引证标题 (多个以 | 分割）\n" +
                "referContent\n" +
                "引证内容 (多个以 | 分割）\n" +
                "language\n" +
                "语言\n" +
                "newspaper_name\n" +
                "报纸名称\n" +
                "newspaper_year\n" +
                "报纸年\n" +
                "newspaper_qihao\n" +
                "报纸_期号\n" +
                "newspaper_banhao\n" +
                "报纸_版号\n" +
                "periodical_name\n" +
                "期刊_期刊名称\n" +
                "periodical_org\n" +
                "期刊_机构\n" +
                "periodical_year\n" +
                "期刊_年\n" +
                "periodical_period\n" +
                "期刊_期\n" +
                "periodical_issn\n" +
                "期刊_ISSN\n" +
                "periodical_cn\n" +
                "期刊_CN\n" +
                "book_isbn\n" +
                "图书_ISBN\n" +
                "book_edition\n" +
                "图书_版次\n" +
                "category\n" +
                "一手分类";
        String result = concatOneAndTwo(some);
        System.out.println(result);
    }

    /**
     * 将下面格式的列进行转换
     * <p>
     * 报纸_报纸
     * 报纸_年
     * 报纸_期号
     * 报纸_版号
     * 期刊_期刊
     * 期刊_机构
     * 期刊_年
     * 期刊_期
     * 期刊_IS
     * 期刊_CN
     * 图书_IS
     * 图书_版次
     * 一手分类
     */
    public static String toSplitWith(String content) {
        return toSplitWith(content, ",");
    }

    public static String toSplitWith(String content, String split) {
        String result = content.replace("\r\n", split).replace("\n", split);
        List<String> arr = Arrays.stream(result.split(split)).map(m -> m.trim()).collect(Collectors.toList());
        return String.join(split, arr);
    }

    public static String toKbasePara(String content) {
        String result = toSplitWith(content);
        int length = result.split(",").length;
        String[] paraArr = new String[length];
        for (int i = 0; i < paraArr.length; i++) {
            paraArr[i] = "'%s'";
        }
        return String.join(",", paraArr);
    }

    public static String handleContent(String prefix, String content, String suffix, boolean upperFirstChar) {
        String result = toSplitWith(content);
        String[] arr = result.split(",");
        List<String> list = Arrays.stream(arr).map(m -> prefix + (upperFirstChar ? toUpperCaseFirstOne(m) : m) + suffix).collect(Collectors.toList());
        return String.join("\r\n", list);
    }

    private static String toUpperCaseFirstOne(String s) {
        if (Character.isUpperCase(s.charAt(0)))
            return s;
        else
            return (new StringBuilder()).append(Character.toUpperCase(s.charAt(0))).append(s.substring(1)).toString();
    }

    public static String concat(String cols, String colNames, String concatChar) {
        String colsResult = toSplitWith(cols);
        String colNamesResult = toSplitWith(colNames);

        String[] colsArr = colsResult.split(",");
        String[] colNamesArr = colNamesResult.split(",");

        if (colsArr.length != colNamesArr.length)
            throw new RuntimeException("两者长度不相同");

        List<String> list = new ArrayList<>();
        for (int i = 0; i < colsArr.length; i++) {
            list.add(colsArr[i] + " " + concatChar + " " + colNamesArr[i]);
        }

        return String.join(",", list);

    }
    public static String concatOneAndTwo(String content){
        String val = toSplitWith(content);
        String[] colsArr = val.split(",");

        List<String> list = new ArrayList<>();
        for (int i = 0; i < colsArr.length; i=i+2) {
            list.add("|title\t|String| 标题".replace("title",colsArr[i]).replace("标题",colsArr[i+1]));
        }
        return String.join("\n", list);
    }
}
