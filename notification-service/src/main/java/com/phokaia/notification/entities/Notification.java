package com.phokaia.notification.entities;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "notifications",
    uniqueConstraints = @UniqueConstraint(columnNames = {"transaction_id", "account_code", "type"})
)
public class Notification {



    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String accountCode;

    @Column(nullable = false, length = 36)
    private String transactionId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length =20)
    private NotificationType type;

    @Column(nullable = false, length = 500)
    private String message;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    protected Notification(){}

    public Notification(String accountCode, String transactionId, NotificationType type, String message){
        this.accountCode = accountCode;
        this.transactionId = transactionId;
        this.type = type;
        this.message = message;
        this.createdAt = Instant.now();
    }

    public Long getNotificationId(){
        return id;
    }

    public String getAccountCode(){
        return accountCode;
    }

    public String getTransactionId(){
        return transactionId;
    }

    public NotificationType getNotificationType(){
        return type;
    }

    public String getMessage(){
        return message;
    }

    public Instant getCreatedAt(){
        return createdAt;
    }

}
