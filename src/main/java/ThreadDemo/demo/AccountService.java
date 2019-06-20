package ThreadDemo.demo;

public class AccountService {
    public void transferMoney(Account fromAccount, Account toAccount, double amount) {

        System.out.println("获取FromAccount的锁");
        synchronized (fromAccount) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("获取toAccount的锁");
            synchronized (toAccount) {
                try {
                    fromAccount.debit(amount);
                    toAccount.credit(amount);

                    System.out.println("转账人：" + fromAccount.getName() + ":" + fromAccount.getCurrentAmout());
                    System.out.println("收账人：" + toAccount.getName() + ":" + toAccount.getCurrentAmout());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
