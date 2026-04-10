package com.example.my_api_server.controller;

import com.example.my_api_server.service.OrderService;
import com.example.my_api_server.service.dto.OrderCreateDto;
import com.example.my_api_server.service.dto.OrderResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


//반복적으로 사용하는 문법이나 코드들 = 보일러 플레이트
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/orders")
public class OrderControler {

    private final OrderService orderService;

    @PostMapping
    public OrderResponseDto createOrder(@Validated @RequestBody OrderCreateDto dto) {
        return orderService.createOrderPLock(dto);
    }

    @GetMapping("/{id}")
    public OrderResponseDto findOrder(@PathVariable Long id) {

        return orderService.findOrder(id);
    }

}
