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


        //由于idea有个守护线程，所以这里的activeCount是2，而不是1
        while(Thread.activeCount() > 2){
            P.print("当前活动的线程数:" + Thread.activeCount());
            Thread.yield();
        }
        P.print("当前活动的线程数:" + Thread.activeCount());


        P.print("main thread over");
    }


}
