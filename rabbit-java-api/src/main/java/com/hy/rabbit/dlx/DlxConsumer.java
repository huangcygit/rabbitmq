package com.hy.rabbit.dlx;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DlxConsumer {
    public static void main(String[] args) throws Exception{
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUri("amqp://guest:guest@127.0.0.1:5672");

        Connection connection = factory.newConnection();

        Channel channel = connection.createChannel();

        //指定队列的死信交换机
        Map<String,Object>arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange","DLX_EXCHANGE");
        // arguments.put("x-expires","9000"); // 设置队列的TTL
        // arguments.put("x-max-length", 4); // 如果设置了队列的最大长度，超过长度时，先入队的消息会被发送到DLX

        //申明队列
        channel.queueDeclare("TEST_DLX_QUEUE",false,false,false,arguments);

        //申明死信交换机
        channel.exchangeDeclare("DLX_EXCHANGE","topic",false,false,false,null);
        //什么死信队列
        channel.queueDeclare("DLX_QUEUE",false,false,false,null);

        //绑定死信队列和死信交换机
        channel.queueBind("DLX_QUEUE","DLX_EXCHANGE","#");

        //创建消费者
        Consumer consumer = new DefaultConsumer(channel){

            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String msg = new String(body, "UTF-8");
                System.out.println("Received message : '" + msg + "'");
            }
        };
    }
}
