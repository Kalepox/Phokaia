package com.phokaia.transaction.kafka;

import org.slf4j.LoggerFactory;
import com.phokaia.events.KafkaTopics;
import com.phokaia.events.TransactionRequested;
import com.phokaia.transaction.entities.Transaction;
import org.slf4j.Logger;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class TransactionEventProducer {

    private static final Logger log = LoggerFactory.getLogger(TransactionEventProducer.class);
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public TransactionEventProducer(KafkaTemplate<String, Object> kafkaTemplate){
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishRequested(Transaction transaction){
        TransactionRequested event = new TransactionRequested(
            transaction.getTransactionId(),
            transaction.getFromAccountCode(),
            transaction.getToAccountCode(),
            transaction.getAmount(),
            transaction.getCreatedAt()
        );

        kafkaTemplate.send(KafkaTopics.TRANSACTION_REQUESTED, event.transactionId(), event)
                .whenComplete((result, ex)->{
                    if(ex != null){
                        log.error("Failed to publish TransactionRequested {}", event.transactionId(), ex);
                    }
                    else{
                        log.info("Published TransactionRequested {} to partition {}",
                            event.transactionId(), result.getRecordMetadata().partition());
                    }
                }
            );
    }


}
