package com.scalegate.scale_gate.TollTransaction.DTO;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Builder
public class TollTransactionHistoryResponse implements Serializable {

    private Long transactionId;
    private Long vehicleId;
    private BigDecimal amount;
    private OffsetDateTime createdAt;
}

