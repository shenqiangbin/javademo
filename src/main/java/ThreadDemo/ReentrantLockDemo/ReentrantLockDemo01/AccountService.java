package ThreadDemo.ReentrantLockDemo.ReentrantLockDemo01;

import java.util.concurrent.locks.ReentrantLock;

public class AccountService {

    private final ReentrantLock reentrantLock = new ReentrantLock();

    public void add1(int num) {
        DbHelper.instance.add(num);
    }

    public void add2(int num) {
        reentrantLock.lock();
        try {
            DbHelper.instance.add(num);
        } finally {
            reentrantLock.unlock();
        }
    }

    public void addAcc(Account account) {
        account.add();
    }

    public void addAcc2(Account account) {

        System.out.println("add 获取锁");
        reentrantLock.lock();
        System.out.println("add 获取锁 成功");
        try {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            account.add();
        } finally {
            System.out.println("add 释放锁");
            reentrantLock.unlock();
        }

    }

    public void dsAcc2(Account account) {

//        System.out.println("ds 执行");
//        account.des();

        System.out.println("des 获取锁");
        reentrantLock.lock();
        System.out.println("des 获取锁 成功");
        try {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            account.des();
        } finally {
            System.out.println("des 释放锁");
            reentrantLock.unlock();
        }
    }

    public void add(int num) {
        System.out.println("add 获取锁");
        reentrantLock.lock();
        try {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            DbHelper.instance.add(num);
        } finally {
            System.out.println("add 释放锁");
            reentrantLock.unlock();
        }
    }

    public void des(int num) {
        reentrantLock.lock();
        try {
            DbHelper.instance.des(num);
        } finally {
            reentrantLock.unlock();
        }
    }
}
