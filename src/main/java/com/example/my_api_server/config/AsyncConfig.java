package com.example.my_api_server.config;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AsyncConfig {
    // I/O bound
    @Bean("ioExecutor")
    public ExecutorService ioExecutor(){
        return Executors.newVirtualThreadPerTaskExecutor();
    }

    //CPU bound
    //스레드 개수는 함부로 정해서는 안됨 (Contest Switch Cost)
    @Bean("cpuExecutor")
    public ExecutorService cpuExecutor(){
        //cpu코어 개수 확인
        int coreCount = Runtime.getRuntime().availableProcessors();

        return Executors.newFixedThreadPool(100);
    }
}
