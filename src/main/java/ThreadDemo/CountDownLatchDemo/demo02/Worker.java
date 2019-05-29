package ThreadDemo.CountDownLatchDemo.demo02;

import java.util.concurrent.CountDownLatch;

public class Worker implements Runnable {

    private CountDownLatch countDownLatch;
    /*
     * 员工编号
     */
    private String workCode;

    public Worker(String workCode,CountDownLatch countDownLatch){
        this.countDownLatch = countDownLatch;
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

        this.countDownLatch.countDown();
    }
}
