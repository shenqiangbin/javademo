package ThreadDemo.CyclicBarrierDemo.demo02;

import java.util.concurrent.CountDownLatch;

public class Boss implements Runnable{

    @Override
    public void run() {
        System.out.println("我是老板，我检查了");
    }
}
