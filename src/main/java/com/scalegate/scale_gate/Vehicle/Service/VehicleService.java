
package com.scalegate.scale_gate.Vehicle.Service;

import com.scalegate.scale_gate.Exception.DuplicateVehicleException;
import com.scalegate.scale_gate.Exception.VehicleNotFoundException;
import com.scalegate.scale_gate.Vehicle.DTO.VehicleRegistrationResponse;
import com.scalegate.scale_gate.Vehicle.Repository.VehicleRepository;
import com.scalegate.scale_gate.Vehicle.Vehicle;
import com.scalegate.scale_gate.Vehicle.VehicleType;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
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

    @Cacheable(value = "vehicles", key = "#vehicleId")
    @Transactional(readOnly = true)
    public VehicleRegistrationResponse getVehicleById(Long vehicleId) {

        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() ->
                        new VehicleNotFoundException("Vehicle not found"));

        return VehicleRegistrationResponse.builder()
                .message("Vehicle found successfully")
                .vehicleId(vehicle.getVehicleId())
                .vehicleType(vehicle.getVehicleType())
                .createdAt(vehicle.getCreatedAt())
                .build();
    }
}
