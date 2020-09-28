package com.hy.kafka.config;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Consumer {

    @KafkaListener(topics = "t-k9")
    public void listen(List<ConsumerRecord<?,?>> records, Acknowledgment ack){
        for (ConsumerRecord<?,?> record : records){
            System.out.println("partition:" + record.partition() + ",value" + record.value());
            ack.acknowledge();
        }
    }
}
