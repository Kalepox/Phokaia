package com.phokaia.account.kafka;

import org.springframework.stereotype.*;
import org.springframework.kafka.annotation.KafkaListener;
import com.phokaia.account.services.TransferService;
import com.phokaia.events.KafkaTopics;
import com.phokaia.events.TransactionCompleted;
import com.phokaia.events.TransactionFailed;
import com.phokaia.events.TransactionRequested;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class TransactionRequestedListener {

    private static final Logger log = LoggerFactory.getLogger(TransactionRequestedListener.class);

    private final TransferService transferService;
    private final AccountEventProducer accountEventProducer;

    public TransactionRequestedListener(TransferService transferService, AccountEventProducer accountEventProducer){
        this.transferService = transferService;
        this.accountEventProducer = accountEventProducer;
    }

    @KafkaListener(topics =  KafkaTopics.TRANSACTION_REQUESTED)
    public void onTransactionRequested(TransactionRequested event){
        log.info("Received TransactionRequested {}", event.transactionId());
    

        transferService.processTransfer(event).ifPresentOrElse(result ->{
            if(result instanceof TransactionCompleted completed){
                accountEventProducer.publishCompleted(completed);
            }
            else if(result instanceof TransactionFailed failed){
                accountEventProducer.publishFailed(failed);
            }
        } , () -> log.info("Skipped duplicate TransactionRequested {}",event.transactionId()));
        
    }
}
