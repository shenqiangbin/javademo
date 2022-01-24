package ThreadDemo.SynchronizedDemo;

public class Program {
    public static void main(String[] args) {
//        SyncThread syncThread = new SyncThread();
//        Thread thread1 = new Thread(syncThread, "thread1");
//        Thread thread2 = new Thread(syncThread, "thread2");
//        thread1.start();
//        thread2.start();


//        new Thread(new SyncThread(), "thread1").start();
//        new Thread(new SyncThread(), "thread2").start();

//        SyncThreadLock syncThreadLock = new SyncThreadLock();
//        new Thread(syncThreadLock, "thread-A").start();
//        new Thread(syncThreadLock, "thread-B").start();

        new Thread(new SyncThreadClass(), "thread1").start();
        new Thread(new SyncThreadClass(), "thread2").start();
    }
}
