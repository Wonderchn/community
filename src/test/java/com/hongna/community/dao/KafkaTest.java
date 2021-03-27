package com.hongna.community.dao;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@SpringBootTest
public class KafkaTest {
    @Autowired
    private KafkaProducer kafkaProducer;
    @Test
    public void testKafka() throws InterruptedException {
        kafkaProducer.sendMessage("test","你好");
        kafkaProducer.sendMessage("test","在吗");
        Thread.sleep(20000);


    }
}
@Component
class KafkaProducer{
    @Autowired
    private KafkaTemplate kafkaTemplate;

    public void sendMessage(String topics, String content){
        kafkaTemplate.send(topics, content);
    }
}
@Component
class KafkaConsumer{
    @KafkaListener(topics={"test"}) //关注需要监听的主题，处理监听到消息的方法
    public void handleMessage(ConsumerRecord record){
        System.out.println(record.value());
    }
}
