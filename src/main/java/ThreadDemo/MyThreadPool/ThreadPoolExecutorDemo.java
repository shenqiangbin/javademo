package ThreadDemo.MyThreadPool;

import java.sql.SQLSyntaxErrorException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadPoolExecutorDemo {

    private static final int COUNT_BITS = Integer.SIZE - 3;
    private static final int CAPACITY   = (1 << COUNT_BITS) - 1;

    // runState is stored in the high-order bits
    private static final int RUNNING    = -1 << COUNT_BITS;
    private static final int SHUTDOWN   =  0 << COUNT_BITS;
    private static final int STOP       =  1 << COUNT_BITS;
    private static final int TIDYING    =  2 << COUNT_BITS;
    private static final int TERMINATED =  3 << COUNT_BITS;


    private static int runStateOf(int c)     { return c & ~CAPACITY; }
    private static int workerCountOf(int c)  { return c & CAPACITY; }
    private static int ctlOf(int rs, int wc) { return rs | wc; }

    public static void main(String[] args) {

        System.out.println(Integer.toBinaryString(RUNNING));
        System.out.println(Integer.toBinaryString(SHUTDOWN));
        System.out.println(Integer.toBinaryString(STOP));
        System.out.println(Integer.toBinaryString(TIDYING));
        System.out.println(Integer.toBinaryString(TERMINATED));
        System.out.println(Integer.toBinaryString(CAPACITY));
        System.out.println(Integer.toBinaryString(2 & ~CAPACITY));

        //AtomicInteger ctl = new AtomicInteger(ctlOf(TIDYING, 0));
        AtomicInteger ctl = new AtomicInteger(TIDYING);
        System.out.println(ctl.get() == TIDYING);
       System.out.println( runStateOf(ctl.get()) == TIDYING);



        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(4, 4, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue<>());

        getInfo(threadPoolExecutor);

        Task task = new Task("1");
        threadPoolExecutor.execute(task);

        getInfo(threadPoolExecutor);

        threadPoolExecutor.execute(new Task("2"));
        threadPoolExecutor.execute(new Task("3"));
        threadPoolExecutor.execute(new Task("4"));
        threadPoolExecutor.execute(new Task("5"));

        getInfo(threadPoolExecutor);

        threadPoolExecutor.execute(new Task("6"));
        threadPoolExecutor.execute(new Task("7"));

        getInfo(threadPoolExecutor);

        threadPoolExecutor.shutdown();
    }

    public static void getInfo(ThreadPoolExecutor executor) {
        System.out.println("pool size:" + executor.getPoolSize());
        System.out.println("active size:" + executor.getActiveCount());
    }
}

