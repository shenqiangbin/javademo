public class RegexDemo {

    public static void main(String[] args) {

        System.out.println(isUTCtime("2020-08-20T02:53:19.000Z"));
//        System.out.println(isUTCtime(""));
//        System.out.println(isUTCtime(""));
//        System.out.println(isUTCtime(""));
    }

    private static boolean isUTCtime(String time){
        return time.matches("^[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}.[0-9]{3}Z$");
    }
}
