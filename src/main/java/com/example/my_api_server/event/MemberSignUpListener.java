package com.example.my_api_server.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.resilience.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@Slf4j
public class MemberSignUpListener {//이벤트를 받는 리스너

    @Async//다른 객체 생성
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Retryable(maxRetries = 3)// 1. 상대방 서버 문제라 고쳐지는데 시간이 걸려서 의미가 없을수도 있음
    public void sendNotification(MemberSignUpEvent event) {
        log.info("member ID = {}", event.getId());
        log.info("member Email = {}", event.getEmail());

        try {
            Thread.sleep(5000); //1000ms = 1s
        } catch (InterruptedException e) {
            //2. 실패한 것들을 db에 저장했다가 나중에 한번에 대량알림 발송 -> 트랜잭션 아웃박스 패턴(실무)
            throw new RuntimeException(e);
        }
        log.info("알림 전송완료!");
    }
}
