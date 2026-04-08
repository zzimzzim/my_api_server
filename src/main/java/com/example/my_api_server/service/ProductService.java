package com.example.my_api_server.service;

import com.example.my_api_server.entity.Product;
import com.example.my_api_server.repo.ProductRepo;
import com.example.my_api_server.service.dto.ProductCreateDto;
import com.example.my_api_server.service.dto.ProductResDto;
import com.example.my_api_server.service.dto.ProductUpdateDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Slf4j
public class ProductService {

    private final ProductRepo productRepo;

    //상품 등록
    //JPA 하이버네이트는 DB랑 통신하기위해, DB의 ACID가 되기위해선 begin tran; commit 무조건 되어야한다
    @Transactional
    public ProductResDto createProduct(ProductCreateDto dto) {
        Product product = Product.builder()
                              .productName(dto.getProductName())
                              .productType(dto.getProductType())
                              .productNumber(dto.getProductNumber())
                              .price(dto.getPrice())
                              .stock(dto.getStock())
                              .build();

        Product savedProduct = productRepo.save(product);//영속화

        //Entity -> DTO 변환
        ProductResDto resDto = ProductResDto.builder()
                                   .productNumber(savedProduct.getProductNumber())
                                   .stock(savedProduct.getStock())
                                   .price(savedProduct.getPrice())
                                   .build();

        return resDto;
    }

    //상품 조회, 트랜잭션없이!
    public ProductResDto findProduct(Long productId) {
        //DB에서 조회한거를 바탕으로 조회해서 영속성 컨텍스트에 저장한다(1차 캐시 캐싱). 그값을 리턴해준다!
        Product product = productRepo.findById(productId).orElseThrow();

        //Entity -> DTO 변환
        ProductResDto resDto = ProductResDto.builder()
                                   .productNumber(product.getProductNumber())
                                   .stock(product.getStock())
                                   .price(product.getPrice())
                                   .build();

//        Product product2 = productRepo.findById(productId).orElseThrow(); //select 쿼리 x

        return resDto;
    }

    //상품 수정(더티체킹)
    @Transactional
    public ProductResDto updateProduct(ProductUpdateDto dto) {
        //1. 조회
        Product product = productRepo.findById(dto.productId()).orElseThrow();

        //2. 필요한것만 수정(상품명, 재고수량)
        product.changeProductName(dto.changeProductName());
        product.increaseStock(dto.changeStock()); //수정시 더한다고 가정

        //3. 리턴
        //Entity -> DTO 변환
        ProductResDto resDto = ProductResDto.builder()
                                   .productNumber(product.getProductNumber())
                                   .stock(product.getStock())
                                   .price(product.getPrice())
                                   .build();

        return resDto;
    }
}