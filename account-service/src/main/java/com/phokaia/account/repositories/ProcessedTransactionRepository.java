package com.phokaia.account.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.phokaia.account.entities.ProcessedTransaction;

public interface ProcessedTransactionRepository extends JpaRepository<ProcessedTransaction, String> {

}
