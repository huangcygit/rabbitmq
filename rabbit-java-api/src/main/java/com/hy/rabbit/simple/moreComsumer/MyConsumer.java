package com.hy.rabbit.simple.moreComsumer;

import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * @Author: qingshan
 * @Date: 2018/9/21 10:53
 * @Description: 咕泡学院，只为更好的你
 * 消息消费者
 */
public class MyConsumer {
    private final static String QUEUE_NAME = "SIMPLE_QUEUE_M2";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        // 连接IP
        factory.setHost("127.0.0.1");
        // 默认监听端口
        factory.setPort(5672);
        // 虚拟机
        factory.setVirtualHost("/");

        // 设置访问的用户
        factory.setUsername("guest");
        factory.setPassword("guest");
        // 建立连接
        Connection conn = factory.newConnection();
        // 创建消息通道
        Channel channel = conn.createChannel();

        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        // 创建消费者
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
                                       byte[] body) throws IOException {
                String msg = new String(body, "UTF-8");
                System.out.println("Received message : '" + msg + "'");
                System.out.println("consumerTag : " + consumerTag );
                System.out.println("deliveryTag : " + envelope.getDeliveryTag() );
            }
        };

        // 开始获取消息
        // String queue, boolean autoAck, Consumer callback
        channel.basicConsume(QUEUE_NAME, true, consumer);
    }
}

