package com.phokaia.transaction.repositories;

import com.phokaia.transaction.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, String> {

}
