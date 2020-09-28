package com.hy.kafka;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.io.IOException;
import java.util.Properties;

public class ProducerDemo {

    public static final String brokerList = "192.168.13.128:9092,192.168.13.129:9092,192.168.13.130:9092";
    public static final String topic = "t-k9";

    public static Properties initConfig(){
        Properties prop = new Properties();
        prop.put("bootstrap.servers",brokerList);
        prop.put("key.serializer","org.apache.kafka.common.serialization.StringSerializer");
        prop.put("value.serializer","org.apache.kafka.common.serialization.StringSerializer");
//        prop.put(ProducerConfig.BUFFER_MEMORY_CONFIG,"10");
//        prop.put(ProducerConfig.MAX_BLOCK_MS_CONFIG,"10");
        return prop;
    }

    public static void main(String[] args) throws IOException {
        Properties props = initConfig();
        try {
            KafkaProducer<String,String> producer = new KafkaProducer<String, String>(props);

//            byte[] bytes = new byte[1024];
//            String message = new String(bytes);
            int i = 0;
            while (i < 10){
                String message = "kafka_back_200_" + i;
                final ProducerRecord<String,String> record = new ProducerRecord<>(topic,message);
                //异步方式，异步方式获取发送结果，不阻塞主流程
                producer.send(record, new Callback() {
                    @Override
                    public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                        if (e == null){
                            System.out.println(recordMetadata.partition() + "-->" + record.value());
                        }else {
                            e.printStackTrace();
                        }
                    }
                });
                Thread.sleep(500);
                i++;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
//        System.in.read();
    }

}
