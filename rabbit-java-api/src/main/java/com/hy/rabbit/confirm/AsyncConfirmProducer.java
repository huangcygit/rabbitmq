package com.hy.rabbit.confirm;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * 异步确认:通过调用 addConfinnListener 方法注册回调，在 Broker 确认了 一条或多条消息之后由客户端回调该方法 。
 * 吞吐量高，可以定位到失败消息
 */
public class AsyncConfirmProducer {
    private final static String QUEUE_NAME = "ORIGIN_QUEUE";

    public static void main(String[] args) throws Exception{
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUri("amqp://guest:guest@127.0.0.1:5672");

        Connection connection = factory.newConnection();

        Channel channel = connection.createChannel();
        String msg = "Hello world, Rabbit MQ, Async Confirm";

        channel.queueDeclare(QUEUE_NAME,false,false,false,null);

        // 用来维护未确认消息的deliveryTag
        final SortedSet<Long>confirmSet = Collections.synchronizedSortedSet(new TreeSet<Long>());

        channel.addConfirmListener(new ConfirmListener() {
            @Override
            public void handleAck(long deliveryTag, boolean multiple) throws IOException {
                System.out.println("Broker未确认消息，标识：" + deliveryTag);
                if (multiple){
                    // headSet表示后面参数之前的所有元素，全部删除
                    confirmSet.headSet(deliveryTag + 1).clear();
                }else {
                    confirmSet.remove(deliveryTag);
                }
            }

            @Override
            public void handleNack(long deliveryTag, boolean multiple) throws IOException {
                if (multiple){
                    // headSet表示后面参数之前的所有元素，全部删除
                    confirmSet.headSet(deliveryTag + 1).clear();
                }else {
                    confirmSet.remove(deliveryTag);
                }
            }

        });

        channel.confirmSelect();
        for (int i = 0; i < 10; i++){
            long nextSeqNo = channel.getNextPublishSeqNo();
            channel.basicPublish("",QUEUE_NAME,null, (msg + "_" + i).getBytes());
            confirmSet.add(nextSeqNo);
        }
    }
}
