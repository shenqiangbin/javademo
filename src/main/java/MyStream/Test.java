package MyStream;

import MyModel.Person;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Test {

    public static void main(String[] args) {
        List<Person> list = new ArrayList<>();

        Person p = new Person();
        p.setId(1);
        p.setName("tom");

        list.add(p);

        p = new Person();
        p.setId(1);
        p.setName("tom");

        list.add(p);

        p = new Person();
        p.setId(2);
        p.setName("jerry");

        list.add(p);

        list = list.stream().distinct().collect(Collectors.toList());

        System.out.println(list.size());
    }
}
