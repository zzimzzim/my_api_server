package com.example.my_api_server.controller;

import com.example.my_api_server.service.ProductService;
import com.example.my_api_server.service.dto.ProductCreateDto;
import com.example.my_api_server.service.dto.ProductResDto;
import com.example.my_api_server.service.dto.ProductUpdateDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    //상품생성
    @PostMapping
    public ProductResDto createProduct
    (@Validated @RequestBody ProductCreateDto dto) {
        ProductResDto resDto = productService.createProduct(dto);
        return resDto;

    }

    //상품 조회
    @GetMapping("/{id}")
    public ProductResDto findProduct(@PathVariable Long id) {
        ProductResDto dto = productService.findProduct(id);
        return dto;
    }

    //상품수정
    @PatchMapping //일부분만 수정하겠다.
    public ProductResDto updateProduct
    (@Validated @RequestBody ProductUpdateDto dto) {
        ProductResDto resDto = productService.updateProduct(dto);
        return resDto;

    }

}
