
package com.scalegate.scale_gate.Vehicle.Seeder;

import com.scalegate.scale_gate.Vehicle.Repository.VehicleRepository;
import com.scalegate.scale_gate.Vehicle.Service.VehicleNumberHashService;
import com.scalegate.scale_gate.Vehicle.Vehicle;
import com.scalegate.scale_gate.Vehicle.VehicleType;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
@Profile("dev")
@RequiredArgsConstructor
public class VehicleSeeder implements CommandLineRunner {

    private final VehicleRepository vehicleRepository;
    private final VehicleNumberHashService vehicleNumberHashService;

    private static final int ADDITIONAL_VEHICLES = 15000;
    private static final int EXPECTED_TOTAL_VEHICLES = 25005;

    @Override
    @Transactional
    public void run(String... args) {

        List<Vehicle> vehicles = new ArrayList<>();

        // Generate 15,000 additional benchmark vehicles.

        addVehicles(
                vehicles,
                "JK",
                5000,
                VehicleType.LIGHT_VEHICLE
        );

        addVehicles(
                vehicles,
                "KL",
                5000,
                VehicleType.HEAVY_VEHICLE
        );

        addVehicles(
                vehicles,
                "LW",
                5000,
                VehicleType.TWO_WHEELER
        );

        // Extract generated vehicle hashes.
        List<String> hashes = vehicles.stream()
                .map(Vehicle::getVehicleNumberHash)
                .toList();

        // Check how many of these vehicles already exist.
        long existingCount =
                vehicleRepository.countByVehicleNumberHashIn(hashes);

        if (existingCount == ADDITIONAL_VEHICLES) {
            System.out.println(
                    "Additional vehicle dataset already exists. Skipping seeding."
            );
            return;
        }

        if (existingCount > 0) {
            throw new IllegalStateException(
                    "Partial additional vehicle dataset detected. Expected "
                            + ADDITIONAL_VEHICLES
                            + " records but found "
                            + existingCount
            );
        }

        // Insert the additional 15,000 vehicles.
        vehicleRepository.saveAll(vehicles);

        System.out.println(
                "Additional 15,000 benchmark vehicles seeded successfully."
        );

        // Verify final database count.
        long totalCount = vehicleRepository.count();

        System.out.println(
                "Total vehicles in database: " + totalCount
        );

        if (totalCount != EXPECTED_TOTAL_VEHICLES) {
            throw new IllegalStateException(
                    "Expected " + EXPECTED_TOTAL_VEHICLES
                            + " total vehicles but found "
                            + totalCount
            );
        }
    }

    private void addVehicles(
            List<Vehicle> vehicles,
            String registrationPrefix,
            int count,
            VehicleType vehicleType) {

        for (int i = 1; i <= count; i++) {

            String vehicleNumber = registrationPrefix
                    + String.format("%02d", i)
                    + "CR"
                    + String.format("%04d", i);

            String hash = vehicleNumberHashService
                    .generateVehicleNumberHash(vehicleNumber);

            Vehicle vehicle = Vehicle.builder()
                    .vehicleNumberHash(hash)
                    .vehicleType(vehicleType)
                    .build();

            vehicles.add(vehicle);
        }
    }
}
