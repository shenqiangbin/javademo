package myenum;

public enum IndicatorLabel {
    primaryKey("主键", 0),
    time("时间", 1),
    location("地点", 2),
    lng("经度", 3),
    lat("纬度", 4),
    title("标题", 5),
    keyword("关键词", 6),
    shortAbstract("摘要", 7),
    content("全文", 8),
    areaCode("地域编码", 11),
    sortField1("排序扩展1", 9),
    groupField1("分组扩展1", 10),
    sortField2("排序扩展2", 12),
    groupField2("分组扩展2", 13),
    sortField3("排序扩展3", 14),
    groupField3("分组扩展3", 15),
    Author("作者", 16),
    file("附件", 17),
    indicator("指标名称", 18),
    categoryfield1("分类扩展1", 19),
    categoryfield2("分类扩展2", 20),
    categoryfield3("分类扩展3", 21),
    recommendfield("推荐字段", 22),
    indicatorvalue("指标值", 23),

    groupField4("分组扩展4", 50),
    groupField5("分组扩展5", 51),
    groupField6("分组扩展6", 52),
    groupField7("分组扩展7", 53),
    groupField8("分组扩展8", 54),
    groupField9("分组扩展9", 55),
    groupField10("分组扩展10", 56),

    KeyFiled("核心字段", 80);

    private final  String name;

    private  Integer value;

    public String getName() {
        return name;
    }

    public final Integer getValue() {
        return value;
    }

    private IndicatorLabel(String name, Integer value) {
        this.name = name;
        this.value = value;
    }
}