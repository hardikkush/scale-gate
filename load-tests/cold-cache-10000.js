
import http from 'k6/http';
import { check } from 'k6';
import exec from 'k6/execution';

const BASE_URL = 'http://localhost:8080';

const FIRST_VEHICLE_ID = 6;

export const options = {
    scenarios: {
        cold_cache_test: {
            executor: 'shared-iterations',
            vus: 5,
            iterations: 10000,
            maxDuration: '5m',
        },
    },
};

export default function () {

    // Every iteration receives a unique index across all VUs.
    const iterationIndex = exec.scenario.iterationInTest;

    const vehicleId = FIRST_VEHICLE_ID + iterationIndex;

    const response = http.get(
        `${BASE_URL}/api/v1/vehicles/${vehicleId}`
    );

    check(response, {
        [`vehicle ${vehicleId} returned 200`]:
            (res) => res.status === 200,
    });
}
