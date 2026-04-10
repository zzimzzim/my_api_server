package com.example.my_api_server.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "order_products")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class OrderProduct {//주문한 상품들

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //주문(1) <-> 주문상품(여러 상품들) <-> 상품(1)
    //상품, 주문, 주문수량
    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;//FK

    @ManyToOne(fetch = FetchType.LAZY)
    private Order order; //FK

    private Long number; //FK

}
