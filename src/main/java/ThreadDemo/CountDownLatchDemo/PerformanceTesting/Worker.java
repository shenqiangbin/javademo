package ThreadDemo.CountDownLatchDemo.PerformanceTesting;

import MyDate.DateUtil;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import dbmgr.mySqlAccess.MySqlHelper;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class Worker implements Runnable {
    private final int index;
    private final CountDownLatch startSignal;

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
        System.out.println(index + "do something - " + " " + DateUtil.format(new Date()) + " " + System.currentTimeMillis());

        String task = getWaitTask();
        if (task == null) {
            System.out.println(index + "没有任务了 - " + " " + DateUtil.format(new Date()) + " " + System.currentTimeMillis());
        }

        System.out.println(index + "处理任务 - " + task + " " + DateUtil.format(new Date()) + " " + System.currentTimeMillis());

    }

    HikariDataSource mysqlDataSource = new HikariDataSource(getConfig());
    MySqlHelper mySqlHelper = new MySqlHelper(mysqlDataSource);

    private String getWaitTask() throws Exception {
        // state: 0-待处理
        List<LinkedHashMap<String, Object>> linkedHashMaps = mySqlHelper.simpleQuery("select * from task where state = 0 limit 1", null);
        if (linkedHashMaps != null && linkedHashMaps.size() > 0) {
            String id = linkedHashMaps.get(0).get("id").toString();
            String name = linkedHashMaps.get(0).get("name").toString();
            return name;
        }
        return null;
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
        config.setUsername("root");
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setPassword("123456");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmptCacheSqlLimit", "2048");

        return config;
    }
}
