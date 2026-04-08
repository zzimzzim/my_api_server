package com.example.my_api_server.common;

import com.example.my_api_server.entity.Product;
import com.example.my_api_server.entity.ProductType;
import java.util.List;

public class ProductFixture {

    //.productNumber("TEST2")
    //                             .productName("티셔츠 2")
    //                             .productType(ProductType.CLOTHES)
    //                             .price(2000L)
    //                             .stock(2L)
    //                             .build();
    //.productType 공통으로 사용한다고 가정
    public static Product.ProductBuilder defaultProduct() {
        return Product.builder()
                 .productType(ProductType.CLOTHES);
    }

    //기본적으로 사용하는 고정된 테스트 데이터 리턴
    public static List<Product> defaultProducts() {
        Product product1 = Product.builder()
                             .productNumber("TEST1")
                             .productName("티셔츠 1")
                             .productType(ProductType.CLOTHES)
                             .price(1000L)
                             .stock(1L)
                             .build();

        Product product2 = Product.builder()
                             .productNumber("TEST2")
                             .productName("티셔츠 2")
                             .productType(ProductType.CLOTHES)
                             .price(2000L)
                             .stock(2L)
                             .build();

        return List.of(product1, product2);
    }
}
