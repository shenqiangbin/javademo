package ThreadDemo.MyThreadPool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FixedThreadPool {

    public static void main(String[] args) {
        ExecutorService service = Executors.newFixedThreadPool(3);

        Task task1 = new Task("task 1");
        Task task2 = new Task("task 2");
        Task task3 = new Task("task 3");
        Task task4 = new Task("task 4");
        Task task5 = new Task("task 5");

        service.execute(task1);
        service.execute(task2);
        service.execute(task3);
        service.execute(task4);
        service.execute(task5);

        service.shutdown();
    }


}

class Task implements Runnable {

    private String name;

    public Task(String name) {
        this.name = name;
    }

    @Override
    public void run() {

        System.out.println(this.name + " 运行中... ");

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(this.name + " 运行完毕 ");
    }
}
