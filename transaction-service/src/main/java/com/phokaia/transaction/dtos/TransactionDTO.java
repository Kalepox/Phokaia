package com.phokaia.transaction.dtos;

import com.phokaia.transaction.entities.Transaction;
import com.phokaia.transaction.entities.TransactionStatus;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.Instant;

public final class TransactionDTO {

    // Container only: nobody should create a TransactionDTO object itself
    private TransactionDTO(){}

    /** JSON in: POST /transactions/transfer
     *  { "fromAccountCode": "TRYCUS04821937", "toAccountCode": "TRYCUS11112222", "amount": 50.00 } */
    public record TransferRequest(
        @NotBlank @Size(max = 20)
        String fromAccountCode,
        @NotBlank @Size(max = 20)
        String toAccountCode,
        @NotNull @Positive @Digits(integer = 17, fraction = 2)
        BigDecimal amount
    ){}

    /** JSON out: the transfer and its current status */
    public record Response(
        String transactionId,
        String fromAccountCode,
        String toAccountCode,
        BigDecimal amount,
        TransactionStatus status,
        String failureReason,
        Instant createdAt,
        Instant updatedAt
    ){
        public static Response from(Transaction transaction){
            return new Response(
                transaction.getTransactionId(),
                transaction.getFromAccountCode(),
                transaction.getToAccountCode(),
                transaction.getAmount(),
                transaction.getStatus(),
                transaction.getFailureReason(),
                transaction.getCreatedAt(),
                transaction.getUpdatedAt()
            );
        }
    }
}
