package com.hy.rabbit.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitmqConsumerConfig {

    @Value("${rabbitmq.exchange}")
    private String exchange;
    @Value("${rabbitmq.routing}")
    private String routing;
    @Value("${rabbitmq.queue}")
    private String queue;

    @Value("${rabbitmq.dlx.exchange}")
    private String dlxExchange;
    @Value("${rabbitmq.dlx.queue}")
    private String dlxQueue;


    @Bean
    public TopicExchange topicExchange(){
        return new TopicExchange(exchange,true,false,null);
    }

    /**
     * 死信交换机
     * @return
     */
//    @Bean
    public TopicExchange deadExchange(){
        return new TopicExchange(dlxExchange,true,false,null);
    }

    @Bean
    public Queue queue(){
//        Map<String, Object> map = new HashMap<>();
//        map.put("x-message-ttl",10000);
//        map.put("x-dead-letter-exchange",dlxExchange);
        return new Queue(queue,true,false,false,null);
    }

    /**
     * 死信队列
     * @return
     */
//    @Bean
    public Queue deadQueue(){
        return new Queue(dlxQueue,true,false,false,null);
    }

    @Bean
    public Binding binding(){
        return BindingBuilder.bind(queue()).to(topicExchange()).with(routing);
    }

//    @Bean
    public Binding bindingDead(){
        return BindingBuilder.bind(deadQueue()).to(deadExchange()).with("#");
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
//        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        factory.setAutoStartup(true);
        return factory;
    }
}
