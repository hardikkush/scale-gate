
import http from 'k6/http';
import { check } from 'k6';
import exec from 'k6/execution';

const BASE_URL = 'http://localhost:8080';

// Benchmark vehicle IDs: 6 through 25005
const FIRST_VEHICLE_ID = 6;
const TOTAL_VEHICLES = 25000;

export const options = {
    scenarios: {
        sustained_cold_cache: {
            executor: 'constant-vus',
            vus: 5,
            duration: '10s',
        },
    },
};

export default function () {
    const iterationIndex = exec.scenario.iterationInTest;

    if (iterationIndex >= TOTAL_VEHICLES) {
        return;
    }

    const vehicleId = FIRST_VEHICLE_ID + iterationIndex;

    const response = http.get(
        `${BASE_URL}/api/v1/vehicles/${vehicleId}`
    );

    check(response, {
        [`vehicle ${vehicleId} returned 200`]:
            (res) => res.status === 200,
    });
}
