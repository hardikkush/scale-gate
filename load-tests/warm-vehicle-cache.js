
import http from 'k6/http';
import { check } from 'k6';

const BASE_URL = 'http://localhost:8080';

const VEHICLE_IDS = Array.from(
    { length: 1000 },
    (_, index) => index + 6
);

let successfulRequests = 0;
let failedRequests = 0;

export const options = {
    vus: 1,
    iterations: 1,
};

export default function () {

    for (const vehicleId of VEHICLE_IDS) {

        const response = http.get(
            `${BASE_URL}/api/v1/vehicles/${vehicleId}`
        );

        check(response, {
            [`vehicle ${vehicleId} returned 200`]:
                (res) => res.status === 200,
        });

        if (response.status === 200) {
            successfulRequests++;
        } else {
            failedRequests++;
        }
    }

    console.log(`Successful requests: ${successfulRequests}`);
    console.log(`Failed requests: ${failedRequests}`);
}
