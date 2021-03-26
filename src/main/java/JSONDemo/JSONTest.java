package JSONDemo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JSONTest {

    public static void main(String[] args) throws IOException {
        //test();
        test2();

        handle();

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

    public static void handle() throws IOException {

        FileInputStream fileInputStream = null;
        BufferedReader bufferedReader = null;

        fileInputStream = new FileInputStream("d:/json.txt");
        InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
        bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));

        StringBuilder builder2 = new StringBuilder();

        int word;
        while ((word = bufferedReader.read()) != -1) {
            builder2.append((char) word);
        }

        System.out.println(builder2.toString());

        String sql = "INSERT INTO `bd`.`intentionenum`(`enumid`, `code`, `name`, `status`, `updatetime`, `createtime`, `updateuser`, `createuser`) VALUES ( 4703, '%s', '%s', 1, '2020-12-16 23:03:10', '2020-12-16 23:03:10', 'sa', 'sa');";
        String s2 = builder2.toString().replace("\n", "");
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> map = mapper.readValue(s2, Map.class);
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, String> item : map.entrySet()) {
            String sql2 = String.format(sql, item.getKey(), item.getValue());
            builder.append(sql2).append("\r\n");
        }
        System.out.println(builder.toString());
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

    public static void test2() throws IOException {
        String str = "[{\"name\":\"小明\",\"age\":10},{\"name\":\"小王\",\"age\":20}]";
//        ObjectMapper mapper = new ObjectMapper();
//        JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class, Person.class);
//        List<Person> list = mapper.readValue(str, javaType);
        List<Person> list = toListObject(str,Person.class);
        System.out.println(list);
    }

    public static <T> List<T> toListObject(String content, Class<T> valueType) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class, valueType);
        return mapper.readValue(content, javaType);
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
