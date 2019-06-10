package ThreadDemo.CyclicBarrierDemo;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PersonOperator {
    public static void main(String[] args) {

        CyclicBarrier cyclicBarrier = new CyclicBarrier(3, new Runnable() {
            @Override
            public void run() {
                System.out.println("over once");
            }
        });

        ExecutorService executorService = Executors.newCachedThreadPool();
        for(int i=0; i<5; i++){
            executorService.execute(new Person(cyclicBarrier,i));
        }

        executorService.shutdown();

    }
}
