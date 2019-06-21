package ThreadDemo.ReentrantLockDemo.ReentrantLockDemo01;

public class DbHelper {

    public static final DbHelper instance = new DbHelper();

    private int val = 0;

    public void add(int num) {
        this.val = this.val + num;
    }

    public void des(int num) {
        this.val = this.val - num;
    }

    public int getVal(){
        return this.val;
    }
}
