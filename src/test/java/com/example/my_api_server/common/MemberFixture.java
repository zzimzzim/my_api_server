package com.example.my_api_server.common;

import com.example.my_api_server.entity.Member;

//공통으로 사용하는 멤버를 생성해주는 클래스(테스트 용)
public class MemberFixture {

    //이메밀, 비밀번호(이메일은 고정된 값을 쓴다고 가정)
    //정적 팩토리 메서드 패턴(디자인 패턴의 생성 패턴 중 하나)

    public static Member.MemberBuilder defaultMember() {
        return Member.builder().email("test1@gmail.com");
    }
}
