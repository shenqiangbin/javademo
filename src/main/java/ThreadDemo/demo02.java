package ThreadDemo;

public class demo02 {

    public static void main(String[] args) {
        // run 则输出1
        // debug 则输出2
        System.out.println(Thread.activeCount());

        String sql = "4546,4548,";
        String newq = sql.substring(0, sql.length()-1);
        System.out.println(newq);
    }

}
