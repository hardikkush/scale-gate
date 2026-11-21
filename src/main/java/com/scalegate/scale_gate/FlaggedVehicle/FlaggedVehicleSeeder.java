
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
                "WB1001", "WB1002", "WB1003",
                "WB1004", "WB1005", "WB1006",
                "WB1007", "WB1008", "WB1009",
                "WB1010", "WB1011", "WB1012",
                "WB1013",

                "AS2001", "AS2002", "AS2003",
                "AS2004", "AS2005", "AS2006",
                "AS2007", "AS2008", "AS2009",
                "AS2010", "AS2011", "AS2012"
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
