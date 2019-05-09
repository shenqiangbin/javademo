package ThreadDemo;

import common.P;

public class demo01 {
    public static void main(String[] args){
        P.print("abc");

        Thread oneThread = new Thread(new Runnable() {
            @Override
            public void run() {
                P.print("thread start " + Thread.currentThread().getId() + " " +
                Thread.currentThread().getName());
            }
        });

        oneThread.start();


        Thread thread2 = new Thread(new MyWorkThread("name2"));
        thread2.setName("name2");
        //thread2.start();

        P.print("当前活动的线程数:" + Thread.activeCount());
        while(Thread.activeCount() > 1){
            P.print("当前活动的线程数:" + Thread.activeCount());
            Thread.yield();
        }
        P.print("当前活动的线程数:" + Thread.activeCount());


        P.print("main thread over");
    }


}
