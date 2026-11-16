
package com.scalegate.scale_gate.FlaggedVehicle;

import com.scalegate.scale_gate.Vehicle.Service.VehicleNumberHashService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Profile("dev")
@RequiredArgsConstructor
public class FlaggedVehicleSeeder implements CommandLineRunner {

    private final FlaggedVehicleRepository flaggedVehicleRepository;
    private final VehicleNumberHashService vehicleNumberHashService;

    @Override
    public void run(String... args) {

        if (flaggedVehicleRepository.count() > 0) {
            System.out.println("Flagged vehicle records already exist. Skipping seeding.");
            return;
        }

        List<String> mockVehicleNumbers = List.of(
                "DL01CR1001", "DL01CR1002", "DL01CR1003",
                "DL01CR1004", "DL01CR1005", "DL02CR1006",
                "DL02CR1007", "DL02CR1008", "DL02CR1009",
                "DL02CR1010", "HR26CR1011", "HR26CR1012",
                "HR26CR1013", "HR26CR1014", "HR26CR1015",
                "UP16CR1016", "UP16CR1017", "UP16CR1018",
                "UP16CR1019", "UP16CR1020", "RJ14CR1021",
                "RJ14CR1022", "RJ14CR1023", "RJ14CR1024",
                "RJ14CR1025"
        );

        for (String vehicleNumber : mockVehicleNumbers) {

            String hash = vehicleNumberHashService
                    .generateVehicleNumberHash(vehicleNumber);

            FlaggedVehicle flaggedVehicle = FlaggedVehicle.builder()
                    .vehicleNumberHash(hash)
                    .reason("MOCK_FLAGGED_VEHICLE")
                    .build();

            flaggedVehicleRepository.save(flaggedVehicle);
        }

        System.out.println("25 mock flagged vehicles seeded successfully.");
    }
}
