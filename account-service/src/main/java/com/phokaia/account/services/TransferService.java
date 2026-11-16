package com.phokaia.account.services;

import com.phokaia.account.entities.Account;
import com.phokaia.account.entities.ProcessedTransaction;
import com.phokaia.account.exceptions.*;
import com.phokaia.account.repositories.AccountRepository;
import com.phokaia.account.repositories.ProcessedTransactionRepository;
import com.phokaia.events.TransactionCompleted;
import com.phokaia.events.TransactionFailed;
import com.phokaia.events.TransactionRequested;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.util.Optional;

@Service
public class TransferService {

    private final ProcessedTransactionRepository processedTransactionRepository;
    private final AccountRepository accountRepository;

    public TransferService(ProcessedTransactionRepository processedTransactionRepository, AccountRepository accountRepository){
        this.processedTransactionRepository = processedTransactionRepository;
        this.accountRepository = accountRepository;
    }

    @Transactional 
    public Optional<Object> processTransfer(TransactionRequested event){
        if(processedTransactionRepository.existsById(event.transactionId())){
            return Optional.empty();
        }

        processedTransactionRepository.save(new ProcessedTransaction(event.transactionId()));

        try{
            Account from = findAccount(event.fromAccountCode());
            Account to = findAccount(event.toAccountCode());

            from.makeTransfer(to, event.amount());

            return Optional.of(new TransactionCompleted(
                event.transactionId(), event.fromAccountCode(), event.toAccountCode(), event.amount(), Instant.now()));
            } 
        catch (AccountNotFoundException | InsufficientFundsException | CurrencyMismatchException | IllegalArgumentException ex ){
                return Optional.of(new TransactionFailed(
                    event.transactionId(), event.fromAccountCode(), event.toAccountCode(), event.amount(), ex.getMessage(), Instant.now()));
    }}

    private Account findAccount(String accountCode){
        return accountRepository.findById(accountCode)
        .orElseThrow(() -> new AccountNotFoundException(accountCode));
    }
        
    

}
