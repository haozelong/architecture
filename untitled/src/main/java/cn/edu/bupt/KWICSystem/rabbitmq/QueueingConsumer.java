//package cn.edu.bupt.KWICSystem.rabbitmq;
//
//import com.rabbitmq.client.*;
//
//import java.io.IOException;
//import java.util.concurrent.LinkedBlockingQueue;
//import java.util.concurrent.TimeUnit;
//
//public class QueueingConsumer extends DefaultConsumer {
//private LinkedBlockingQueue<Delivery> queue;
//    public QueueingConsumer(Channel channel) {
//        super(channel);
//        queue = new LinkedBlockingQueue<RabbitMqMessageConsumer.Delivery>();
//    }
//    public QueueingConsumer(Channel channel,int size) {
//        super(channel);
//        queue = new LinkedBlockingQueueQueuedBlockingQueue<RabbitMqMessageConsumer.Delivery>(size);
//    }
//    public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties, byte[] body) throws IOException, InterruptedException {
//        Delivery delivery = new Delivery();
//        delivery.setBody(body);
//        delivery.setProperties(properties);
//        delivery.setEnvelope(envelope);
//        try {
//            queue.put(delivery);
//        } catch (InterruptedException e) {
//                throw e;
//            }
//        }
//        public Delivery nextDelivery()throws InterruptedException, ShutdownSignalException, ConsumerCancelledException{
//            return queue.take();
//        }
//        public Delivery nextDelivery(long timeout)throws InterruptedException, ShutdownSignalException, ConsumerCancelledException{
//            return queue.poll(timeout, TimeUnit.MILLISECONDS);
//        }
//        public class Delivery{
//        private BasicProperties properties;
//        private byte[] body;
//        private Envelope envelope;
//        public BasicProperties getProperties() {
//            return properties;
//        }
//        public void setProperties(BasicProperties properties) {
//            this.properties = properties;
//        }
//        public byte[] getBody() {
//            return body;
//        }
//        public void setBody(byte[] body) {
//            this.body = body;
//        }
//        public Envelope getEnvelope() {
//            return envelope;
//        }
//        public void setEnvelope(Envelope envelope) {
//            this.envelope = envelope;
//        }
//    }
//}