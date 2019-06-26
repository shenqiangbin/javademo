package ThreadDemo.demo;

public class Person implements Runnable {

    private AccountService service;
    private Account accountA;
    private Account accountB;
    private int i;

    public Person(int i,AccountService service,Account accountA,Account accountB){
        this.i = i;
        this.service = service;
        this.accountA = accountA;
        this.accountB = accountB;
    }

    @Override
    public void run() {
        System.out.println("thread" + i + "start");
        service.transferMoney(accountA, accountB, 1);
        System.out.println("thread" + i + "end");
    }
}
