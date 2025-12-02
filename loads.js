import http from 'k6/http';
import { sleep } from 'k6';
import { check } from 'k6';

export const options = {
  // Very aggressive: ramps up to 600 virtual users
  stages: [
    { duration: '1m', target: 100 },  // warm-up
    { duration: '1m', target: 300 },  // ramp higher
    { duration: '2m', target: 600 },  // full load
    { duration: '1m', target: 0 },    // ramp down
  ],
  thresholds: {
    http_req_duration: ['p(95)<500'],       // 95% requests < 500 ms (you'll probably break this, which is fine)
    http_req_failed: ['rate<0.05'],         // < 5% failures
  },
};

const BASE_URL = 'http://fbs-app-alb-1790579884.ap-south-1.elb.amazonaws.com';

export default function () {
  const res = http.get(`${BASE_URL}/hc`);

  check(res, {
    'status is 200': (r) => r.status === 200,
  });

  // 0.1s sleep = up to ~10 requests/sec per VU at peak
  // 600 VUs ⇒ up to ~6000 requests/sec during full load
  sleep(0.1);
}