package ThreadDemo.CountDownLatchDemo;

import java.util.concurrent.CountDownLatch;

public class Worker implements Runnable {
    private final int index;
    private final CountDownLatch startSignal;

    public Worker(int index, CountDownLatch startSignal){
        this.index = index;
        this.startSignal = startSignal;
    }

    @Override
    public void run() {
        //doWork();
        try {
            startSignal.await();
            doWork();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    void doWork(){
        //dosomething
        System.out.println("do something - " + index);
    }
}
