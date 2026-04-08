package com.example.my_api_server.service.dto;

import com.example.my_api_server.entity.OrderStatus;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
@Builder
public class OrderResponseDto {

    //주문완료시간, 주문상태, 주문성공여부
    private LocalDateTime orderCompletedTime; //주문완료시간

    private OrderStatus orderStatus; //주문상태

    private boolean isSuccess; //주문성공여부

}
