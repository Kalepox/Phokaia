package com.phokaia.transaction.entities;

import java.math.BigDecimal;
import java.time.Instant;

public class Transaction {

    String transactionId;
    String fromAccountCode;
    String toAccountCode;
    BigDecimal amount;
    TransactionStatus status;
    String failureReason;
    Instant createdAt;
    Instant updatedAt;
    Long version;

    protected Transaction() { } 

    public Transaction(String fromAccountCode, String toAccountCode, BigDecimal amount){
        this.transactionId = transactionId;
        this.fromAccountCode = fromAccountCode;
        this.toAccountCode = toAccountCode;
        this.amount = amount;
        this.status = status;
        this.failureReason= failureReason;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.version = version;
    }

    public String getTransactionId(){
        return transactionId;
    }

    public String getFromAccountCode(){
        return fromAccountCode;
    }

    public String getToAccountCode(){
        return toAccountCode;
    }

}
