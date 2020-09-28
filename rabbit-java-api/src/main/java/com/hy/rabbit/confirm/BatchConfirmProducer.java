package com.hy.rabbit.confirm;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * 批量确认，没发送完一批消息后，再调用waitForConfirmsOrDie方法等待Broker的确认消息
 * 吞吐量高，但是批量确认，如果有失败，难以确认哪条消息失败
 */
public class BatchConfirmProducer {
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

        try {
            for (int i = 0; i < 5; i++){
                channel.basicPublish("",QUEUE_NAME,null, (msg + "_" + i).getBytes());
            }

            channel.waitForConfirmsOrDie();
            System.out.println("消息发送完毕，批量确认成功");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
