
package com.scalegate.scale_gate.Vehicle.Controller;

import com.scalegate.scale_gate.Vehicle.DTO.VehicleRegistrationRequest;
import com.scalegate.scale_gate.Vehicle.DTO.VehicleRegistrationResponse;
import com.scalegate.scale_gate.Vehicle.Service.VehicleService;
import com.scalegate.scale_gate.Vehicle.Vehicle;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/vehicles")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;

    @PostMapping
    public ResponseEntity<VehicleRegistrationResponse> registerVehicle(
            @Valid @RequestBody VehicleRegistrationRequest request) {

        Vehicle vehicle = vehicleService.registerVehicle(
                request.getVehicleNumber(),
                request.getVehicleType()
        );

        VehicleRegistrationResponse response =
                VehicleRegistrationResponse.builder()
                        .message("Vehicle registered successfully")
                        .vehicleType(vehicle.getVehicleType())
                        .createdAt(vehicle.getCreatedAt())
                        .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}

