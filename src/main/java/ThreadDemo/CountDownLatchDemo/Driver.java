package ThreadDemo.CountDownLatchDemo;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class Driver {

    void main(){

        CountDownLatch startSignal = new CountDownLatch(1);

        int number = 10;
        for(int i=0;i<number; i++){
            new Thread(new Worker(i,startSignal)).start();
        }

        try {
            System.out.println("driver 准备中");
            TimeUnit.SECONDS.sleep(new Random().nextInt(10));
            System.out.println("driver 准备完毕");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //driver准备完了，可以countDown了，等待的可以继续走了
        startSignal.countDown();

        System.out.println("driver do something else done");
    }

    public static void main(String[] args) {
        new Driver().main();
    }
}
