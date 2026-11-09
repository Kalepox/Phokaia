package com.phokaia.transaction.repositories;

import com.phokaia.transaction.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, String> {

     List<Transaction> findByFromAccountCodeOrToAccountCodeOrderByCreatedAtDesc(String fromAccountCode, String toAccountCode);
}
