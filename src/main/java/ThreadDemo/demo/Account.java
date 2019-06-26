package ThreadDemo.demo;

public class Account {
    private double currentAmout;
    private String name;

    public Account(double currentAmout,String name){
        this.currentAmout = currentAmout;
        this.name = name;
    }

    public double getCurrentAmout() {
         return this.currentAmout;
    }

    public String getName(){
        return this.name;
    }

    //取钱
    public void debit(double amount) throws Exception {
        if (this.currentAmout < amount) {
            throw new Exception("余额不足");
        } else {
            this.currentAmout = this.currentAmout - amount;
        }

    }

    //存钱
    public void credit(double amount) {
        this.currentAmout = this.currentAmout + amount;
    }
}
