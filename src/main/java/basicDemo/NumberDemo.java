package basicDemo;

import java.text.NumberFormat;

public class NumberDemo {
    public static void main(String[] args) {

        for (int i = -4; i <= 128; i++) {
            System.out.println(i + " " + Integer.toHexString(i) + " " + Integer.toBinaryString(i));

            //System.out.printf("%x\n",x);//按16进制输出
            //System.out.printf("%o\n",x);//按8进制输出
        }

        Integer s2 = 0x80000000;
        System.out.println(Math.pow(-2, 31));
        System.out.println(s2);
        System.out.println(Integer.toHexString(s2));
        System.out.println(Integer.toBinaryString(s2));

        //  h % length  等于 h & (length -1 ) 【length 为 2 的 n 次方]

        for(int i=0; i<= 20; i++){
            int length = 8;
            System.out.println(i % length);
            System.out.println(i & (length-1));
        }

        //32 位，有正负，最小值为 -2(n次) 即 -2（32次）
//        System.out.println(Math.pow(2, 0));
//        System.out.println(Math.pow(2, 1));
//        System.out.println(Math.pow(2, 2));
//        System.out.println(Math.pow(2, 32));
    }

    static String toPlainNumber(double number) {
        NumberFormat nf = NumberFormat.getInstance();
        nf.setGroupingUsed(false);
        return nf.format(number);
    }

    static String toPlainNumber(long number) {
        NumberFormat nf = NumberFormat.getInstance();
        nf.setGroupingUsed(false);
        return nf.format(number);
    }
}
