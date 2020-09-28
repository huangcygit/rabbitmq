package com.hy.rabbit.confirm;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 *普通确认，生产者没发送一条消息后就调用waitForCOnfirms方法等待Broker确认消息，本质上是串行执行方式 吞吐量低
 */
public class NormalConfirmProducer {
    private static final String QUEUE_NAME = "ORIGIN_QUEUE";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUri("amqp://guest:guest@127.0.0.1:5672");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);

        //开启发送方确认方式
        channel.confirmSelect();
        String msg = "Hello world, Rabbit MQ ,Batch Confirm";
        channel.basicPublish("",QUEUE_NAME,null,msg.getBytes());

        // 普通Confirm，发送一条，确认一条
        if (channel.waitForConfirms()){
            System.out.println("消息发送成功");
        }

        channel.close();
        connection.close();
    }
}
