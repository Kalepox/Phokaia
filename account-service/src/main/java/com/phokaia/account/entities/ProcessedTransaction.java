package com.phokaia.account.entities;

import jakarta.persistence.*;
import java.time.Instant;


@Entity
@Table(name = "processed_transactions")
public class ProcessedTransaction {

    @Id 
    @Column(length = 36)
    private String transactionId;

    @Column(nullable = false)
    private Instant processedAt;

    ProcessedTransaction(){}

    public ProcessedTransaction(String transactionId){
        this.transactionId = transactionId;
        this.processedAt = Instant.now();
    }

    public String getTransactionId(){
        return transactionId;
    }

    public Instant getProcessedAt(){
        return processedAt;
    }

}
