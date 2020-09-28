package com.hy.rabbit.consumer;


import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = "${rabbitmq.queue}")
public class FirstConsumer {

    @RabbitHandler
    public void process(String msg){
        System.out.println("get message " + msg);
    }
}
