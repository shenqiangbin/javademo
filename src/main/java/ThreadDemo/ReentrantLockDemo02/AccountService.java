package ThreadDemo.ReentrantLockDemo02;

import java.util.concurrent.locks.ReentrantLock;

public class AccountService {

    ReentrantLock lock = new ReentrantLock();

    public void add(Account account) {

        //sleep一下，保证多个线程同时运行下面的方法
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        lock.lock();

        try {
            account.setNum(account.getNum() + 1);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}



