package com.scalegate.scale_gate.TollTransaction.Repository;

import com.scalegate.scale_gate.TollTransaction.TollTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TollTransactionRepository
        extends JpaRepository<TollTransaction, Long> {

    List<TollTransaction> findAllByVehicle_VehicleId(Long vehicleId);
}
