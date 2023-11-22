package cn.edu.bupt.KWICSystem.rabbitmq;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class Consumer {
    public static String rabbitMqRes = "";
    // 交换机
//    private final static String QUEUE_NAME = "test_queue_work";
//
//    public static void getMessage() throws Exception {
//
//        // 获取到连接以及mq通道
//        Connection connection = RabbitMq.getConnection();
//        Channel channel = connection.createChannel();
//
//        // 声明队列
//        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
//
//        // 同一时刻服务器只会发一条消息给消费者
//        channel.basicQos(1);
//
//        // 定义队列的消费者
//        DefaultConsumer consumer = new DefaultConsumer(channel) {
//            public void handleDelivery(String consumerTag, Envelope envelope,
//                                       BasicProperties properties,byte[] body) throws IOException {
//                System.out.println("received message -> "+new String(body));
//            }
//        };
//        consumer.handleDelivery();
//
//        // 监听队列，false表示手动返回完成状态，true表示自动
//        channel.basicConsume(QUEUE_NAME, true, consumer);
//
//        // 获取消息
//        while (true) {
//            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
//            String message = new String(delivery.getBody());
//            System.out.println(" [y] Received '" + message + "'");
//            //休眠
//            Thread.sleep(10);
//            // 返回确认状态，注释掉表示使用自动确认模式
//            //channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
//        }
//    }

    private static final String QUEUE_NAME = "q_test_01";
    private static final String IP_ADDRESS = "127.0.0.1";
    private static final int PORT = 5672; //RabbitMQ 服务端默认端口号为 5672
    private static final String USER_NAME = "guest";
    private static final String PASSWORD = "guest";

    public static void getMessage() throws IOException, TimeoutException,InterruptedException
    {
        String res;
        Address[] addresses = new Address[]{new Address(IP_ADDRESS, PORT)};
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername(USER_NAME);
        factory.setPassword(PASSWORD);
        Connection connection = factory.newConnection(addresses); //创建连接
        final Channel channel = connection.createChannel(); //创建信道
        channel.exchangeDeclare("amqp.fanout","fanout");
        channel.queueBind(QUEUE_NAME, "amqp.fanout","");
        channel.basicQos(1); //设置客户端最多接收未被ack的消息的个数
        DefaultConsumer consumer = new DefaultConsumer(channel)
        {
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException
            {
                rabbitMqRes = new String(body);
                System.out.println("接收消息队列 'q_test_01' 中的信息：" + new String(body));
                try
                {
                    TimeUnit.SECONDS.sleep(1);
                }
                catch (InterruptedException ie)
                {
                    ie.printStackTrace();
                }
                channel.basicAck(envelope.getDeliveryTag(), false);
            }
        };
        channel.basicConsume(QUEUE_NAME, consumer);
        //等待回调函数执行完毕之后，关闭资源
        TimeUnit.SECONDS.sleep(5);
        channel.close();
        connection.close();
    }

}
