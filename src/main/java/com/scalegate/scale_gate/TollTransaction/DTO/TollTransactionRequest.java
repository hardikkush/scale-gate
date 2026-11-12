package com.scalegate.scale_gate.TollTransaction.DTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TollTransactionRequest {

    @NotNull
    @Positive
    private Long vehicleId;
}
