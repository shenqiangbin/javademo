package myenum;

public class EnumTest {

    public static void main(String[] args) {
        String esCommonFieldName = IndicatorLabel.keyword.toString();
        String commonFieldName = IndicatorLabel.keyword.getName();

        System.out.println(esCommonFieldName);
        System.out.println(commonFieldName);

    }
}
