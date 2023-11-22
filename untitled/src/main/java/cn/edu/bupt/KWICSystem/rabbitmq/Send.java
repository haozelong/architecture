package cn.edu.bupt.KWICSystem.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

public class Send {
    private final static String QUEUE_NAME = "q_test_01";

    public static void send(String message) throws Exception {
        // 获取到连接以及mq通道
        Connection connection = RabbitMq.getConnection();
        // 从连接中创建通道
        Channel channel = connection.createChannel();

        // 声明（创建）队列
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        // 消息内容
//        String message = "Hello World!";
//        System.out.println(message);
        channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
        System.out.println(" 发送消息 '" + message + " '到消息队列 'q_test_01' 中：");
        //关闭通道和连接
        channel.close();
        connection.close();
    }
}
