
package com.scalegate.scale_gate.TollTransaction.DTO;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Builder
public class TollTransactionResponse {

    private String message;
    private Long transactionId;
    private Long vehicleId;
    private BigDecimal amount;
    private OffsetDateTime createdAt;
}
