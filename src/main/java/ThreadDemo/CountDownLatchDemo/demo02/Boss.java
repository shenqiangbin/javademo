package ThreadDemo.CountDownLatchDemo.demo02;

import java.util.concurrent.CountDownLatch;

public class Boss implements Runnable{

    private CountDownLatch countDownLatch;

    public Boss(CountDownLatch countDownLatch){
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {
        try {
            this.countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("我是老板，我检查了");
    }
}
