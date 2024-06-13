package ThreadDemo.CountDownLatchDemo.PerformanceTesting01;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * 10 个线程并发估某件事的功能 测试。（抢某个资源的测试）
 */
public class Driver {

    void main() {

        CountDownLatch startSignal = new CountDownLatch(1);

        int number = 10;
        for (int i = 0; i < number; i++) {
            new Thread(new Worker(i, startSignal)).start();
        }

        try {
            // 这里是等待 worker 线程创建完毕
            System.out.println("driver 准备中");
            TimeUnit.SECONDS.sleep(5);
            System.out.println("driver 准备完毕");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //driver准备完了，可以countDown了，等待的可以继续走了
        startSignal.countDown();

        System.out.println("driver do something else done");
    }

    public static void main(String[] args) {
        new Driver().main();
    }
}
