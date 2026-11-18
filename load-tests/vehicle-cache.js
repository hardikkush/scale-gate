
import http from 'k6/http';
import { check, sleep } from 'k6';

const BASE_URL = 'http://localhost:8080';

export default function () {
    const response = http.get(
        `${BASE_URL}/api/v1/vehicles/3`
    );

    check(response, {
        'status is 200': (res) => res.status === 200,
    });

    sleep(1);
}
