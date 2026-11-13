package com.phokaia.account.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

import com.phokaia.events.KafkaTopics;

@Configuration 
public class KafkaTopicConifg {

    
    @Bean 
    public NewTopic transactionCompletedTopic(){
        return TopicBuilder.name(KafkaTopics.TRANSACTION_COMPLETED)
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean 
    public NewTopic transactionFailedTopic(){
        return TopicBuilder.name(KafkaTopics.TRANSACTION_FAILED)
                .partitions(3)
                .replicas(1)
                .build();
    }


}
