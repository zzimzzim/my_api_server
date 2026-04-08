package com.example.my_api_server.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;


@Component
@Aspect
@Slf4j
public class LogAspect {

    //메서드 실행 하면 시간을 찍어볼게요
    @Around("execution(* com.example.my_api_server.service..*(..))")
    public Object logging(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();

        try {
            return joinPoint.proceed();
        } finally {
            long endTime = System.currentTimeMillis();
            log.info(joinPoint.getSignature() + " AOP 잘됫나?: " + (endTime - startTime) + "ms");
            //실무에서는 AOP 로깅
            //운영환경에서 모든 로그를 수집하지않고 특정 필요한로그를 AOP에서 ES(ELK)
        }
    }
}
