package com.hy.rabbit.simple.producer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Myproducer {

    private static final String EXCHANGE_NAME = "MY_FIRST_SIMPLE_EXCHANGE_1";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("127.0.0.1");
        factory.setPort(5672);
        factory.setVirtualHost("/");
        factory.setUsername("guest");
        factory.setPassword("guest");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        String msg = "first rabbitmq message";

        //发送到交换机，通过路由键发送到相应的队列
        channel.basicPublish(EXCHANGE_NAME,"hy.test",null,msg.getBytes());
        channel.close();
        connection.close();
    }
}
