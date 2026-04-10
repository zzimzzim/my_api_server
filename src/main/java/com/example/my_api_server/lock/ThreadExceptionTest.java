package com.example.my_api_server.lock;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//가상스레드 실습
public class ThreadExceptionTest {
    private int count = 0;

    public static void maint(String[] args){
        ThreadExceptionTest t = new ThreadExceptionTest();
        int threadCount = 10000;

        //newFixedThreadPool은 작업이 끝나도 커널에게 자원을 반납 X
        ExecutorService es = Executors.newFixedThreadPool(threadCount);

        for(int i=0; i < threadCount; i++){
            es.submit(t::increase);
        }
        es.shutdown();

        System.out.println("실행완료");
    }

    public void increase(){
        count++;
    }
}
