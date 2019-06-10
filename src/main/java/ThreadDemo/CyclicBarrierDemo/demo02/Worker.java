package ThreadDemo.CyclicBarrierDemo.demo02;

import java.util.concurrent.BrokenBarrierException;
<<<<<<< HEAD
=======
import java.util.concurrent.CountDownLatch;
>>>>>>> 3d58f33d269783f34eee612568737ef44d2bdf11
import java.util.concurrent.CyclicBarrier;

public class Worker implements Runnable {

    private CyclicBarrier cyclicBarrier;
    /*
     * 员工编号
     */
    private String workCode;

<<<<<<< HEAD
    public Worker(String workCode,CyclicBarrier cyclicBarrier){
=======
    Worker(String workCode, CyclicBarrier cyclicBarrier){
>>>>>>> 3d58f33d269783f34eee612568737ef44d2bdf11
        this.cyclicBarrier = cyclicBarrier;
        this.workCode = workCode;
    }

    @Override
    public void run() {

<<<<<<< HEAD
        String msg = new StringBuilder()
                .append("我是员工")
                .append(workCode)
                .append(",我正在干活")
                .toString();
=======
        String msg = "我是员工" +
                workCode +
                ",我正在干活";
>>>>>>> 3d58f33d269783f34eee612568737ef44d2bdf11

        System.out.println(msg);

        try {
            Thread.sleep(2000);
<<<<<<< HEAD
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
=======

            System.out.println(new StringBuilder(this.workCode).append("活干完了"));

            //开始等待其他员工干完
            this.cyclicBarrier.await();

            System.out.println(new StringBuilder(this.workCode).append("开始干第二批"));

        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
>>>>>>> 3d58f33d269783f34eee612568737ef44d2bdf11
    }
}
