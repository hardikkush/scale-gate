package com.scalegate.scale_gate.Vehicle.Service;

import com.scalegate.scale_gate.Exception.DuplicateVehicleException;
import com.scalegate.scale_gate.Vehicle.Repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.scalegate.scale_gate.Vehicle.Vehicle;
import com.scalegate.scale_gate.Vehicle.VehicleType;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final VehicleNumberHashService vehicleNumberHashService;

    @Transactional
    public Vehicle registerVehicle(String vehicleNumber, VehicleType vehicleType) {

        if (vehicleType == null) {
            throw new IllegalArgumentException("Vehicle type cannot be null");
        }

        String vehicleNumberHash =
                vehicleNumberHashService.generateVehicleNumberHash(vehicleNumber);

        if (vehicleRepository.existsByVehicleNumberHash(vehicleNumberHash)) {
            throw new DuplicateVehicleException("Vehicle is already registered");
        }

        Vehicle vehicle = Vehicle.builder()
                .vehicleNumberHash(vehicleNumberHash)
                .vehicleType(vehicleType)
                .build();

        return vehicleRepository.save(vehicle);
    }
}
