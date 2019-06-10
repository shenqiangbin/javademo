package ThreadDemo.CyclicBarrierDemo.demo02;


<<<<<<< HEAD
import ThreadDemo.CountDownLatchDemo.demo02.Boss;
import ThreadDemo.CountDownLatchDemo.demo02.Worker;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
=======
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.CyclicBarrier;
>>>>>>> 3d58f33d269783f34eee612568737ef44d2bdf11

public class Program {
    public static void main(String[] args) {

<<<<<<< HEAD
        CountDownLatch countDownLatch = new CountDownLatch(3);

        Worker worker1 =new Worker("worker1",countDownLatch);
        Worker worker2 =new Worker("worker2",countDownLatch);
        Worker worker3 =new Worker("worker3",countDownLatch);

        Boss boss = new Boss(countDownLatch);
=======

        //重点实现的是 员工干完第一批活之后，老板检查了，员工才开始干第二批活。

        int threadCount = 3;

        Boss boss = new Boss();

        CyclicBarrier cyclicBarrier = new CyclicBarrier(threadCount,boss);

        Worker worker1 =new Worker("worker1",cyclicBarrier);
        Worker worker2 =new Worker("worker2",cyclicBarrier);
        Worker worker3 =new Worker("worker3",cyclicBarrier);
>>>>>>> 3d58f33d269783f34eee612568737ef44d2bdf11

        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(worker1);
        executorService.execute(worker2);
        executorService.execute(worker3);
<<<<<<< HEAD
        executorService.execute(boss);
=======

>>>>>>> 3d58f33d269783f34eee612568737ef44d2bdf11
        executorService.shutdown();

        System.out.println("over");


    }
}
