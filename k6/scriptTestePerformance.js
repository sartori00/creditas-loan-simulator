import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
  scenarios: {
    load_test_with_stages: {
      executor: 'ramping-vus',
      startVUs: 0,
      gracefulStop: '5s',

      stages: [
        { duration: '15s', target: 300 }, // Aumenta de 0 para 300 VUs em 15 segundos
        { duration: '30s', target: 300 }, // Mantém 300 VUs por 30 segundos
        { duration: '10s', target: 0 },  // Diminui de 300 para 0 VUs em 10 segundos
      ],
    },
  },
  thresholds: {
    'http_req_duration': ['p(95)<1'],
    'http_req_failed': ['rate<0.01'],
  },
};

export default function () {
  const url = 'http://localhost:8090/v1/loan';

  const headers = {
    'accept': 'application/json',
    'Content-Type': 'application/json',
  };

  const payload = JSON.stringify({
    "currency": "EUR",
    "loanAmount": 1587.5,
    "qtInstallments": 48,
    "person": {
      "document": "07062032751",
      "birthDay": "1955-05-05",
      "email": "fakeemail@fake.com"
    }
  });

  const res = http.post(url, payload, { headers: headers });


  check(res, {
    'status is 200': (r) => r.status === 200,
    'response body contains totalAmountToPay': (r) => r.body && r.body.includes('totalAmountToPay')
  });

  sleep(1);
}
