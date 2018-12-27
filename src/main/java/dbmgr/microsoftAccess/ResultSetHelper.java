package dbmgr.microsoftAccess;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ResultSetHelper {

    public static <T> List<T> toList(ResultSet resultSet, Class<T> type) throws SQLException, InstantiationException,
            IllegalAccessException, NoSuchFieldException, SecurityException {
        List<T> list = new ArrayList<T>();

        if (resultSet != null) {

            ResultSetMetaData md = resultSet.getMetaData();// 获取键名
            int columnCount = md.getColumnCount();// 获取行的数量

            while (resultSet.next()) {

                // 此类要有默认的构造函数
                T instance = type.newInstance();
                Field[] fields = type.getDeclaredFields();

                for (int i = 1; i <= columnCount; i++) {
                    String colName = md.getColumnName(i);
                    Object val = resultSet.getObject(i);

                    for (Field field : fields) {
                        if (field.getName().equalsIgnoreCase(colName)) {
                            field.setAccessible(true);
                            field.set(instance, val);
                        }
                    }
                }

                list.add(instance);

            }
        }

        return list;
    }
}
