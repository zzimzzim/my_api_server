package com.example.my_api_server.lock;

import java.lang.Thread.State;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class SyncCounter {

    private int count = 0;//해당 공유영역 값을 동시에 수정

    public static void main(String[] args) {
        List<Thread> threads = new ArrayList<>();
        int threadCount = 100;
        SyncCounter counter = new SyncCounter();

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

        //스레드 1번이 들어오면서 락을 획득합니다.
        State state = Thread.currentThread().getState();
        log.info("state1 = {}", state.toString());

        //해당 범위만 락을 얻겟다!(스레드 1 -> 스레드 2 -> 스레드 3)
        synchronized (this) { //락으로 순서 제어
            log.info("state2(락을 얻는 부분) = {}", state.toString());
            count++; //연산..
        }
        //스레드 1번이 락을 반환합니다.
        log.info("state3 = {}", state.toString());
    }
}
