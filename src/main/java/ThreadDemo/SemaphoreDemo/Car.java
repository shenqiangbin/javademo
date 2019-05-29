package ThreadDemo.SemaphoreDemo;

public class Car {
    private int number;

    public Car(int number){
        this.number = number;
    }

    @Override
    public String toString() {
        return "车" + this.number;
    }
}
