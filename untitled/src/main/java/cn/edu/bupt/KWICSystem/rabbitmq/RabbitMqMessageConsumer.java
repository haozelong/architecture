package cn.edu.bupt.KWICSystem.rabbitmq;

import com.rabbitmq.client.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;

public class RabbitMqMessageConsumer extends DefaultConsumer {
    public RabbitMqMessageConsumer(Channel channel) {
        super(channel);
    }
    public void Delivery(String consumerTag, Envelope envelope, BasicProperties properties, byte[] body) throws IOException {
    }
}