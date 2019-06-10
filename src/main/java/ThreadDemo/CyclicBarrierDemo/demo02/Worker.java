package ThreadDemo.CyclicBarrierDemo.demo02;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Worker implements Runnable {

    private CyclicBarrier cyclicBarrier;
    /*
     * 员工编号
     */
    private String workCode;

    public Worker(String workCode,CyclicBarrier cyclicBarrier){
        this.cyclicBarrier = cyclicBarrier;
        this.workCode = workCode;
    }

    @Override
    public void run() {

        String msg = new StringBuilder()
                .append("我是员工")
                .append(workCode)
                .append(",我正在干活")
                .toString();

        System.out.println(msg);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("活干完了，等别人干完");

        try {
            this.cyclicBarrier.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }

        System.out.println("继续干");
    }
}
