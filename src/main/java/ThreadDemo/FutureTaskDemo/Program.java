package ThreadDemo.FutureTaskDemo;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class Program {

    public static void main(String[] args) {
        CallableExample callableExample = new CallableExample();
        FutureTask futureTask = new FutureTask(callableExample);
        new Thread(futureTask).start();


        System.out.println("main do other");

        try {
            System.out.println(futureTask.get());

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        System.out.println("main end");
    }
}


class CallableExample implements Callable {
    @Override
    public Object call() throws Exception {
        Random random = new Random();
        int val = random.nextInt(5);

        Thread.sleep(2000);
        return val;
    }
}
