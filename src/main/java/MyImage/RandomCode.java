package MyImage;

import common.P;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.apache.commons.codec.digest.DigestUtils;

public class RandomCode {
    public static void main(String[] args) {
        String code = getCode(4);
        P.print(code);

        code = getNumCode(4);
        P.print(code);

        code = getNumCode(4);
        P.print(code);

        code = getNumCode(6);
        P.print(code);

        code = getNumCode(6);
        P.print(code);

        /*其他随机字符串生成方法*/
        P.print(java.util.UUID.randomUUID().toString());
        P.print(java.util.UUID.randomUUID().toString().replace("-", ""));


        code = getCode(4);
        String time = new SimpleDateFormat("yyyyMMdd HH:mm:ss").format(new Date());
        String md5 = DigestUtils.md5Hex(code + time + "540de77bc32847828b85c84217ca4c32");
        P.print("thecode:");
        P.print(md5);

        Random randomer = new Random();
        randomer.nextInt(10000 + 1);
    }

    /**
     * @param codeLength 随机验证码的长度
     * @return 由大小写字母和数字组成的字符串
     */
    public static String getCode(int codeLength) {

        Random randomer = new Random();
        StringBuilder builder = new StringBuilder();

        char[] elements = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R',
                'S', 'T', 'V', 'U', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
                'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '1', '2', '3', '4', '5', '6', '7', '8',
                '9'};

        for (int i = 0; i < codeLength; i++) {
            int index = randomer.nextInt(elements.length);
            builder.append(elements[index]);
        }

        return builder.toString();
    }


    /**
     * @param codeLength 随机验证码的长度
     * @return 由数字组成的字符串
     */
    public static String getNumCode(int codeLength) {

        Random randomer = new Random();
        StringBuilder builder = new StringBuilder();

        char[] elements = {'1', '2', '3', '4', '5', '6', '7', '8', '9'};

        for (int i = 0; i < codeLength; i++) {
            int index = randomer.nextInt(elements.length);
            builder.append(elements[index]);
        }

        return builder.toString();
    }
}
