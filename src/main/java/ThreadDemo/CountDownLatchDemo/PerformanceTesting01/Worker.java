package ThreadDemo.CountDownLatchDemo.PerformanceTesting01;

import MyDate.DateUtil;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import dbmgr.mySqlAccess.MySqlHelper;
import org.apache.commons.lang3.StringUtils;

import java.sql.SQLException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class Worker implements Runnable {
    private final int index;
    private final CountDownLatch startSignal;

    // state: 2-处理完成  1-处理中 0-待领取

    public Worker(int index, CountDownLatch startSignal) {
        this.index = index;
        this.startSignal = startSignal;
    }

    @Override
    public void run() {
//        System.out.println("worker - " + index + " 准备好了");
//        doWork();
        try {
            System.out.println("worker - " + index + " 准备好了");
            startSignal.await();
            doWork();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void doWork() throws Exception {
        //dosomething
        System.out.println(index + " - do something - " + " " + DateUtil.format(new Date()) + " " + System.currentTimeMillis());

        String taskId = getWaitTask();
        if (taskId == null) {
            System.out.println(index + " - 没有任务了 - " + " " + DateUtil.format(new Date()) + " " + System.currentTimeMillis());
        }else {
            int effectrow = handleTask(taskId);
            System.out.println(index + " - 处理任务 - " + taskId + " " + DateUtil.format(new Date()) + " " + System.currentTimeMillis() + " effectrow:" + effectrow);
        }
    }


    HikariDataSource mysqlDataSource = new HikariDataSource(getConfig());
    MySqlHelper mySqlHelper = new MySqlHelper(mysqlDataSource);

    private String getWaitTask() throws Exception {
        // state: 0-待处理
        List<LinkedHashMap<String, Object>> linkedHashMaps = mySqlHelper.simpleQuery("select * from task where state = 0 limit 1", null);
        if (linkedHashMaps != null && linkedHashMaps.size() > 0) {
            String id = linkedHashMaps.get(0).get("id").toString();
            String name = linkedHashMaps.get(0).get("name").toString();
            String version = linkedHashMaps.get(0).get("version").toString();

            // 状态改变成功，才能说明拿到了这个任务
            if (changeStateWithVersion(id, version)) {
                return id;
            } else {
                // 如果没成功，再获取一次任务
                String secondId = getWaitTask();
                if (!StringUtils.isBlank(secondId)) {
                    return secondId;
                }
            }

        }
        return null;
    }

    /**
     * 改变任务状态（附加版本号条件）
     * 注意：这里必须同时改变获取任务时的查询条件，保证不会这个语句执行完毕之后，还会检索到任务。
     * @param id
     * @param currentVersion
     * @return
     * @throws SQLException
     */
    private boolean changeStateWithVersion(String id, String currentVersion) throws SQLException {
        System.out.println("version:" + currentVersion);
        int effectrow = mySqlHelper.update("update task set version = version + 1, state = 1 where id = ? and version = ?", new Object[]{id, currentVersion});
        return effectrow > 0;
    }

    private int handleTask(String task) throws SQLException {
        // state: 2-处理完成  1-处理中 0-待领取
        try {
            System.out.println("处理中");
            Thread.sleep(1000*5);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        int effectrow = mySqlHelper.update("update task set state = 2 where id = ?", new Object[]{task});
        return effectrow;

    }

    public static HikariConfig getConfig() {

        /**
         * CREATE TABLE `task`  (
         *   `id` int(0) NOT NULL AUTO_INCREMENT,
         *   `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
         *   `state` int(0) NULL DEFAULT NULL COMMENT '0-待处理',
         *    PRIMARY KEY (`id`) USING BTREE
         * ) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;
         *
         * INSERT INTO `task`(`id`, `name`, `state`) VALUES (1, '任务1', 0);
         * INSERT INTO `task`(`id`, `name`, `state`) VALUES (2, '任务2', 0);
         */
        HikariConfig config = new HikariConfig();

        //config.setJdbcUrl("jdbc:mysql://:3306/bd?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=UTC");
        config.setJdbcUrl("jdbc:mysql://10.31.68.13:3306/tablebigdata54?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=UTC");
        //config.setJdbcUrl("jdbc:mysql://localhost:3306/world?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=UTC");
        config.setUsername("root");
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setPassword("123456");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmptCacheSqlLimit", "2048");

        return config;
    }
}
