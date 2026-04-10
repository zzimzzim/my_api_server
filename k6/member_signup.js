import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
  // 테스트 설정 (예: 10명이 30초 동안 반복 호출)
  vus: 2,
  duration: '2s',
  thresholds: { //SLA(Service Level Agreement) 준수: 서비스가 보장해야 하는 최소한의 속도를 강제할 수 있음
    // 1. 응답 시간 기준: 95%의 요청이 500ms(0.5초) 이내여야 함
    http_req_duration: ['p(95)<500'],

    // 2. 에러율 기준: 에러 발생률이 1% 미만이어야 함
    http_req_failed: ['rate<0.01'],

    // 3. 특정 요청에 대한 기준 (주문 API 전용)
    'http_req_duration{url:http://localhost:8080/api/v1/member}': ['p(99)<1000'],
  },
};

export default function () {
  const url = 'http://localhost:8080/api/v1/member'; // 서버 주소에 맞게 수정하세요

  // DTO 구조에 맞춘 페이로드 설정
  const payload = JSON.stringify({
    email: "test1@gmail.com",
    password: "1234",
  });

  const params = {
    headers: {
      'Content-Type': 'application/json',
    },
  };

  // POST 요청 전송
  const res = http.post(url, payload, params);

  // 결과 검증 (HTTP 상태 코드가 200 또는 201인지 확인)
  check(res, {
    'is status 200': (r) => r.status === 200,
  });

  // 다음 요청 전 휴식 (1초)
  sleep(1);
}