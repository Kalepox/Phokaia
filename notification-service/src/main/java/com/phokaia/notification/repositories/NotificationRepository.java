package com.phokaia.notification.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.phokaia.notification.entities.*;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long>{

    List<Notification> findByAccountCodeOrderByCreatedAtDesc(String accountCode);

    boolean existsByTransactionId(String transactionId);

}
