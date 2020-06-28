package com.hy.rabbit.ack.consumer;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

public class Ackconsumer {
    private static final String QUEUE_NAME = "ACK_QUEUE_TEST";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUri("amqp://guest:guest@127.0.0.1:5672");

        Connection connection = factory.newConnection();

        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME,false,false,false,null);

        Consumer consumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String messsage = new String(body, "UTF-8");
                System.out.println(messsage);

                if (messsage.contains("拒收")){
                    // 拒绝消息
                    // requeue：是否重新入队列，true：是；false：直接丢弃，相当于告诉队列可以直接删除掉
                    // TODO 如果只有这一个消费者，requeue 为true 的时候会造成消息重复消费
                    channel.basicReject(envelope.getDeliveryTag(),false);
                }else if (messsage.contains("异常")){
                    // 批量拒绝
                    // requeue：是否重新入队列
                    // TODO 如果只有这一个消费者，requeue 为true 的时候会造成消息重复消费
                    channel.basicNack(envelope.getDeliveryTag(), true,false);
                }else {
                    // 手工应答
                    // 如果不应答，队列中的消息会一直存在，重新连接的时候会重复消费
                    channel.basicAck(envelope.getDeliveryTag(),true);
                }
            }
        };

        // 开始获取消息，注意这里开启了手工应答
        channel.basicConsume(QUEUE_NAME,false,consumer);
    }
}
