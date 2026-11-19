import http from 'k6/http';
import { check } from 'k6';

export const options = {
    vus: 5,
    duration: '30s',
};

const BASE_URL = 'http://localhost:8080';

const VEHICLE_IDS = Array.from(
    { length: 1000 },
    (_, index) => index + 6
);

function getRandomVehicleId() {
    const randomIndex = Math.floor(
        Math.random() * VEHICLE_IDS.length
    );

    return VEHICLE_IDS[randomIndex];
}

export default function () {

    const vehicleId = getRandomVehicleId();

    const response = http.get(
        `${BASE_URL}/api/v1/vehicles/${vehicleId}`
    );

    check(response, {
        'status is 200': (res) => res.status === 200,
    });
}
