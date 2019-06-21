package ThreadDemo.ReentrantLockDemo.ReentrantLockDemo01;

import common.P;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Program {
    public static void main(String[] args) {

        int initCount = Thread.activeCount();
        P.print("当前活动的线程数:" + initCount);

        AccountService accountService = new AccountService();

        Account account = new Account();

        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 0; i < 10; i++) {
            executorService.execute(()->{
                //accountService.add1(1);
                //accountService.add2(1);
                accountService.addAcc2(account);
            });
        }

        for (int i = 0; i < 10; i++) {
            executorService.execute(()->{
                //accountService.add1(1);
                //accountService.add2(1);
                accountService.dsAcc2(account);
            });
        }



        executorService.shutdown();

        while(Thread.activeCount() > initCount){
            Thread.yield();
        }

        P.print("当前活动的线程数:" + Thread.activeCount());

        //System.out.println(DbHelper.instance.getVal());
        System.out.println(account.getNum());
    }
}
