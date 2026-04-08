package com.example.my_api_server.service.dto;

import java.time.LocalDateTime;
import java.util.List;

public record OrderCreateDto(
  Long memberId, //구매자
  List<Long> productId, //주문상품 ID들
  List<Long> count, //주문수량
  LocalDateTime orderTime //주문 시간

) {

    public OrderCreateDto {
        if (orderTime == null) {
            orderTime = LocalDateTime.now();
        }
    }

    public OrderCreateDto(Long memberId, List<Long> productId, List<Long> count) {
        this(memberId, productId, count, LocalDateTime.now());
    }
}