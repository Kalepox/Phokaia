package com.phokaia.notification.services;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.*;
import com.phokaia.events.TransactionCompleted;
import com.phokaia.events.TransactionFailed;
import com.phokaia.notification.entities.*;
import com.phokaia.notification.repositories.NotificationRepository;
import java.util.List;
import jakarta.transaction.TransactionScoped;

import org.springframework.transaction.annotation.Transactional; 

@Service
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository){
        this.notificationRepository = notificationRepository;
    }

    @Transactional 
    public void handleCompleted(TransactionCompleted event){
        if(notificationRepository.existsByTransactionId(event.transactionId())){
            log.info("Notifications for {} already exist, ignoring duplicate", event.transactionId());
            return;
        }

        String amount = event.amount().toPlainString();

        Notification sent = new Notification(
            event.fromAccountCode(),
            event.transactionId(),
            NotificationType.TRANSFER_SENT,
            "You sent " + amount + " to " + event.toAccountCode());

        Notification recieved = new Notification(
            event.toAccountCode(),
            event.transactionId(),
            NotificationType.TRANSFER_RECEIVED,
            "You recieved" + amount + " to " + event.fromAccountCode());

        notificationRepository.saveAll(List.of(sent, recieved));

        log.info("Created SENT/RECIEVED notifications for {}", event.transactionId());
    }

    @Transactional 
    public void handleFailed(TransactionFailed event){
        if(notificationRepository.existsByTransactionId(event.transactionId())){
            log.info("Notifications for {} already exist, ignoring duplicate", event.transactionId());
            return;
        }

        String amount = event.amount().toPlainString();

        Notification failed = new Notification(
            event.fromAccountCode(),
            event.transactionId(),
            NotificationType.TRANSFER_FAILED,
            "Transaction with amount of" + amount + " has been failed due to " + event.reason()
        );

        notificationRepository.save(failed);

        log.info("Created FAILED notifications for {}", event.transactionId());
    }

}
