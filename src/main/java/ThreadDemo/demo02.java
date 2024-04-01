package ThreadDemo;

import cn.hutool.core.date.DateTime;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class demo02 {


    /**
     * 多线程实现定时任务的操作功能
     *
     * scheduleAtFixedRate 的任务执行会顺序执行，并不会并发执行。
     * 第一个任务没有处理完，则会等待
     *
     * 改进版本见 demo02Change
     *
     * @param args
     */
    public static void main(String[] args) {
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(4);
        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                int taskId = getTask();
                // 假设就 10 个任务
                if(taskId>10){
                    return;
                }
                System.out.println("taskId:" + taskId + "---start:" + Thread.currentThread() + " " + DateTime.now());
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
        }, 1, 2, TimeUnit.SECONDS);

    }

    static int taskId = 1;
    static int getTask(){
        return taskId++;
    }

}

