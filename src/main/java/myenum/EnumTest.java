package myenum;

import com.google.common.base.Enums;

public class EnumTest {

    public static void main(String[] args) {
        String esCommonFieldName = IndicatorLabel.keyword.toString();
        String commonFieldName = IndicatorLabel.keyword.getName();

        System.out.println(esCommonFieldName);
        System.out.println(commonFieldName);

        IndicatorLabel indicatorLabel = IEnum.getEnumByVal(IndicatorLabel.class,11);

        System.out.println("ok");
    }
}
