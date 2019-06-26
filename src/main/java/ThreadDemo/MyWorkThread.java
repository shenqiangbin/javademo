package ThreadDemo;

public class MyWorkThread implements Runnable {

    private String name;

    public MyWorkThread(String name){
        this.name = name;
    }

    @Override
    public void run() {
        System.out.println(this.name+"开始干活了...");
        try {
            //模拟业务耗时
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(this.name+"干完活了...");
    }

}
