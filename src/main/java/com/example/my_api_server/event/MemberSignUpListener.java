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

    //아직 새로운 일꾼은 안붙혀서 일꾼 1번이 이 일을 진행합니다.
    //스레드 1번이 DB의 안정성(커밋) 확인되고나서 내 로직을 수행합니다.
    @Async("ioExecutor")//다른 객체 생성
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)//DB의 커밋이 된 후에 이 로직을 수행해줘
    //@Retryable(maxRetries = 3)// 1. 상대방 서버 문제라 고쳐지는데 시간이 걸려서 의미가 없을수도 있음
    public void sendNotification(MemberSignUpEvent event) {
        //Task-1이라는 스레드 2번 이작업을 시작합니다.
        log.info("member ID = {}", event.getId());
        log.info("member Email = {}", event.getEmail());
        log.info("member Email = {}", event.getSerialNum());

        //뭔가 추가적인 작업을 하게됩니다

        try {
            //실행하는 코드(피호출자), Blocking된다.
            Thread.sleep(5000); //1000ms = 1s
        } catch (InterruptedException e) {
            //2. 실패한 것들을 db에 저장했다가 나중에 한번에 대량알림 발송 -> 트랜잭션 아웃박스 패턴(실무)
            throw new RuntimeException(e);
        }
        log.info("알림 전송완료!");
    }
}
