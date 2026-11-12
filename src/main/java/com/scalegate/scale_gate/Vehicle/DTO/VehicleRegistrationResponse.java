package com.scalegate.scale_gate.Vehicle.DTO;

import com.scalegate.scale_gate.Vehicle.VehicleType;
import lombok.Builder;
import lombok.Getter;

import java.time.OffsetDateTime;

@Getter
@Builder
public class VehicleRegistrationResponse {

    private String message;
    private Long vehicleId;
    private VehicleType vehicleType;
    private OffsetDateTime createdAt;


}
