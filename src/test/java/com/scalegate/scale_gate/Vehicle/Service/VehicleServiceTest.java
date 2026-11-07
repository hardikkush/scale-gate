package com.scalegate.scale_gate.Vehicle.Service;

import com.scalegate.scale_gate.Vehicle.Repository.VehicleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.scalegate.scale_gate.Vehicle.Vehicle;
import com.scalegate.scale_gate.Vehicle.VehicleType;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VehicleServiceTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private VehicleNumberHashService vehicleNumberHashService;

    @InjectMocks
    private VehicleService vehicleService;

    @Test
    void shouldRegisterVehicleSuccessfully() {

        // ARRANGE
        String vehicleNumber = "UP32AB1234";
        String vehicleHash = "test-hash";

        when(vehicleNumberHashService.generateVehicleNumberHash(vehicleNumber))
                .thenReturn(vehicleHash);

        when(vehicleRepository.existsByVehicleNumberHash(vehicleHash))
                .thenReturn(false);

        Vehicle savedVehicle = Vehicle.builder()
                .vehicleNumberHash(vehicleHash)
                .vehicleType(VehicleType.LIGHT_VEHICLE)
                .build();

        when(vehicleRepository.save(any(Vehicle.class)))
                .thenReturn(savedVehicle);

        // ACT
        Vehicle result = vehicleService.registerVehicle(
                vehicleNumber,
                VehicleType.LIGHT_VEHICLE
        );

        // ASSERT
        assertNotNull(result);
        assertEquals(vehicleHash, result.getVehicleNumberHash());
        assertEquals(VehicleType.LIGHT_VEHICLE, result.getVehicleType());

        verify(vehicleRepository).save(any(Vehicle.class));

    }

    @Test
    void shouldRejectDuplicateVehicleRegistration() {

        // ARRANGE
        String vehicleNumber = "UP32AB1234";
        String vehicleHash = "test-hash";

        when(vehicleNumberHashService.generateVehicleNumberHash(vehicleNumber))
                .thenReturn(vehicleHash);

        when(vehicleRepository.existsByVehicleNumberHash(vehicleHash))
                .thenReturn(true);

        // ACT & ASSERT
        assertThrows(
                IllegalArgumentException.class,
                () -> vehicleService.registerVehicle(
                        vehicleNumber,
                        VehicleType.LIGHT_VEHICLE
                )
        );

        // VERIFY
        verify(vehicleRepository, never()).save(any(Vehicle.class));

    }

    @Test
    void shouldStopRegistrationWhenHashGenerationFails() {

        // ARRANGE
        String vehicleNumber = "";

        when(vehicleNumberHashService.generateVehicleNumberHash(vehicleNumber))
                .thenThrow(new IllegalArgumentException("Invalid vehicle number"));

        // ACT & ASSERT
        assertThrows(
                IllegalArgumentException.class,
                () -> vehicleService.registerVehicle(
                        vehicleNumber,
                        VehicleType.LIGHT_VEHICLE
                )
        );

        // VERIFY
        verify(vehicleRepository, never()).existsByVehicleNumberHash(anyString());
        verify(vehicleRepository, never()).save(any(Vehicle.class));

    }



}