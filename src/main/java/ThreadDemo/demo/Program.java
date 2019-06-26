package ThreadDemo.demo;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Program {
    public static void main(String[] args) {
        Account accountA = new Account(20, "账户A");
        Account accountB = new Account(30, "账户B");

        AccountService service = new AccountService();

        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(new Person(0, service, accountA, accountB));
        executorService.execute(new Person(1, service, accountB, accountA));

        executorService.shutdown();
    }
}
