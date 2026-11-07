package com.scalegate.scale_gate.Vehicle.Repository;

import com.scalegate.scale_gate.Vehicle.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle , Long> {
    Optional<Vehicle> findByVehicleNumberHash(String vehicleNumberHash);
    boolean existsByVehicleNumberHash(String vehicleNumberHash);
}
