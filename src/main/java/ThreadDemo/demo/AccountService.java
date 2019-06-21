package ThreadDemo.demo;

import java.util.concurrent.locks.ReentrantLock;

public class AccountService {

    private final ReentrantLock reentrantLock = new ReentrantLock();

    public void transferMoney(Account fromAccount, Account toAccount, double amount) {

        System.out.println("获取FromAccount的锁");
        synchronized (fromAccount) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("获取toAccount的锁");
            synchronized (toAccount) {
                try {
                    fromAccount.debit(amount);
                    toAccount.credit(amount);

                    System.out.println("转账人：" + fromAccount.getName() + ":" + fromAccount.getCurrentAmout());
                    System.out.println("收账人：" + toAccount.getName() + ":" + toAccount.getCurrentAmout());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void transferMoney2(Account fromAccount, Account toAccount, double amount) {

        System.out.println("获取FromAccount的锁");
        if(reentrantLock.tryLock())
        synchronized (fromAccount) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("获取toAccount的锁");
            synchronized (toAccount) {
                try {
                    fromAccount.debit(amount);
                    toAccount.credit(amount);

                    System.out.println("转账人：" + fromAccount.getName() + ":" + fromAccount.getCurrentAmout());
                    System.out.println("收账人：" + toAccount.getName() + ":" + toAccount.getCurrentAmout());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
