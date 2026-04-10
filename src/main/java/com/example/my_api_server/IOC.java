package com.example.my_api_server;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

//실제 스프링에게 빈(객체)으로 등록하게 해주는 설정

@Component
//@Configuration
public class IOC {

    //@Bean
    //메서드 단위로 등록을합니다. 메서드의 리턴 타입, IOC_TEST를 IOC 컨테이너에 등록해준다!
    public void func1(){
        System.out.println("func1 실행");
    };


    public static void main(String[] args) {
        //객체 생성
        //(메모리 (RAM), JVM Heap 메모리에 사용한다.
        //저의 방이 5평인데 물건을 계속 들여와여 OOM(Out of Heap memory)

        //spring한테 우리가 IOC라는 객체를 만들어 줄테니, 대신에 하나로만 만들어서 재사용하게 해줘!(IOC)
        //개발자가 직접 객체를 만드는게 아니라, 스프링 프레임워크가 관리해주는 것. 필요할 때 스프링이 주입해 준다(DI)
        IOC ioc = new IOC();

        //객체의 메서드 호출
        ioc.func1();
    }

}
//IOC 컨테이너에 등록이 됩니다. (객체 = 물건, 단 하나만 생성이 됩니다!, 싱글톤 패턴)