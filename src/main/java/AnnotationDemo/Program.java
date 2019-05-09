package AnnotationDemo;


import common.P;

@IsUseful(who="xiaoming")
class Person{

    @IsUseful
    public void say(){}
}

public class Program {

    public static void main(String[] args){


        //通过反射判断是否使用了某个注解
        boolean isUseful = Person.class.isAnnotationPresent(IsUseful.class);

        P.print(isUseful);

        //获取注解的信息
        IsUseful isUseful1 = Person.class.getAnnotation(IsUseful.class);
        P.print(isUseful1);
        P.print(isUseful1.who());

    }


}
