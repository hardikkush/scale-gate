package com.scalegate.scale_gate.Vehicle.Service;

import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

public class VehicleNumberHashServiceTest {

    @Test
    void shouldNormalizeVehicleNumber() {

        // ARRANGE
        VehicleNumberHashService service = new VehicleNumberHashService();

        // ACT
        String result = service.normalizeVehicleNumber("up32 ab1234");

        // ASSERT
        assertEquals("UP32AB1234",result);
    }

    @Test
    void shouldNormalizeVehicleNumberRegardlessOfCaseAndSpaces() {

        // ARRANGE
        VehicleNumberHashService service = new VehicleNumberHashService();

        // ACT
        String result1 = service.normalizeVehicleNumber("up32 ab1234");
        String result2 = service.normalizeVehicleNumber("UP32AB1234");

        // ASSERT
        assertEquals(result1, result2);
    }

    @Test
    void shouldGenerateSameHashForSameVehicleNumber() {

        // ARRANGE
        VehicleNumberHashService service = new VehicleNumberHashService();

        ReflectionTestUtils.setField(
                service,
                "secretKey",
                "test-secret-key-for-testing-only"
        );

        // ACT
        String hash1 = service.generateVehicleNumberHash("UP32AB1234");
        String hash2 = service.generateVehicleNumberHash("UP32AB1234");

        // ASSERT
        assertEquals(hash1, hash2);
    }

    @Test
    void shouldThrowExceptionWhenVehicleNumberIsEmpty() {

        // ARRANGE
        VehicleNumberHashService service = new VehicleNumberHashService();

        // ACT & ASSERT
        assertThrows(
                IllegalArgumentException.class,
                () -> service.normalizeVehicleNumber("")
        );
    }

    @Test
    void shouldThrowExceptionWhenVehicleNumberIsNull() {

        // ARRANGE
        VehicleNumberHashService service = new VehicleNumberHashService();

        // ACT & ASSERT
        assertThrows(
                IllegalArgumentException.class,
                () -> service.normalizeVehicleNumber(null)
        );
    }

    @Test
    void shouldThrowExceptionWhenVehicleNumberContainsOnlySpaces() {

        // ARRANGE
        VehicleNumberHashService service = new VehicleNumberHashService();

        // ACT & ASSERT
        assertThrows(
                IllegalArgumentException.class,
                () -> service.normalizeVehicleNumber("   ")
        );
    }

    @Test
    void shouldGenerateDifferentHashesForDifferentVehicleNumbers() {

        // ARRANGE
        VehicleNumberHashService service = new VehicleNumberHashService();

        ReflectionTestUtils.setField(
                service,
                "secretKey",
                "test-secret-key-for-testing-only"
        );

        // ACT
        String hash1 = service.generateVehicleNumberHash("UP32AB1234");
        String hash2 = service.generateVehicleNumberHash("UP32CD5678");

        // ASSERT
        assertNotEquals(hash1, hash2);
    }


}
