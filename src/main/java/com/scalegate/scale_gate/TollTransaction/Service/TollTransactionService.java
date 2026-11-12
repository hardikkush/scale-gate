
package com.scalegate.scale_gate.TollTransaction.Service;

import com.scalegate.scale_gate.Exception.VehicleNotFoundException;
import com.scalegate.scale_gate.TollTransaction.Repository.TollTransactionRepository;
import com.scalegate.scale_gate.TollTransaction.TollTransaction;
import com.scalegate.scale_gate.Vehicle.Repository.VehicleRepository;
import com.scalegate.scale_gate.Vehicle.Vehicle;
import com.scalegate.scale_gate.Vehicle.VehicleType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class TollTransactionService {

    private final VehicleRepository vehicleRepository;
    private final TollTransactionRepository tollTransactionRepository;

    @Transactional
    public TollTransaction createTollTransaction(Long vehicleId) {

        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() ->
                        new VehicleNotFoundException("Vehicle not found"));

        BigDecimal amount = calculateToll(vehicle.getVehicleType());

        TollTransaction transaction = TollTransaction.builder()
                .vehicle(vehicle)
                .amount(amount)
                .build();

        return tollTransactionRepository.save(transaction);
    }

    private BigDecimal calculateToll(VehicleType vehicleType) {

        return switch (vehicleType) {
            case LIGHT_VEHICLE -> BigDecimal.valueOf(250);
            case HEAVY_VEHICLE -> BigDecimal.valueOf(450);
            case TWO_WHEELER -> BigDecimal.valueOf(150);
        };
    }
}

