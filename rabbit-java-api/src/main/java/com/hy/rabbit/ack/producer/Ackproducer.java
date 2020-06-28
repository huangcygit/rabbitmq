package com.hy.rabbit.ack.producer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Ackproducer {
    private static final String QUEUE_NAME = "ACK_QUEUE_TEST";

    public static void main(String[] args) throws Exception{
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUri("amqp://guest:guest@127.0.0.1:5672");

        Connection connection = factory.newConnection();

        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME,false,false,false,null);

        String msg = "test ack message ";

        //直接发送到队列
        channel.basicPublish("", QUEUE_NAME, null, (msg).getBytes());

        channel.basicPublish("", QUEUE_NAME, null, "message拒绝".getBytes());

        channel.basicPublish("", QUEUE_NAME, null, "message异常".getBytes());

        channel.close();
        connection.close();
    }
}
