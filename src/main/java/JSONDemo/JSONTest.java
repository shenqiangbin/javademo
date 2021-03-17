package JSONDemo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JSONTest {

    public static void main(String[] args) throws IOException {
        //test();

        HashMap<String,String> hashMap = new HashMap();
        Object asd = hashMap.put("a", "b");

        String put = hashMap.put("", "");
        System.out.println("put = " + put);
        System.out.println(put);



        ObjectMapper mapper = new ObjectMapper();
        List<String> list = new ArrayList<String>();
        list.add("学校");
        list.add("学校,大学");
        list.add("学校\"大学");
        String jsonString2 = mapper.writeValueAsString(list);
        System.out.println(jsonString2);
    }

    /*
        <dependency>
            <groupId>com.fasterxml.jackson.core</groupId>
            <artifactId>jackson-databind</artifactId>
            <version>2.9.1</version>
        </dependency>
    */

    public static void test() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Person person = new Person();
        person.setName("Tom");
        person.setAge(40);

        String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(person);
        String jsonString2 = mapper.writeValueAsString(person);

        Person deserializedPerson = mapper.readValue(jsonString, Person.class);

        // 可以使用此方法实现 JSON 的规范化 和 去规范化
        System.out.println(jsonString);
        System.out.println(jsonString2);
    }
}

class Person {
    private String name;
    private int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
