package com.hy.rabbit.producer;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Producer {

    @Value("${rabbitmq.exchange}")
    private String exchange;
    @Value("${rabbitmq.routing}")
    private String routing;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void send(){
        for (int i = 0; i < 10; i++){
            rabbitTemplate.convertAndSend(exchange,routing, "boot message" + i);
        }
    }
}
