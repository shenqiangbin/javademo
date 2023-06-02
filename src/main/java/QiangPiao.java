import MyDate.DateUtil;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class QiangPiao {

    static int HOUR = 11;
    static int MINUTE = 43;
    static int QUAN = 5;

    public static void main(String[] args) throws Exception {

        connect();

        //创建定时器对象
        Timer timer = new Timer();
//        Timer timer = new Timer(true);  //设为守护线程模式

        // 开枪按钮 608 1575
        // 272 759 日期
        //

//        String middleBtn = "/Users/adminqian/soft/platform-tools/adb shell input tap 608 1575";
//        click(middleBtn);
//
//        String middleBtn = "/Users/adminqian/soft/platform-tools/adb shell input tap 272 259";
//        click(middleBtn);

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
//        String leftButton = "d:/adb/adb.exe shell input tap 124.5  211.8";
//        click(leftButton);

        String str = "/Users/adminqian/soft/platform-tools/adb shell input tap 608 1575";
        click(str);
        Thread.sleep(1000);
        //str = "/Users/adminqian/soft/platform-tools/adb shell input tap 257 753";
        str = "/Users/adminqian/soft/platform-tools/adb shell input tap 325 609";
        click(str);
        Thread.sleep(300);
        //str = "/Users/adminqian/soft/platform-tools/adb shell input tap 594 1162";
        str = "/Users/adminqian/soft/platform-tools/adb shell input tap 228 1008";
        click(str);
//        Thread.sleep(10);
        str = "/Users/adminqian/soft/platform-tools/adb shell input tap 608 1575";
        click(str);
        Thread.sleep(1000);

        // 选人
        str = "/Users/adminqian/soft/platform-tools/adb shell input tap 481 998";
        click(str);

        // 点击确定
        str = "/Users/adminqian/soft/platform-tools/adb shell input tap 859 1575";
        click(str);
        Thread.sleep(10);
    }

    public static void click(String cmd) throws IOException, InterruptedException {
        CmdUtil.execCmdSync(cmd, (success, exitVal, error, output, originalCmd) -> {
            if (!success) {
                throw new IOException(error);
            }
        });
    }

    public static void connect() throws IOException, InterruptedException {
        //String cmd = "d:/adb/adb.exe connect 127.0.0.1:7555";
        String cmd = "/Users/adminqian/soft/platform-tools/adb connect 127.0.0.1:7555";
        CmdUtil.execCmdSync(cmd, (success, exitVal, error, output, originalCmd) -> {
            System.out.println(output);
            System.out.println(error);
        });
    }
}
