package ThreadDemo.CountDownLatchDemo;

import java.util.concurrent.atomic.AtomicLong;

public class OrderIdGenerator {
    private static final AtomicLong lastTimestamp = new AtomicLong(-1L);
    private static final AtomicLong sequence = new AtomicLong(10000L); // 序列号，可以根据需要调整位数

    public static synchronized String generate() {
        long timestamp = System.currentTimeMillis();
        if (timestamp < lastTimestamp.get()) {
            throw new RuntimeException("Clock moved backwards. Refusing to generate order id");
        }

        // 如果是同一时间生成的，则进行毫秒内序列
        long sequenceId;
        if (timestamp == lastTimestamp.get()) {
            sequenceId = sequence.getAndIncrement();
            if (sequenceId >= 10000L) {
                sequenceId = 0;
            }
        } else {
            // 如果是不同时间生成，毫秒内序列重置
            sequenceId = 1;
            sequence.set(1);
        }

        lastTimestamp.set(timestamp);

        // 订单号格式：当前时间戳 + 序列号
        return String.format("%d-%03d", timestamp, sequenceId);
    }

    public static void main(String[] args) {
        // 测试生成订单号
        for (int i = 0; i < 10; i++) {
            System.out.println(OrderIdGenerator.generate());
        }
    }
}