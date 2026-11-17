
package com.scalegate.scale_gate.TollTransaction.Controller;

import com.scalegate.scale_gate.TollTransaction.DTO.TollTransactionHistoryResponse;
import com.scalegate.scale_gate.TollTransaction.DTO.TollTransactionRequest;
import com.scalegate.scale_gate.TollTransaction.DTO.TollTransactionResponse;
import com.scalegate.scale_gate.TollTransaction.Service.TollTransactionService;
import com.scalegate.scale_gate.TollTransaction.TollTransaction;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/toll-transactions")
@RequiredArgsConstructor
public class TollTransactionController {

    private final TollTransactionService tollTransactionService;

    @PostMapping
    public ResponseEntity<TollTransactionResponse> createTollTransaction(
            @Valid @RequestBody TollTransactionRequest request) {

        TollTransaction transaction =
                tollTransactionService.createTollTransaction(
                        request.getVehicleId()
                );

        TollTransactionResponse response =
                TollTransactionResponse.builder()
                        .message("Toll transaction successful")
                        .transactionId(transaction.getTransactionId())
                        .vehicleId(transaction.getVehicle().getVehicleId())
                        .amount(transaction.getAmount())
                        .createdAt(transaction.getCreatedAt())
                        .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/vehicle/{vehicleId}")
    public ResponseEntity<List<TollTransactionHistoryResponse>> getTransactionsByVehicleId(
            @PathVariable Long vehicleId) {

        List<TollTransactionHistoryResponse> response =
                tollTransactionService.getTransactionsByVehicleId(vehicleId);

        return ResponseEntity.ok(response);
    }

}
