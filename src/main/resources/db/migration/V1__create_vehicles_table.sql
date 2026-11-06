CREATE TABLE vehicles
(
    vehicle_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,

    vehicle_number_hash VARCHAR(64) NOT NULL UNIQUE,

    vehicle_type VARCHAR(30) NOT NULL
        CHECK (vehicle_type IN (
                                'LIGHT_VEHICLE',
                                'HEAVY_VEHICLE',
                                'TWO_WHEELER'
            )),

    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);
