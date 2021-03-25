package MyDate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateUtil {

    private enum Type {
        MILLISECONDS,
        SECOND,
        MINUTE,
        HOUR,
        DAY
    }

    public static void main(String[] args) throws ParseException {
        Date d = new Date();
        String dStr = DateUtil.format(d);
        System.out.println(dStr);

        System.out.println(DateUtil.formatOnlyDay(d));
        System.out.println(DateUtil.addOneDayAndformatOnlyDay(d));

        long diff = DateUtil.diff(DateUtil.parse("2021-1-3 12:11:10"), DateUtil.parse("2021-1-1 12:11:10"), Type.HOUR);
        System.out.println(diff);

        List<String> list = Arrays.asList("1,2,3,4","123");
        System.out.println(list);
    }

    public static String format(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    /**
     * 格式化时间，只格式到天（常用在 SQL 时间查询的 between and 中）
     *
     * @param date
     * @return
     */
    public static String formatOnlyDay(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        return sdf.format(date);
    }

    /**
     * 新增一天并格式化时间，只格式到天（常用在 SQL 时间查询的 between and 中）
     *
     * @param date
     * @return
     */
    public static String addOneDayAndformatOnlyDay(Date date) {
        return DateUtil.formatOnlyDay(DateUtil.addDay(date, 1));
    }

    public static Date parse(String date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.parse(date);
    }

    public static Date addDay(Date date, int amount) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, amount);
        return cal.getTime();
    }

    /**
     * 两个时间差
     *
     * @param from 开始时间
     * @param to   结束时间
     * @param type 以 type 单位来表示时间差
     * @return
     */
    public static long diff(Date from, Date to, DateUtil.Type type) {
        long between = Math.abs((to.getTime() - from.getTime()));

        switch (type) {
            case MILLISECONDS:
                return between;
            case SECOND:
                return between / 1000;
            case MINUTE:
                return between / 1000 / 60;
            case HOUR:
                return between / 1000 / 60 / 60;
            case DAY:
                return between / 1000 / 60 / 60 / 24;
            default:
                throw new RuntimeException("未知类型");
        }
    }
}
