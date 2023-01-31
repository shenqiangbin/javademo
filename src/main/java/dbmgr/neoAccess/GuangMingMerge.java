package dbmgr.neoAccess;

import com.zaxxer.hikari.HikariConfig;
import dbmgr.mySqlAccess.MySqlHelper;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GuangMingMerge {


    public static void main(String[] args) throws Exception {

        //76
        String str = "\"基因编辑\",\"图像识别\",\"食品安全\",\"包装设计\",\"抗病性\",\"饲料添加剂\",\"饲料\",\"牛肉\",\"猪肉\",\"育种\",\"多式联运\",\"健康\",\"感官评定\",\"饲料原料\",\"冷鲜肉\",\"养殖\",\"冷冻\",\"减盐\",\"动物营养\",\"调味品\",\"高蛋白\",\"渠道\",\"猪饲料\",\"网络工程\",\"料肉比\",\"低碳\",\"屠宰\",\"边缘计算\",\"低脂\",\"水产饲料\",\"PSY\",\"瘦肉率\",\"细胞培养肉\",\"饲料配方\",\"冷鲜\",\"节能减排\",\"预混料\",\"水产\",\"种猪\",\"动物蛋白\",\"植物蛋白\",\"港口\",\"尾水处理\",\"预制菜\",\"禽肉\",\"供应链金融\",\"冻精\",\"农产品质量安全\",\"替代蛋白\",\"零售渠道\",\"昆虫蛋白\",\"循环冷却水\",\"植物肉\",\"重金属废水处理\",\"粗分割\",\"仔猪饲料\",\"性状指标\",\"反刍动物饲料\",\"决策辅助\",\"养猪废弃物\",\"标准化生产\",\"精细化管理\",\"浓缩料\",\"消费者行为\",\"社群运营\",\"水铁联运\",\"跨境零售\",\"智能排产\",\"新媒体渠道\",\"包装工艺\",\"速食\",\"热鲜肉\",\"饲草资源\",\"禽饲料\",\"低温高湿\",\"期货交割库\"";

        //73
        String str2 = "\"基因编辑\",\"图像识别\",\"供应链金融\",\"食品安全\",\"育种\",\"抗病性\",\"饲料\",\"饲料添加剂\",\"包装设计\",\"消费者行为\",\"牛肉\",\"健康\",\"猪肉\",\"养殖\",\"高蛋白\",\"精细化管理\",\"边缘计算\",\"饲料原料\",\"水产饲料\",\"感官评定\",\"农产品质量安全\",\"冷冻\",\"禽肉\",\"渠道\",\"动物营养\",\"冷鲜肉\",\"料肉比\",\"低碳\",\"PSY\",\"猪饲料\",\"屠宰\",\"昆虫蛋白\",\"调味品\",\"种猪\",\"浓缩料\",\"低脂\",\"水产\",\"节能减排\",\"饲料配方\",\"标准化生产\",\"多式联运\",\"预混料\",\"尾水处理\",\"冻精\",\"减盐\",\"预制菜\",\"植物蛋白\",\"瘦肉率\",\"分销\",\"替代蛋白\",\"养猪废弃物\",\"植物肉\",\"性状指标\",\"港口\",\"包装工艺\",\"饲草资源\",\"细胞培养肉\",\"冷鲜\",\"低温高湿\",\"禽饲料\",\"反刍动物饲料\",\"消费者预期\",\"动物蛋白\",\"热鲜肉\",\"双碳\",\"零售渠道\",\"循环冷却水\",\"码头\",\"决策辅助\",\"社群运营\",\"跨境零售\",\"速食\",\"重金属废水处理\"";

        String strSort = Arrays.stream(str.split(",")).sorted().collect(Collectors.toList()).toString();
        String str2Sort = Arrays.stream(str2.split(",")).sorted().collect(Collectors.toList()).toString();

        System.out.println(strSort);
        System.out.println(str2Sort);

    }


}