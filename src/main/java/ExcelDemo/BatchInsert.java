package ExcelDemo;

import java.util.ArrayList;
import java.util.List;

/**
 * 批量插入的示例类
 *
 * using：
 *
 * BatchInsert batchInsert = new BatchInsert("insert into user(name,age) values(?,?);
 *
 * 遍历表格或其他地方的数值来源
 * for(){
 *     List<Object> params = new ArrayList();
 *     params.add("张三"); // 赋值
 *     params.add(20);  // 赋值
 *     batchInsert.save(); // 保存后，默认每 1000 条会真正提交保存到数据库
 * }
 *     batchInsert.flush(); // 必须调用
 *
 **/
public class BatchInsert {

    private String insertSql;
    List<List<Object>> paramsList = new ArrayList<>();

    public BatchInsert() {
    }

    public BatchInsert(String insertSql) {
        this.insertSql = insertSql;
    }

    public void setInsertSql(String insertSql) {
        this.insertSql = insertSql;
    }

    public void save(List<Object> params) {
        // 每够 1000 条记录提交一次
        if (paramsList.size() > 0 && paramsList.size() % 1000 == 0) {
            saveRecord(paramsList);
            // 提交后，清空列表
            paramsList.clear();
        }
    }

    private void saveRecord(List<List<Object>> params) {
        //mySqlHelper.executeSqlBatch(insertSql, params);
    }

    public void flush() {
        if (paramsList.size() > 0) {
            saveRecord(paramsList);
            paramsList.clear();
        }
    }
}
