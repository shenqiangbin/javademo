package CacheDemo;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisFuture;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.api.sync.RedisStringCommands;

import java.util.concurrent.ExecutionException;

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

        try {

            AsyncDemo();

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void syncDemo(){
        RedisClient client = RedisClient.create("redis://192.168.163.134");
        StatefulRedisConnection<String, String> connection = client.connect();
        RedisStringCommands sync  = connection.sync();

        Object value = sync.get(CacheKey.SERVER_NAME);
        System.out.println(value);

        sync.set(CacheKey.SERVER_NAME,"job");

        value = sync.get(CacheKey.SERVER_NAME);
        System.out.println(value);

        connection.close();
        client.shutdown();
    }

    private static void AsyncDemo() throws ExecutionException, InterruptedException {
        RedisClient client = RedisClient.create("redis://192.168.163.134");
        StatefulRedisConnection<String, String> connection = client.connect();
        RedisAsyncCommands sync  = connection.async();

        RedisFuture<String> future = sync.get(CacheKey.SERVER_NAME);
        System.out.println(future.get());

        sync.set(CacheKey.SERVER_NAME,"job");

        //value = sync.get(CacheKey.SERVER_NAME);
        //System.out.println(value);

        connection.close();
        client.shutdown();
    }

    class CacheKey{
        public static final String SERVER_NAME = "server.name";
    }
}
