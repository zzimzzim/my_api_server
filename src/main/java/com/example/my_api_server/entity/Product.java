package com.example.my_api_server.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "products")
@Getter
@Builder
public class Product {//상품
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //pk

    private String productName; //상품명

    @Column
    private String productNumber;//상품번호

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductType productType;//상품타입

    private Long price;//가격

    private Long stock;//재고
    //상품명, 상품번호, 상품타입(의류, 음식등), 가격, 재고수량
    @Version
    private Long version;

    //필요한 것만 바꿀수있게 Setter처럼 변경할수 있게하고, 네이밍은 의미있는 메서드로 만들어둡니다.
    public void changeProductName(String changeProductName) {
        this.productName = changeProductName;
    }

    //재고 -,+
    public void increaseStock(Long addStock) {
        this.stock += addStock; //현재 재고 + 더해줄 재고
    }

    public void decreaseStock(Long subStock) {
        this.stock -= subStock; //현재 재고 + 감소할 재고
    }

    //구매가능여부 확인
    public void buyProductWithStock(Long orderCount) {
        if (this.getStock() - orderCount < 0) {
            throw new RuntimeException("재고가 음수이니 주문 할 수 없습니다!");
        }
        this.decreaseStock(orderCount);
    }
}
