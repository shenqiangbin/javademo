package ThreadDemo.SynchronizedDemo;

public class SyncThreadClass implements Runnable{
    private int count = 0;

    @Override
    public void run() {
        synchronized (SyncThreadClass.class) {
            for (int i = 0; i < 5; i++) {
                count++;
                System.out.println(Thread.currentThread().getName() + " - " + (count));
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public int getCount() {
        return count;
    }
}
