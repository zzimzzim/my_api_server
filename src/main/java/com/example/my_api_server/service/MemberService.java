package com.example.my_api_server.service;

//비즈니스 로직이 구성됨

import com.example.my_api_server.entity.Member;
import com.example.my_api_server.repo.MemberRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service //bean으로 등록
@RequiredArgsConstructor //생성자 주입 di
@Slf4j //로그
public class MemberService {

    private final MemberRepo memberRepo;

    /**
     * 회원가입 - 회원 저장 후 알림을 전송한다.
     *
     * @param email
     * @param password
     * @return
     */
    public Long signUp(String email, String password) {
        Long memberId = memberRepo.saveMember(email, password);

        log.info("회원가입한 member ID = {}", memberId);

        //알림 전송
        sendNotification();

        return memberId;
    }


    public void dbTest() {

        //비지니스 로직
        //회원 탈퇴, 회원 멤버심 포인트 지급

    }


    //BEGIN TRAN
    //COMMIT; or ROLLBACK;
    // 알림 외부 API 호출(TCP <-> HTTP 통신하게됩니다)
    public void sendNotification() {
        log.info("모든 메서드 실행 전 로그 출력 해야함");
        try {
            Thread.sleep(1000); //1000ms = 1s
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        log.info("알림 전송완료!");
        log.info("모든 메서드 실행 후 로그 출력 해야함");
    }

    //회원 조회
    public Member findMember(Long id) {
        Member member = memberRepo.findMember(id);
        return member;
    }
}