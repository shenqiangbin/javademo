package ThreadDemo.MyThreadPool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NewCachedThreadPoolDemo {
    public static void main(String[] args) throws Exception {

        System.out.println(Runtime.getRuntime().availableProcessors());

        int num = 0;

        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 0; i < 10; i++) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000 * 1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    getThredInfo();

                    System.out.println("1:" + Thread.currentThread() + " " + Thread.activeCount());

                }
            });

            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000 * 1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("2:" + Thread.currentThread() + " " + Thread.activeCount());

                }
            });
        }

        executorService.shutdown();
    }

    public static void getThredInfo() {
        ThreadGroup threadGroup = Thread.currentThread().getThreadGroup();


        Thread[] threadArr = new Thread[20];
        if (threadGroup != null) {
            int num = threadGroup.enumerate(threadArr);
            System.out.println(Thread.currentThread() + ":当前线程数：" + num);
        }

        for (Thread thread : threadArr) {
            if(thread==null)
                continue;

            System.out.println(
                    new StringBuilder()
                    .append(thread.getName()).append("\n")
                    .append(thread.getState()).append("\n")
            );
        }
    }
}
