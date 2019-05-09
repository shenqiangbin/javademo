package ThreadDemo;

import common.P;

public class MyWorkThread implements Runnable {

    private String name;

    public MyWorkThread(String name){
        this.name = name;
    }

    @Override
    public void run() {
        P.print(this.name);
    }
}
