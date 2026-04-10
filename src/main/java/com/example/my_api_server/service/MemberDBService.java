package com.example.my_api_server.service;

import com.example.my_api_server.entity.Member;
import com.example.my_api_server.event.MemberSignUpEvent;
import com.example.my_api_server.repo.MemberDBRepo;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor //생성자 주입 DI
@Slf4j
public class MemberDBService {

    private final MemberDBRepo memberDBRepo;
    private final MemberPointService memberPointService;
    private final ApplicationEventPublisher publisher; //이벤트를 보내줄 publisher

    //회원저장(DB에 저장)

    /**
     * 1. @Transcational은 AOP로 돌아가서 begin tran() commit() 2. DB에는 commit 명령어가 실행되어야 테이블에
     * 반영됩니다.(redo,undo log) -> table 저장 3. Jpa의 구현체인 하이버네이트와 엔티티매니저 JDBC Driver <-> DB 일련의 과정을
     * Spring 자동으로해줍니다
     */
    @Transactional//기본적으로 런타임 예외만 롤백해주는데, IOException 예외로 롤백해주겟다!
    public Long signUp(String email, String password) throws IOException {
        Member member = Member.builder()
                            .email(email)
                            .password(password)
                            .build();

        //동기 - 작업이 완료될때까지 기다린다.
        //비동기 - 작업이 완료될때까지 기다리지 않는다.

        //저장
        // 동기, 블로킹
        // i/o 발행한다고 가정 sleep
        Member savedMember = memberDBRepo.save(member); //1s
        //DB에 커밋이 정상적으로 잘 된다면(일꾼1), 그때 메일 알림을 발송하면 어떨까요?(일꾼2, 재시도 3번)
        //기능 안정성 + 예외 상황 + 알림이 몇초가 x -> 전체의 총 서버의 응답시간은 단축되지 않을까요?

        //기존의 로직에서는 값이 바뀌거나, 리턴값이 바뀌거나, 파라미터 추가되거나하면 기존 로직이 영향을 입는다(Side Effect)
        memberPointService.sendEmail(
          new MemberSignUpEvent(savedMember.getId(), savedMember.getEmail(), "1"), 1L);//기존로직(다른 서비스에서 메일 보냈다)

        //이벤트 발송
        //변경지점이 되게 작아지게되는 유지보수성 Up(강결합 해결)
        publisher.publishEvent( //리팩토링 후 로직(이벤트)
          new MemberSignUpEvent(savedMember.getId(), savedMember.getEmail(), "d")); //이벤트 발송

        //        sendNotification(); //i/o 작업이 트랜잭션에 같이 있어도 되는걸까? 10s
//        memberPointService.changeAllUserData();

//        throw new IOException("외부 API 호출하다가 I/O 예외가 터짐");
        //I/O 입센션 우리측 문제가아니라 상대측 문제이기떄문에 상대측 서버가 헬스체크가 된다면, 다시 보내줘야하는 로직을 구성해야(재전송 로직)

//        DB에 저장하다가 뭔가 오류가 발생해서 예외가 터짐(Runtime 예외)
//        throw new RuntimeException("DB에 저장하다가 뭔가 오류가 발생해서 예외가 터짐");
        return savedMember.getId();
    } //11s DB 작업이 끝난후에 따로 스레드가 실행하면되지않을까? 총 시간은 1~2s, 회원가입 빠름

    //회원저장(DB에 저장)
    //해당 스레드의 영속성 컨텍스트도 GC에 의해 JVM 메모리에서 제거됩니다.(OSIV = false)

    //새로운 트랜잭션을 만들겟다!
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void changeAllUserData() {
        List<Member> members = memberDBRepo.findAll();

        //뭔가 값을 바꿧다고 가정해보겟습니다.
    }

    //이메일, 알림 가정
//    @Async // 비동기로(다른 스레드 일을 시키는겁니다) 다른 스레드(톰캣 워커스레드)에게 일을 시키겟다
//    @Retryable(maxRetries = 3, includes = InterruptedException.class) //실패를 대비해서 3번 재전송하게끔하겟다
    public void sendNotification() {
        try {
            Thread.sleep(5000); //1000ms = 1s
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        log.info("알림 전송완료!");
    }

    //TX 테스트 메서드
    @Transactional(propagation = Propagation.REQUIRED, timeout = 2)
    public void tx1() {
        List<Member> members = memberDBRepo.findAll();
        members.stream()
            .forEach((m) -> {
                log.info("member id = {}", m.getId());
                log.info("member email = {}", m.getEmail());
            });
        memberPointService.changeAllUserData(); //AOP

        memberPointService.timeout(); //타임아웃 테스트
    }


}