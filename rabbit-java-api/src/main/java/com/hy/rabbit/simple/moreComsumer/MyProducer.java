package com.hy.rabbit.simple.moreComsumer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @Author: qingshan
 * @Date: 2018/9/21 10:52
 * @Description: 咕泡学院，只为更好的你
 * 消息生产者
 */
public class MyProducer {
    private final static String EXCHANGE_NAME = "SIMPLE_EXCHANGE_M2";
    private final static String QUEUE_NAME = "SIMPLE_QUEUE_M1";

    private final static String QUEUE_NAME_2 = "SIMPLE_QUEUE_M2";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        // 连接IP
        factory.setHost("127.0.0.1");
        // 连接端口
        factory.setPort(5672);
        // 虚拟机
        factory.setVirtualHost("/");
        // 用户
        factory.setUsername("guest");
        factory.setPassword("guest");

        // 建立连接
        Connection conn = factory.newConnection();
        // 创建消息通道
        Channel channel = conn.createChannel();

        // 发送消息
        String msg = "Hello world, Rabbit MQ";

        // 声明交换机
        // String exchange, String type, boolean durable, boolean autoDelete, Map<String, Object> arguments
        channel.exchangeDeclare(EXCHANGE_NAME,"topic",false, false, null);

        // 声明队列
        // String queue, boolean durable, boolean exclusive, boolean autoDelete, Map<String, Object> arguments
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        System.out.println(" Waiting for message....");

        channel.queueDeclare(QUEUE_NAME_2, false, false, false, null);

        // 绑定队列和交换机
        channel.queueBind(QUEUE_NAME,EXCHANGE_NAME,"gupao.#");

        // 绑定队列和交换机
        channel.queueBind(QUEUE_NAME_2,EXCHANGE_NAME,"gupao.#");

        //开启发送方确认方式
        channel.confirmSelect();

        // String exchange, String routingKey, BasicProperties props, byte[] body
        for (int i = 0; i < 10; i++){
            channel.basicPublish(EXCHANGE_NAME, "gupao.test", null, (msg + "_" + i).getBytes());
        }

        // 普通Confirm，发送一条，确认一条
        if (channel.waitForConfirms()){
            System.out.println("消息发送成功");
        }


        channel.close();
        conn.close();
    }
}

