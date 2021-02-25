package dbmgr.mySqlAccess;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import dbmgr.mySqlAccess.model.ClassStandard;
import fileDemo.FileHelper;
import org.apache.commons.lang3.StringUtils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class MySqlHelperExportData {
    static MySqlHelper mySqlHelper = new MySqlHelper(getConfig());

    public static void main(String[] args) throws Exception {
        HikariDataSource dataSource = new HikariDataSource(getConfig());
        Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement();

        String sql = "select id,normalName from nv_normal_value where organizationid = 61";
        ResultSet resultSet = statement.executeQuery(sql);

        StringBuilder builder = new StringBuilder();
        if (resultSet != null) {
            while (resultSet.next()) {
                String id = resultSet.getString("id");
                String normalName = resultSet.getString("normalName");
                String category = getIndicatorCategory(id);
                builder.append(normalName).append(",").append(category).append("\r\n");
                System.out.println("......");
            }
        }

        FileHelper.deleteTxtFile("e:/指标.csv");
        FileHelper.writeTxtFile(builder.toString(), "e:/指标.csv", false, true);

        System.out.println("完");
    }


    /**
     * 获取到 classCode 比如 4546/4548/  然后再转换成对应的汉字
     *
     * @param indicatorId
     * @return
     */
    private static String getIndicatorCategory(String indicatorId) throws Exception {
        String sql = "select classCode from nv_class_value_relation where status = 1 and valueid = ?";
        String classCode = mySqlHelper.executeScalar(sql, Arrays.asList(new Object[]{indicatorId}));

        if (StringUtils.isBlank(classCode)) {
            return "";
        }

        return convertClassCode(classCode);
    }

    /**
     * 将 4546/4548/ 转换成 地区/亚洲
     *
     * @param classCode
     * @return
     * @throws Exception
     */
    private static String convertClassCode(String classCode) throws Exception {
        String sql = "select id,className from nv_class_standard where status = 1 and id in (" + classCode.replace("/", ",").substring(0, classCode.length() - 1) + ")";
        List<ClassStandard> list = mySqlHelper.simpleQuery(sql, ClassStandard.class);

        List<String> newList = new ArrayList<>();
        String[] arr = classCode.split("/");
        for (String id : arr) {
            Optional<ClassStandard> classStandard = list.stream().filter(m -> m.getId().toString().equals(id)).findFirst();
            if (classStandard.isPresent() && classStandard.get() != null) {
                newList.add(classStandard.get().getClassName());
            } else {
                newList.add("");
            }
        }

        return String.join("/", newList);
    }

    public static HikariConfig getConfig() {

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/tmp?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=UTC");
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setUsername("root");
        config.setPassword("123456");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmptCacheSqlLimit", "2048");

        return config;
    }


}
