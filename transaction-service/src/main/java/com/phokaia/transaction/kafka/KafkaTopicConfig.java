package com.phokaia.transaction.kafka;

import org.springframework.context.annotation.*;
import com.phokaia.events.KafkaTopics;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.kafka.config.TopicBuilder;


@Configuration
public class KafkaTopicConfig {

    @Bean 
    public NewTopic transactionRequestedTopic(){
        return TopicBuilder.name(KafkaTopics.TRANSACTION_REQUESTED)
                .partitions(3)
                .replicas(1)
                .build();
    }

}
