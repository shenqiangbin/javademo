package ThreadDemo.CountDownLatchDemo.demo02;


import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Program {
    public static void main(String[] args) {

        CountDownLatch countDownLatch = new CountDownLatch(3);

        Worker worker1 =new Worker("worker1",countDownLatch);
        Worker worker2 =new Worker("worker2",countDownLatch);
        Worker worker3 =new Worker("worker3",countDownLatch);

        Boss boss = new Boss(countDownLatch);

        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(worker1);
        executorService.execute(worker2);
        executorService.execute(worker3);
        executorService.execute(boss);
        executorService.shutdown();

        System.out.println("over");


    }
}
