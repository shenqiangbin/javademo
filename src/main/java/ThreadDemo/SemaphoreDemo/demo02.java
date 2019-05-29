package ThreadDemo.SemaphoreDemo;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class demo02 {
    public static void main(String[] args) {


        ParkingSpace parkingSpace = new ParkingSpace();

        ExecutorService executorService = Executors.newCachedThreadPool();

        Semaphore semaphore = new Semaphore(2,true);

        for (int i=0; i<5; i++){
            int number = i+1;
            executorService.execute(() -> {
                try {
                    Car car = new Car(number);
                    semaphore.acquire();

                    parkingSpace.park(car);
                    Thread.sleep(2000);
                    parkingSpace.go(car);

                    semaphore.release();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

        executorService.shutdown();

    }
}
