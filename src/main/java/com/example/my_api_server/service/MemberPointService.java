package com.example.my_api_server.service;

import com.example.my_api_server.entity.Member;
import com.example.my_api_server.event.MemberSignUpEvent;
import com.example.my_api_server.repo.MemberDBRepo;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberPointService {

    private final MemberDBRepo memberDBRepo;


    //새로운 트랜잭션을 만들겟다!
    //AOP가 실행되면서 이러한 옵션값을 설정해서 트랜잭션을 생성합니다 begin
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.REPEATABLE_READ)
    public void changeAllUserData() {
        List<Member> members = memberDBRepo.findAll();

        //뭔가 값을 바꿧다고 가정해보겟습니다.
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void supportTxTest() {
        //db를 사용하지 않는, 다눈 자바 코드를 실행하거나
        //혹은 readonly=true 주로 최적화된 읽기를 사용할 때 가끔 사용(거의 안씀)
        memberDBRepo.findAll();
    }

    @Transactional(timeout = 2) //timeout는 트랜잭션의 총 실행시간을 제한, 시간 범위 내에서 총 실행시간 걸린다면 예외 발생
    public void timeout() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        memberDBRepo.findAll();
    }

    public String sendEmail(MemberSignUpEvent event, Long serialID){

        //코드가 변경됬습니다
        //알림보내는데 기타 로직이
        return "1";
    }
}