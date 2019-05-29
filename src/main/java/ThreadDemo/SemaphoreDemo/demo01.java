package ThreadDemo.SemaphoreDemo;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class demo01 {

    public static void main(String[] args) {
        ExecutorService executors = Executors.newCachedThreadPool();

        Semaphore semaphore = new Semaphore(3);

        for(int i=0; i<20; i++){
            final int number = i;
            executors.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        semaphore.acquire();
                        System.out.println("go " + number + " " + new Date());
                        Thread.sleep(2000);
                        semaphore.release();

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        executors.shutdown();
    }


}
