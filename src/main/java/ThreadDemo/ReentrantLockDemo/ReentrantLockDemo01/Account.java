package ThreadDemo.ReentrantLockDemo.ReentrantLockDemo01;

public class Account {
    private int num = 0;

    public void add(){
        this.num = this.num + 1;
    }

    public void des(){
        this.num = this.num - 1;
    }

    public int getNum(){
        return this.num;
    }
}
