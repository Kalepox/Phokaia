package com.phokaia.transaction.kafka;

import com.phokaia.events.KafkaTopics;
import com.phokaia.events.TransactionCompleted;
import com.phokaia.events.TransactionFailed;
import com.phokaia.transaction.services.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Receives transfer results from account-service and updates the transaction status.
 */
@Component
public class TransactionResultListener {

    private static final Logger log = LoggerFactory.getLogger(TransactionResultListener.class);

    private final TransactionService transactionService;

    public TransactionResultListener(TransactionService transactionService){
        this.transactionService = transactionService;
    }

    @KafkaListener(topics = KafkaTopics.TRANSACTION_COMPLETED)
    public void onCompleted(TransactionCompleted event) {
        log.info("Received TransactionCompleted {}", event.transactionId());
        transactionService.completeTransaction(event);
    }

    @KafkaListener(topics = KafkaTopics.TRANSACTION_FAILED)
    public void onFailed(TransactionFailed event) {
        log.info("Received TransactionFailed {}: {}", event.transactionId(), event.reason());
        transactionService.failTransaction(event);
    }
}
