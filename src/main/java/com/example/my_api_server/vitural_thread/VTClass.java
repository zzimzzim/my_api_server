package com.example.my_api_server.vitural_thread;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class VTClass {
    static final int TASK_COUNT = 1000;
    static final Duration IO_DURATION = Duration.ofSeconds(1);// I/O작업 시간

    public static void main(String[] args){
//        Thread vt1 = Thread.ofVirtual()
//                          .name("가상스레드1")
//                          .start(VTClass::ioRun);

        // I/O는 메모리를 더 쓰더라도 성능이 많이 차이남
//        log.info("[I/O]플랫폼 스레드 시작");
//        ioRun(Executors.newFixedThreadPool(200));//플랫폼 스레드 생성
//
//        log.info("[I/O]가상 스레드 시작");
//        ioRun(Executors.newVirtualThreadPerTaskExecutor());//가상 스레드 필요개수만큼 생성
//
//        //가상스레드는 힙에 생성됨 그로 인해 JVM 힙 메모리가 많이 사용되고, GC가 더 많은 일을 해야함
//        //사용자가 많아질수록 CPU연산의 차이는 크게 나지 않고 메모리를 많이 사용하게됨
//        log.info("[CPU]플랫폼 스레드 시작");
//        cpuRun(Executors.newFixedThreadPool(200));//플랫폼 스레드 생성
//
//        log.info("[CPU]가상 스레드 시작");
//        cpuRun(Executors.newVirtualThreadPerTaskExecutor());//가상 스레드 필요개수만큼 생성

//        log.info("[I/O]플랫폼 스레드 피닝 테스트 시작");
//        ioRunPinning(Executors.newFixedThreadPool(200));
//        log.info("[I/O]가상 스레드 피닝 테스트 시작");
//        ioRunPinning(Executors.newVirtualThreadPerTaskExecutor());

        log.info("[I/O]플랫폼 스레드 피닝 테스트2 시작");
        ioRunPinningRL(Executors.newFixedThreadPool(200));
        log.info("[I/O]가상 스레드 피닝 테스트2 시작");
        ioRunPinningRL(Executors.newVirtualThreadPerTaskExecutor());
    }

    //플랫폼 스레드 VS 가상 스레드 실행 속도 측정 비교
    public static void ioRun(ExecutorService es){
        Instant start = Instant.now(); //실행 시간 측정
        try(es){
            IntStream.range(0, TASK_COUNT).forEach(idx -> {
                es.submit(()->{
                    try {
                        //실제 외부 API및 DB연동코드(I/O발생) iobound
                        //가상스레드는 I/O를 만나면 umount되고 다른 가상스레드 실행
                        Thread.sleep(IO_DURATION);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                });
            });
        }//try-resource 문법 자동으로 리소스해제(es.close())해준다.

        Instant end = Instant.now(); //실행 시간 측정
        System.out.printf("작업 완료 시간: %d ms%n", Duration.between(start, end).toMillis());
    }

    public static void cpuRun(ExecutorService es){
        Instant start = Instant.now(); //실행 시간 측정
        try(es){
            IntStream.range(0, TASK_COUNT).forEach(idx -> {
                es.submit(()->{
                    // cpu연산(cpu bound)
                    for (int i = 0; i < 10000000; i++) {
                        int a=1;
                        int b=2;
                        int c = a + b;
                    }
                });
            });
        }//try-resource 문법 자동으로 리소스해제(es.close())해준다.

        Instant end = Instant.now(); //실행 시간 측정
        System.out.printf("작업 완료 시간: %d ms%n", Duration.between(start, end).toMillis());
    }

    public static void ioRunPinning(ExecutorService es){
        Instant start = Instant.now(); //실행 시간 측정
        try(es){
            IntStream.range(0, TASK_COUNT).forEach(idx -> {
                es.submit(()->{

                    //내부적으로 락을 사용한다고 가정
                    //synchronized는 커널의 세마포어/뮤텍스 객체를 사용해서 동시성을 제어함.
                    //systemcall -> 플랫폼 스레드 blocked됨. 그러면 플랫폼 스레드도 멈춤
                    //가상스레드의 장점이 없어지게됨
                    synchronized (es) {
                        try {
                            //실제 외부 API및 DB연동코드(I/O발생) iobound
                            //가상스레드는 I/O를 만나면 umount되고 다른 가상스레드 실행
                            Thread.sleep(IO_DURATION);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                });
            });
        }//try-resource 문법 자동으로 리소스해제(es.close())해준다.

        Instant end = Instant.now(); //실행 시간 측정
        System.out.printf("작업 완료 시간: %d ms%n", Duration.between(start, end).toMillis());
    }

    public static void ioRunPinningRL(ExecutorService es){
        Instant start = Instant.now(); //실행 시간 측정
        try(es){
            IntStream.range(0, TASK_COUNT).forEach(idx -> {
                es.submit(()->{
                    ReentrantLock lock = new ReentrantLock();
                    lock.lock();
                    try {
                        //실제 외부 API및 DB연동코드(I/O발생) iobound
                        //가상스레드는 I/O를 만나면 umount되고 다른 가상스레드 실행
                        Thread.sleep(IO_DURATION);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }finally {
                        lock.unlock();
                    }
                });
            });
        }//try-resource 문법 자동으로 리소스해제(es.close())해준다.

        Instant end = Instant.now(); //실행 시간 측정
        System.out.printf("작업 완료 시간: %d ms%n", Duration.between(start, end).toMillis());
    }
}
