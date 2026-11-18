
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
import com.scalegate.scale_gate.TollTransaction.DTO.TollTransactionHistoryResponse;
import org.springframework.cache.annotation.Cacheable;
import com.scalegate.scale_gate.TollTransaction.Event.TollTransactionCreatedEvent;
import org.springframework.context.ApplicationEventPublisher;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TollTransactionService {

    private final VehicleRepository vehicleRepository;
    private final TollTransactionRepository tollTransactionRepository;
    private final ApplicationEventPublisher eventPublisher;

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

        TollTransaction savedTransaction =
                tollTransactionRepository.save(transaction);

        eventPublisher.publishEvent(
                new TollTransactionCreatedEvent(vehicleId)
        );

        return savedTransaction;
    }

    private BigDecimal calculateToll(VehicleType vehicleType) {

        return switch (vehicleType) {
            case LIGHT_VEHICLE -> BigDecimal.valueOf(250);
            case HEAVY_VEHICLE -> BigDecimal.valueOf(450);
            case TWO_WHEELER -> BigDecimal.valueOf(150);
        };
    }

    @Cacheable(value = "tollHistory", key = "#vehicleId")
    @Transactional(readOnly = true)
    public List<TollTransactionHistoryResponse> getTransactionsByVehicleId(Long vehicleId) {

        // First verify that the vehicle exists
        vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new VehicleNotFoundException(
                        "Vehicle not found with ID: " + vehicleId
                ));

        // Fetch all transactions belonging to this vehicle
        return tollTransactionRepository
                .findAllByVehicle_VehicleId(vehicleId)
                .stream()
                .map(transaction -> TollTransactionHistoryResponse.builder()
                        .transactionId(transaction.getTransactionId())
                        .vehicleId(transaction.getVehicle().getVehicleId())
                        .amount(transaction.getAmount())
                        .createdAt(transaction.getCreatedAt())
                        .build())
                .toList();
    }


}

