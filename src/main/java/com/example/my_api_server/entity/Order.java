package com.example.my_api_server.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor //기본 생성자 생성
@AllArgsConstructor //매개변수를 다받는 생성자 생성
@Table(name = "orders")
@Getter
@Builder
public class Order {

    //Order가 저장되면 OrderProduct도 같이 저장된다.(생명주기를 동일하게 하겠다)
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    List<OrderProduct> orderProducts = new ArrayList<>();
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //PK
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member buyer; //구매자
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus; //주문상태

    //상품(N) 바지,신발,모자 : 주문(1) 1:N 관계를 나타내야합니다.
    //주문(1) <-> 주문상품(여러 상품들) <-> 상품(1)
    @Column(nullable = false)
    private LocalDateTime orderTime; //주문 시간

    //정적 팩토리 패턴
    public static Order createOrder(Member member, LocalDateTime orderTime) {
        Order order = Order.builder()
                        .buyer(member)
                        .orderStatus(OrderStatus.PENDING)
                        .orderTime(orderTime)
                        .build();

        return order;
    }

    //루트 엔티티(에거리거트 루트)
    public OrderProduct createOrderProduct(Long orderCount, Product product) {
        return OrderProduct.builder()
                 .order(this)
                 .number(orderCount) //product에 맞는 주문개수를 찾는다!
                 .product(product)
                 .build();
    }

    //양방향 매핑
    public void addOrderProducts(List<OrderProduct> orderProduct) {
        this.orderProducts = orderProduct;
    }
}