package cn.edu.bupt.KWICSystem;

import cn.edu.bupt.KWICSystem.kwic.KWIC;
import cn.edu.bupt.KWICSystem.kwic.impl.KWICImpl;
import cn.edu.bupt.KWICSystem.rabbitmq.Consumer;
import cn.edu.bupt.KWICSystem.rabbitmq.RabbitMq;
import cn.edu.bupt.KWICSystem.rabbitmq.Send;

public class Main {

    public static void main(String[] args) throws Exception {
//        RabbitMq.getConnection();
//        Send.send("nihaoya");
//        Consumer.getMessage();
        KWIC kwic = new KWICImpl();
        kwic.execute();

    }
}
