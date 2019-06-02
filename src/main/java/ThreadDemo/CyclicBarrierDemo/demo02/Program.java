package ThreadDemo.CyclicBarrierDemo.demo02;


import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.CyclicBarrier;

public class Program {
    public static void main(String[] args) {


        //重点实现的是 员工干完第一批活之后，老板检查了，员工才开始干第二批活。

        int threadCount = 3;

        Boss boss = new Boss();

        CyclicBarrier cyclicBarrier = new CyclicBarrier(threadCount,boss);

        Worker worker1 =new Worker("worker1",cyclicBarrier);
        Worker worker2 =new Worker("worker2",cyclicBarrier);
        Worker worker3 =new Worker("worker3",cyclicBarrier);

        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(worker1);
        executorService.execute(worker2);
        executorService.execute(worker3);

        executorService.shutdown();

        System.out.println("over");


    }
}
