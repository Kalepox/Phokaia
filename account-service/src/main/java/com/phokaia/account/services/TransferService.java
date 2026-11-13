package com.phokaia.account.services;

import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
import com.phokaia.account.repositories.ProcessedTransactionRepository;
import com.phokaia.events.TransactionRequested;
import com.phokaia.events.TransactionCompleted;
import com.phokaia.events.TransactionFailed;
import com.phokaia.account.entities.Account;
import com.phokaia.account.entities.ProcessedTransaction;
import com.phokaia.account.exceptions.*;
import java.time.Instant;

@Service
public class TransferService {

    private final ProcessedTransactionRepository processedTransactionRepository;

    public TransferService(ProcessedTransactionRepository processedTransactionRepository){
        this.processedTransactionRepository = processedTransactionRepository;
    }

    @Transactional 
    public Optional<Object> processTransfer(TransactionRequested event){
        if(processedTransactionRepository.existsById(event.transactionId())){
            return Optional.empty();
        }

        processedTransactionRepository.save(new ProcessedTransaction(event.transactionId()));

        try{
            
        }
    }

}
