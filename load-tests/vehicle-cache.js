
import http from 'k6/http';
import { check } from 'k6';

export const options = {
    vus: 5,
    duration: '30s',
};

const BASE_URL = 'http://localhost:8080';

export default function () {
    const response = http.get(
        `${BASE_URL}/api/v1/vehicles/3`
    );

    check(response, {
        'status is 200': (res) => res.status === 200,
    });
}
