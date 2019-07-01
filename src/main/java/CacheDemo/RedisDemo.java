package CacheDemo;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisStringCommands;

public class RedisDemo {

    /**
     * 参考地址：https://github.com/lettuce-io/lettuce-core
     * pom添加依赖
     * <dependency>
     * <groupId>io.lettuce</groupId>
     * <artifactId>lettuce-core</artifactId>
     * <version>5.1.7.RELEASE</version>
     * </dependency>
     */
    public static void main(String[] args) {
        RedisClient client = RedisClient.create("redis://192.168.163.134");
        StatefulRedisConnection<String, String> connection = client.connect();
        RedisStringCommands sync  = connection.sync();

        Object value = sync.get(CacheKey.SERVER_NAME);
        System.out.println(value);

        sync.set(CacheKey.SERVER_NAME,"job");

        value = sync.get(CacheKey.SERVER_NAME);
        System.out.println(value);


    }

    class CacheKey{
        public static final String SERVER_NAME = "server.name";
    }
}
