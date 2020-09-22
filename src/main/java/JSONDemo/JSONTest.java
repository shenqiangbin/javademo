package JSONDemo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class JSONTest {

    public static void main(String[] args) throws IOException {
        test();
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
