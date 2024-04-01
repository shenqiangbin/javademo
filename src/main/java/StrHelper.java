import com.google.common.collect.Lists;

import java.text.NumberFormat;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StrHelper {

    public static void main(String[] args) {
//        String a= "Steel";
//        String b = "Steel Pipe";
//        System.out.println("相似度："+getSimilarityRatio(a,b));
//
//        String[] str1 = {"今天星期四","12345667890","Steel"};
//        String[] str2 = {"今天是星期五","1234567890","Steel Pipe"};
//        for(int i=0;i<str1.length;i++){
//            levenshtein(str1[i],str2[i]);
//        }


        StringBuilder relInfoSql = new StringBuilder("select company_id,company_original_logo,company_name, company_brief, tag_name, ");
        relInfoSql.append("    corporate_city_name,corporate_district_name, corparate_round_name\n");
        relInfoSql.append("          from dwd_tag_company  where ");
        relInfoSql.append("     company_id in (\n");
        relInfoSql.append("       select distinct company2Id from companies_rel a \n");
        relInfoSql.append("       join dwd_tag_company c on c.company_id = a.companyId\n");
        relInfoSql.append("       where c.company_id =  318387");
//        if (StringUtils.isNotBlank(companyInfoRequest.getTagName())) {
//            relInfoSql.append("   and c.tag_name = '" + companyInfoRequest.getTagName() + "'");
//        }

        relInfoSql.append("       and (a.active is null or a.active='Y')   )");
//        if (StringUtils.isNotBlank(companyInfoRequest.getTagName())) {
//            relInfoSql.append("   and tag_name = '" + companyInfoRequest.getTagName() + "'");
//        }
        relInfoSql.append("       group by company_id ");

        //System.out.println(relInfoSql.toString());

        String sql = String.format("select company.* from company_tag_rel rel\n" +
                        "        left join company on rel.companyId = company.id  where tagId = %s\n" +
                        "        and (rel.active is null or rel.active = 'Y') and  (company.active is null or company.active = 'Y') and not ISNULL( company.originalLogo ) and not ISNULL( company.brief )\n" +
                        "  and not ISNULL( company.active ) ORDER BY company.verify desc, website desc limit 50"
                , 123);
        //System.out.println(sql);

        List<String> list =  Lists.newArrayList("SELECT " +
                "a.name as tagName, a.id as tagId, a.type as tagType, b.confidence,\n" +
                "c.id as companyId, c.name as companyName, c.originalLogo as companyOriginalLogo, c.website as companyWebsite,\n" +
                "c.description as companyDescription,c.companyStatus,c.statusDate as companyStatusdate,\n" +
                "c.brief as companyBrief, c.prime as companyPrime, c.verify,\n" +
                "d.id as corporateId, d.fullName as corporateFullname, d.establishDate as corporateEstablishdate,\n" +
                "d.round as corporateRound,\n" +
                "CASE\n" +
                "when d.round = 0 then '股权投资'\n" +
                "when d.round = 1009 then '众筹'\n" +
                "when d.round = 1010 then '种子轮'\n" +
                "when d.round = 1011 then '天使轮'\n" +
                "when d.round = 1020 then 'pre-A'\n" +
                "when d.round = 1030 then 'A'\n" +
                "when d.round = 1031 then 'A+'\n" +
                "when d.round = 1039 then 'Pre-B'\n" +
                "when d.round = 1040 then 'B'\n" +
                "when d.round = 1041 then 'B+'\n" +
                "when d.round = 1050 then 'C'\n" +
                "when d.round = 1051 then 'C+'\n" +
                "when d.round = 1060 then 'D'\n" +
                "when d.round = 1070 then 'E'\n" +
                "when d.round = 1080 then 'F'\n" +
                "when d.round = 1090 then 'late Stage'\n" +
                "when d.round = 1100 then 'pre-IPO'\n" +
                "when d.round = 1105 then '新三板'\n" +
                "when d.round = 1106 then '新三板定增'\n" +
                "when d.round = 1109 then 'ICO'\n" +
                "when d.round = 1110 then 'IPO'\n" +
                "when d.round = 1111 then 'Post-IPO'\n" +
                "when d.round = 1120 then '被收购'\n" +
                "when d.round = 1130 then '战略投资'\n" +
                "when d.round = 1131 then '战略合并'\n" +
                "when d.round = 1140 then '私有化'\n" +
                "when d.round = 1150 then '债权融资'\n" +
                "when d.round = 1160 then '股权转让'\n" +
                "when d.round = 1170 then '扶持基金'\n" +
                "when d.round = 1180 then '新三板退市'\n" +
                "else '无融资记录' end as corparateRoundName,\n" +
                " d.regionId as corporateRegionid, e.`name`as corporateRegionName, \n" +
                "d.provinceId as corporateProvinceid, f.`name` as corporateProvinceName,\n" +
                "d.cityId as corporateCityid, g.`name` as corporateCityName,\n" +
                "d.districtId as corporateDistrictid, h.`name` as corporateDistrictName,\n" +
                "CASE\n" +
                "when i.stockId is null then 0 \n" +
                "else 1  end as stockStatus, j.listDate as corporateListDate\n" +
                "  FROM tag a join company_tag_rel b on a.id = b.tagId\n" +
                "  join company c on b.companyId = c.id \n" +
                "  join corporate d on c.corporateId = d.id\n" +
                "  left join region e on d.regionId = e.id\n" +
                "  left join province f on d.provinceId = f.id\n" +
                "  left join city g on d.cityId = g.id\n" +
                "  left join district h on d.districtId = h.id\n" +
                "  left join corporate_stock_rel i on d.id = i.corporateId\n" +
                "  left join stock j on i.stockId = j.id\n" +
                " WHERE (a.active is null or a.active='Y') and a.type in (11010, 11011, 11012, 11013, 11053, 11100, 11500, 11700) \n" +
                "   and (b.active is null or b.active='Y') \n" +
                "   and (c.active is null or c.active='Y') \n" +
                "   and (d.active is null or d.active='Y') \n" +
                "   and (e.active is null or e.active='Y') \n" +
                "   and (f.active is null or f.active='Y') \n" +
                "   and (g.active is null or g.active='Y') \n" +
                "   and (h.active is null or h.active='Y') \n" +
                "   and (i.active is null or i.active='Y') \n" +
                "   and (j.active is null or j.active='Y') \n"
        );

        System.out.println(list.get(0));

        System.out.println("over");

    }



    /**
     * 比较两个字符串的相识度
     * 核心算法：用一个二维数组记录每个字符串是否相同，如果相同记为0，不相同记为1，每行每列相同个数累加
     * 则数组最后一个数为不相同的总数，从而判断这两个字符的相识度
     *
     * @param str
     * @param target
     * @return
     */
    private static int compare(String str, String target) {
        int d[][];              // 矩阵
        int n = str.length();
        int m = target.length();
        int i;                  // 遍历str的
        int j;                  // 遍历target的
        char ch1;               // str的
        char ch2;               // target的
        int temp;               // 记录相同字符,在某个矩阵位置值的增量,不是0就是1
        if (n == 0) {
            return m;
        }
        if (m == 0) {
            return n;
        }
        d = new int[n + 1][m + 1];
        // 初始化第一列
        for (i = 0; i <= n; i++) {
            d[i][0] = i;
        }
        // 初始化第一行
        for (j = 0; j <= m; j++) {
            d[0][j] = j;
        }
        for (i = 1; i <= n; i++) {
            // 遍历str
            ch1 = str.charAt(i - 1);
            // 去匹配target
            for (j = 1; j <= m; j++) {
                ch2 = target.charAt(j - 1);
                if (ch1 == ch2 || ch1 == ch2 + 32 || ch1 + 32 == ch2) {
                    temp = 0;
                } else {
                    temp = 1;
                }
                // 左边+1,上边+1, 左上角+temp取最小
                d[i][j] = min(d[i - 1][j] + 1, d[i][j - 1] + 1, d[i - 1][j - 1] + temp);
            }
        }
        return d[n][m];
    }


    /**
     * 获取最小的值
     */
    private static int min(int one, int two, int three) {
        return (one = one < two ? one : two) < three ? one : three;
    }

    /**
     * 获取两字符串的相似度
     */
    public static float getSimilarityRatio(String str, String target) {
        int max = Math.max(str.length(), target.length());
        return 1 - (float) compare(str, target) / max;
    }


    /*****************/


    /**
     * 　　DNA分析 　　拼字检查 　　语音辨识 　　抄袭侦测
     *
     */
    public static void levenshtein(String str1,String str2) {
        //计算两个字符串的长度。
        int len1 = str1.length();
        int len2 = str2.length();
        //建立上面说的数组，比字符长度大一个空间
        int[][] dif = new int[len1 + 1][len2 + 1];
        //赋初值，步骤B。
        for (int a = 0; a <= len1; a++) {
            dif[a][0] = a;
        }
        for (int a = 0; a <= len2; a++) {
            dif[0][a] = a;
        }
        //计算两个字符是否一样，计算左上的值
        int temp;
        for (int i = 1; i <= len1; i++) {
            for (int j = 1; j <= len2; j++) {
                if (str1.charAt(i - 1) == str2.charAt(j - 1)) {
                    temp = 0;
                } else {
                    temp = 1;
                }
                //取三个值中最小的
                dif[i][j] = min(dif[i - 1][j - 1] + temp, dif[i][j - 1] + 1,
                        dif[i - 1][j] + 1);
            }
        }
        System.out.println("字符串\""+str1+"\"与\""+str2+"\"的比较");
        //取数组右下角的值，同样不同位置代表不同字符串的比较
        System.out.println("差异步骤："+dif[len1][len2]);
        //计算相似度
        float similarity =1 - (float) dif[len1][len2] / Math.max(str1.length(), str2.length());
        System.out.println("相似度："+getPercentValue(similarity));
    }
    public static String getPercentValue( float similarity){
        NumberFormat fmt = NumberFormat.getPercentInstance();
        fmt.setMaximumFractionDigits(2);//最多两位百分小数，如25.23%
        return fmt.format(similarity);
    }
    //得到最小值
    private static int min(int... is) {
        int min = Integer.MAX_VALUE;
        for (int i : is) {
            if (min > i) {
                min = i;
            }
        }
        return min;
    }
}
