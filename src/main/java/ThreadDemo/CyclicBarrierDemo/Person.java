package ThreadDemo.CyclicBarrierDemo;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Person implements Runnable {

    private CyclicBarrier cyclicBarrier;
    private int num;

    public Person(CyclicBarrier cyclicBarrier,int num){
        this.cyclicBarrier = cyclicBarrier;
        this.num = num;
    }

    @Override
    public void run() {
        try {

            //做一些事
            System.out.println(num + "做一些事");
            Thread.sleep(1000*2);

            //事情做完，相当于到了栅栏处，等待其它线程做完。
            cyclicBarrier.await();

            Thread.sleep(1000*2);
            System.out.println(num + "做完了");

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
    }
}
