package ThreadDemo.CyclicBarrierDemo.demo02;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

public class Worker implements Runnable {

    private CyclicBarrier cyclicBarrier;
    /*
     * 员工编号
     */
    private String workCode;

    Worker(String workCode, CyclicBarrier cyclicBarrier){
        this.cyclicBarrier = cyclicBarrier;
        this.workCode = workCode;
    }

    @Override
    public void run() {

        String msg = "我是员工" +
                workCode +
                ",我正在干活";

        System.out.println(msg);

        try {
            Thread.sleep(2000);

            System.out.println(new StringBuilder(this.workCode).append("活干完了"));

            //开始等待其他员工干完
            this.cyclicBarrier.await();

            System.out.println(new StringBuilder(this.workCode).append("开始干第二批"));

        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
    }
}
