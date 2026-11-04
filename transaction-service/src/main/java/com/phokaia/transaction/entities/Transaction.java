package com.phokaia.transaction.entities;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import jakarta.persistence.*;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @Column(length = 36, updatable = false)
    private String transactionId;

    @Column(nullable = false, length = 20, updatable =false)
    private String fromAccountCode;

    @Column(nullable = false, length = 20, updatable =false)
    private String toAccountCode;

    @Column(nullable = false, precision = 19, scale = 2, updatable = false, length = 20)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TransactionStatus status;

    @Column(length = 500)
    private String failureReason;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;
    @Version
    private Long version;

    protected Transaction() { } 

    public Transaction(String fromAccountCode, String toAccountCode, BigDecimal amount){
        this.transactionId = UUID.randomUUID().toString();
        this.fromAccountCode = fromAccountCode;
        this.toAccountCode = toAccountCode;
        this.amount = amount;
        this.status = TransactionStatus.PENDING;
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
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

    public BigDecimal getAmount(){
        return amount;
    }

    public String getFailureReason(){
        return failureReason;
    }

    public Instant getCreatedAt(){
        return createdAt;
    }

    public Instant getUpdatedAt(){
        return updatedAt;
    }

    public void markCompleted(){
        if (status != TransactionStatus.PENDING) {
            throw new IllegalStateException("Transaction " + transactionId + " is already " + status);
        }
        status = TransactionStatus.COMPLETED;
        this.updatedAt = Instant.now();
    }

    public void markFailed(String reason){
        if (status != TransactionStatus.PENDING) {
            throw new IllegalStateException("Transaction " + transactionId + " is already " + status);
        }
        status = TransactionStatus.FAILED;
        this.failureReason = reason;
        this.updatedAt = Instant.now();
    }

    public TransactionStatus getStatus(){
        return status;
    }

}
