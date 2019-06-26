package ThreadDemo;

import common.P;

public class demo01 {
    public static void main(String[] args){
       /*
        线程创建
        */
//
//        Thread thread2 = new Thread(new MyWorkThread("name2"));
//        thread2.start();
//
//        System.out.println("主线程接着走...");

        /*
        * 线程创建2，匿名线程
        * */

        Thread oneThread = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("开始干活了...");
                try {
                    //模拟业务耗时
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("干完活了...");

            }
        });

        oneThread.start();
//
//
//        P.print("当前活动的线程数:" + Thread.activeCount());
//
//
//        //由于idea有个守护线程，所以这里的activeCount是2，而不是1
//        while(Thread.activeCount() > 2){
//            P.print("当前活动的线程数:" + Thread.activeCount());
//            Thread.yield();
//        }
//        P.print("当前活动的线程数:" + Thread.activeCount());
//
//
//        P.print("main thread over");
    }


}
