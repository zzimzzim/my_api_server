package com.example.my_api_server.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.postgresql.PostgreSQLContainer;


@TestConfiguration(proxyBeanMethods = false) //스프링 DI사용안함(프록시 처리 관여X), 싱글톤으로 만들지않겠다.
public class TestContainerConfig {

    @Bean
    @ServiceConnection// 스프링 부트 4.0이 컨테이너 정보를 자동으로 DataSource에 주입함
    public PostgreSQLContainer postgreSQLContainer(
      @Value("${testcontainers.postgres.image:postgres:16-alpine}") String image
    ) {

        return new PostgreSQLContainer(image); // username/password 없음 - @ServiceConnection이 알아서 처리
    }
}
