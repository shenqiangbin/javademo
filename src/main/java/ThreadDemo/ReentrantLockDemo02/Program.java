package ThreadDemo.ReentrantLockDemo02;

import common.P;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Program {
    public static void main(String[] args) {

        int initCount = Thread.activeCount();
        System.out.println("当前活动的线程数:" + initCount);

        Account account = new Account();

        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 0; i < 10; i++) {
            executorService.execute(() -> {
                AccountService accountService = new AccountService();
                accountService.add(account);
            });
        }

        executorService.shutdown();

        //保证上面的线程全部执行完毕
        while(Thread.activeCount() > initCount){
            Thread.yield();
        }

        System.out.println("当前活动的线程数:" + Thread.activeCount());

        System.out.println(account.getNum());
    }
}
