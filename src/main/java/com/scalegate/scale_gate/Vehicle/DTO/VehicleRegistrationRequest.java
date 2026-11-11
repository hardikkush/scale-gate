package com.scalegate.scale_gate.Vehicle.DTO;

import com.scalegate.scale_gate.Vehicle.VehicleType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VehicleRegistrationRequest {

    @NotBlank
    private String vehicleNumber;

    @NotNull
    private VehicleType vehicleType;

}
