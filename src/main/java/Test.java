import MyDate.DateUtil;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class Test {

    static int HOUR = 14;
    static int MINUTE = 0;
    static int QUAN = 5;

    public static void main(String[] args) throws Exception {

        connect();

        //创建定时器对象
        Timer timer = new Timer();
//        Timer timer = new Timer(true);  //设为守护线程模式


        String middleBtn = "d:/adb/adb.exe shell input tap 405.5  211.8";
        click(middleBtn);

        //指定定时任务
        timer.schedule(new TimerTask() {
            //编写一个匿名内部类，即定时任务
            @Override
            public void run() {
                System.out.println("timer-" + DateUtil.format(new Date()));

                int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
                int minute = Calendar.getInstance().get(Calendar.MINUTE);

                //System.out.println(String.format("hour:%s, minute:%s", hour,minute));

                if (hour == HOUR && minute == MINUTE) {
                    timer.cancel();
                    try {
                        go();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }


            }
        }, new Date(), 500);


//        click(middleBtn);
//        click(leftButton);


    }

    public static void go() throws IOException, InterruptedException {
        String leftButton = "d:/adb/adb.exe shell input tap 124.5  211.8";
        click(leftButton);

        String q1 = "d:/adb/adb.exe shell input tap 699  346.7";
        String q2 = "d:/adb/adb.exe shell input tap 699  575.5";
        String q3 = "d:/adb/adb.exe shell input tap 699  807.4";
        String q4 = "d:/adb/adb.exe shell input tap 699  1039";
        String q5 = "d:/adb/adb.exe shell input tap 699  1240";

        String str =
                QUAN == 1 ? q1 :
                QUAN == 2 ? q2 :
                QUAN == 3 ? q3 :
                QUAN == 4 ? q4 :
                QUAN == 5 ? q5 : "";

        System.out.println(str);

        for (int i = 0; i < 20; i++) {
            System.out.println("click-" + DateUtil.format(new Date()));
            click(str);
            Thread.sleep(10);
        }
    }

    public static void click(String cmd) throws IOException, InterruptedException {
        CmdUtil.execCmdSync(cmd, (success, exitVal, error, output, originalCmd) -> {
            if (!success) {
                throw new IOException(error);
            }
        });
    }

    public static void connect() throws IOException, InterruptedException {
        String cmd = "d:/adb/adb.exe connect 127.0.0.1:7555";
        CmdUtil.execCmdSync(cmd, (success, exitVal, error, output, originalCmd) -> {
            System.out.println(output);
            System.out.println(error);
        });
    }
}
