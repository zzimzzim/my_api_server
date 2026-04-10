package com.example.my_api_server.lock;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class Counter {

    private int count = 0;//해당 공유영역 값을 동시에 수정

    public static void main(String[] args) {
        List<Thread> threads = new ArrayList<>();
        int threadCount = 200000;
        Counter counter = new Counter();

        //스레드 생성
        for (int i = 0; i < threadCount; i++) {
            Thread thread = new Thread(counter::increaseCount);
            thread.start();
            threads.add(thread);//스레드 리스드에 스레드 삽입
        }

        //스레드의 일이 다 끝날때까지 기다림
        threads.forEach(thread ->
        {
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        log.info("기대값 : {}", threadCount);
        log.info("기대값 : {}", counter.getCount());
    }

    private void increaseCount() {
        count++;
    }
}
