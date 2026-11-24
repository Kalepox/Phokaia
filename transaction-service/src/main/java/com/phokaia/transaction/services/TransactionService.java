package com.phokaia.transaction.services;

import com.phokaia.transaction.dtos.TransactionDTO;
import com.phokaia.transaction.repositories.*;
import com.phokaia.transaction.entities.*;
import com.phokaia.events.*;
import org.springframework.transaction.annotation.Transactional;
import com.phokaia.transaction.exceptions.*;
import java.util.List;
import org.springframework.stereotype.Service;
import com.phokaia.transaction.kafka.TransactionEventProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionEventProducer eventProducer;
    private static final Logger log = LoggerFactory.getLogger(TransactionService.class);

    public TransactionService(TransactionRepository transactionRepository, TransactionEventProducer eventProducer){
        this.transactionRepository = transactionRepository;
        this.eventProducer = eventProducer;
    }


    @Transactional 
    public TransactionDTO.Response requestTransaction(TransactionDTO.TransferRequest request){
        if(request.fromAccountCode().equals(request.toAccountCode())){
            throw new IllegalArgumentException("Cannot Transfer to the same account");
        }

        Transaction transaction = new Transaction(request.fromAccountCode(), request.toAccountCode(), request.amount());
        Transaction saved = transactionRepository.save(transaction);
        eventProducer.publishRequested(saved);
        return TransactionDTO.Response.from(saved);
    }

    @Transactional(readOnly = true)
    public TransactionDTO.Response getTransaction(String transactionId){
        return TransactionDTO.Response.from(findTransaction(transactionId));
    }

    @Transactional(readOnly = true)
    public List<TransactionDTO.Response> getTransactionsOfAccount(String accountCode){
    
        List<Transaction> transactions = transactionRepository.findByFromAccountCodeOrToAccountCodeOrderByCreatedAtDesc(accountCode, accountCode);
         return transactions.stream().map(TransactionDTO.Response :: from).toList();
    }

    @Transactional 
    public void completeTransaction(TransactionCompleted event){
        Transaction transaction = findTransaction(event.transactionId());
        if (transaction.getStatus() != TransactionStatus.PENDING){
            log.info("Transaction {} already {}, ignoring duplicate TransactionCompleted",
            transaction.getTransactionId(), transaction.getStatus());
            return;
        }

    transaction.markCompleted();
    }

    @Transactional 
    public void failTransaction(TransactionFailed event){
        Transaction transaction = findTransaction(event.transactionId());
        if (transaction.getStatus() != TransactionStatus.PENDING){
            log.info("Transaction {} already {}, ignoring duplicate TransactionFailed",
            transaction.getTransactionId(), transaction.getStatus());
            return;
        }

        transaction.markFailed(event.reason());
    }

    private Transaction findTransaction(String transactionId){
        return transactionRepository.findById(transactionId)
            .orElseThrow(()-> new TransactionNotFoundException(transactionId));
    }

}
