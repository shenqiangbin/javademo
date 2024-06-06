package ThreadDemo;

import cn.hutool.core.date.DateTime;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class demo02Change {


    /**
     * demo02 的改进版本
     * @param args
     */
    public static void main(String[] args) {
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(4);

        // 专门执行任务的
        // 应该会同时执行3个任务，第1个任务用时比较长，但第2，3个任务完成后，就会执行4，5
        ExecutorService doservice = Executors.newFixedThreadPool(3);

        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                System.out.println("schedule :" + taskId + "---start:" + Thread.currentThread().toString() + " " + DateTime.now());
                doservice.execute(new Runnable() {
                    @Override
                    public void run() {
                        int taskId = getTask();
                        // 假设就 10 个任务
                        if(taskId>10){
                            return;
                        }
                        System.out.println("taskId:" + taskId + "---start:" + Thread.currentThread().toString() + " " + DateTime.now());
                        try {
                            if(taskId == 1){
                                // 第一个任务运行 20 s
                                Thread.sleep(1000 * 1 * 20);
                            }else {
                                // 每个运行 5 + taskId 秒
                                Thread.sleep(1000 * 1 * (5+taskId));
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println("taskId:" + taskId + "---end:" + Thread.currentThread() + " " + DateTime.now());
                    }
                });

            }
        }, 1, 2, TimeUnit.SECONDS);

    }

    static int taskId = 1;
    static int getTask(){
        System.out.println("获取任务");
        return taskId++;
    }


}

