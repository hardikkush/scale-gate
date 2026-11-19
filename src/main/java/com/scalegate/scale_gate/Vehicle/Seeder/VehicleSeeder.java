
package com.scalegate.scale_gate.Vehicle.Seeder;

import com.scalegate.scale_gate.Vehicle.Repository.VehicleRepository;
import com.scalegate.scale_gate.Vehicle.Service.VehicleNumberHashService;
import com.scalegate.scale_gate.Vehicle.Vehicle;
import com.scalegate.scale_gate.Vehicle.VehicleType;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Profile("dev")
@RequiredArgsConstructor
public class VehicleSeeder implements CommandLineRunner {

    private final VehicleRepository vehicleRepository;
    private final VehicleNumberHashService vehicleNumberHashService;

    @Override
    public void run(String... args) {

        List<Vehicle> vehicles = new ArrayList<>();

        addVehicles(vehicles, "PJ", 400, VehicleType.LIGHT_VEHICLE);
        addVehicles(vehicles, "HM", 350, VehicleType.HEAVY_VEHICLE);
        addVehicles(vehicles, "GA", 250, VehicleType.TWO_WHEELER);

        List<String> hashes = vehicles.stream()
                .map(Vehicle::getVehicleNumberHash)
                .toList();

        long existingCount =
                vehicleRepository.countByVehicleNumberHashIn(hashes);

        if (existingCount == 1000) {
            System.out.println("Vehicle dataset already exists. Skipping seeding.");
            return;
        }

        if (existingCount > 0) {
            throw new IllegalStateException(
                    "Partial vehicle dataset detected. Expected 1000 records but found "
                            + existingCount
            );
        }

        vehicleRepository.saveAll(vehicles);

        System.out.println("1000 benchmark vehicles seeded successfully.");
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
