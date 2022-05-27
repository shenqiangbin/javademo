package ThreadDemo.CyclicBarrierDemo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PersonOperator {
    public static void main(String[] args) {

        List<Person> list = new ArrayList<>();

        CyclicBarrier cyclicBarrier = new CyclicBarrier(3, new Runnable() {
            @Override
            public void run() {
                System.out.println("over once");
                int count = list.stream().mapToInt(Person::getNum).sum();
            }
        });

        ExecutorService executorService = Executors.newCachedThreadPool();
        for(int i=0; i<6; i++){
            Person p = new Person(cyclicBarrier,i);
            list.add(p);
            executorService.execute(p);

            executorService.execute(() -> {

            });
        }

        executorService.shutdown();

    }
}
