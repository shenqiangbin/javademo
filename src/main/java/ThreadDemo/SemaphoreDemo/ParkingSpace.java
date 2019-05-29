package ThreadDemo.SemaphoreDemo;

public class ParkingSpace {
    private Car[] spaces = new Car[2];

    public void park(Car car) {
        boolean find = false;
        for (int i = 0; i < spaces.length; i++) {
            Car item = spaces[i];
            if (item == null) {
                spaces[i] = car;
                System.out.println(car.toString() + "停放在" + i);
                find=true;
                break;
            }
        }
        if(find==false)
            System.out.println(car.toString() + "没找到车位");
    }

    public void go(Car car) {
        boolean find = false;
        for (int i = 0; i < spaces.length; i++) {
            Car item = spaces[i];
            if (item == car) {
                spaces[i] = null;
                System.out.println(car.toString() + "离开了" + i);
                find =true;
                break;
            }
        }
        if(find == false)
            System.out.println(car.toString() + "没有此车");
    }
}
