package ThreadDemo.CompletableFutureDemo;

import MyDate.DateUtil;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * 多个任务并行处理，然后统计结果
 */
public class CompleteFutureDemo02 {

    public static void main(String[] args) {

        System.out.println("time:" + DateUtil.format(new Date()) + " -----  start");

        List<String> filePaths = Arrays.asList("1", "2", "3");
        List<CompletableFuture<String>> list =
                filePaths.stream()
                        .map(path -> doSomething(path))
                        .collect(Collectors.toList());

//        CompletableFuture[] arr = list.toArray(new CompletableFuture[list.size()]);
//        CompletableFuture completableFuture = CompletableFuture.allOf(arr);
//
//        try {
//            completableFuture.join();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        List<String> results = list.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());
        System.out.println(results);

        System.out.println("time:" + DateUtil.format(new Date()) + " thread:" + Thread.currentThread() + " -----  end");
    }

    // 3 个线程同时处理
    static ExecutorService executor = Executors.newFixedThreadPool(3);

    private static CompletableFuture<String> doSomething(String path) {
        return CompletableFuture.supplyAsync(() -> {
            // do something
            try {
                System.out.println("time:" + DateUtil.format(new Date()) + " thread:" + Thread.currentThread() + " path:" + path);
                Thread.sleep(1000*5);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("time:" + DateUtil.format(new Date()) + " thread:" + Thread.currentThread() + " path:" + path);
            return "path:" + path;
        }, executor ).whenComplete((v, e) -> {
//            System.out.println(v);
//            System.out.println(e);
        });
    }


}
