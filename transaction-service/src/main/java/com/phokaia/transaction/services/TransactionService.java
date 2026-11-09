package com.phokaia.transaction.services;

import com.phokaia.transaction.dtos.TransactionDTO;
import com.phokaia.transaction.repositories.*;
import com.phokaia.transaction.entities.*;
import org.springframework.transaction.annotation.Transactional;
import com.phokaia.transaction.exceptions.*;
import java.util.List;
import org.springframework.stereotype.Service;
import com.phokaia.transaction.kafka.TransactionEventProducer;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionEventProducer eventProducer;

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

        Transaction transaction = transactionRepository.findById(transactionId)
            .orElseThrow(()-> new TransactionNotFoundException(transactionId));
        
        return TransactionDTO.Response.from(transaction);
    }

    @Transactional(readOnly = true)
    public List<TransactionDTO.Response> getTransactionsOfAccount(String accountCode){
    
        List<Transaction> transactions = transactionRepository.findByFromAccountCodeOrToAccountCodeOrderByCreatedAtDesc(accountCode, accountCode);
         return transactions.stream().map(TransactionDTO.Response :: from).toList();
    }

}
