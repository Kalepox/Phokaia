package com.phokaia.account.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.phokaia.events.KafkaTopics;
import com.phokaia.events.TransactionCompleted;
import com.phokaia.events.TransactionFailed;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class AccountEventProducer {

    private static final Logger log  = LoggerFactory.getLogger(AccountEventProducer.class);
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public AccountEventProducer(KafkaTemplate<String, Object> kafkaTemplate){
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishCompleted(TransactionCompleted event){
        kafkaTemplate.send(KafkaTopics.TRANSACTION_COMPLETED, event.transactionId(), event)
                .whenComplete((result, ex)->{
                    if(ex != null){
                        log.error("Failed to publish TransactionCompleted {}", event.transactionId(), ex);
                    }
                    else{
                        log.info("Published TransactionCompleted {} to partition {}",
                            event.transactionId(), result.getRecordMetadata().partition());
                    }
                }
            );
    }
    
    public void publishFailed(TransactionFailed event){
        kafkaTemplate.send(KafkaTopics.TRANSACTION_FAILED, event.transactionId(), event)
                .whenComplete((result, ex)->{
                    if(ex != null){
                        log.error("Failed to publish TransactionFailed{}", event.transactionId(), ex);
                    }
                    else{
                        log.info("Published TransactionFailed {} to partition {}",
                            event.transactionId(), result.getRecordMetadata().partition());
                    }
                }
            );
    }
}
