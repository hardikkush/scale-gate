package com.scalegate.scale_gate.Vehicle.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.HexFormat;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import java.util.Locale;

@Service
public class VehicleNumberHashService {

    @Value("${scalegate.security.vehicle-hmac-secret}")
    private String secretKey;

    public String normalizeVehicleNumber(String vehicleNumber) {

        if(vehicleNumber == null || vehicleNumber.isBlank()) {
            throw new IllegalArgumentException("Vehicle number cannot be empty");
        }

        return vehicleNumber
                .replaceAll("\\s+", "")
                .toUpperCase(Locale.ROOT);
    }

    // HMAC - Generation
    public String generateHmac(String normalizedVehicleNumber) {

        if (secretKey == null || secretKey.isBlank()) {
            throw new IllegalStateException("Vehicle HMAC secret is not configured");
        }

        try {

            Mac mac = Mac.getInstance("HmacSHA256");

            SecretKeySpec keySpec = new SecretKeySpec(
                    secretKey.getBytes(StandardCharsets.UTF_8),
                    "HmacSHA256"
            );

            mac.init(keySpec);

            byte[] hashBytes = mac.doFinal(
                    normalizedVehicleNumber.getBytes(StandardCharsets.UTF_8)
            );

            return HexFormat.of().formatHex(hashBytes);

        } catch (Exception exception) {
            throw new IllegalStateException("Unable to generate vehicle number HMAC", exception);
        }
    }

    public String generateVehicleNumberHash(String vehicleNumber) {

        String normalizedVehicleNumber = normalizeVehicleNumber(vehicleNumber);

        return generateHmac(normalizedVehicleNumber);
    }



}
