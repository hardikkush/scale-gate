package com.scalegate.scale_gate.TollTransaction.Repository;

import com.scalegate.scale_gate.TollTransaction.TollTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TollTransactionRepository
        extends JpaRepository<TollTransaction, Long> {
}
