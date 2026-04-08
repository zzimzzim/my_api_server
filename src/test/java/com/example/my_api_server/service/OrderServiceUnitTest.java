package com.example.my_api_server.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.my_api_server.entity.Member;
import com.example.my_api_server.entity.Order;
import com.example.my_api_server.entity.OrderStatus;
import com.example.my_api_server.entity.Product;
import com.example.my_api_server.entity.ProductType;
import com.example.my_api_server.repo.MemberDBRepo;
import com.example.my_api_server.repo.OrderRepo;
import com.example.my_api_server.repo.ProductRepo;
import com.example.my_api_server.service.dto.OrderCreateDto;
import com.example.my_api_server.service.dto.OrderResponseDto;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/*
OrderService 단위테스트
 */
@ExtendWith(MockitoExtension.class) //Mockito 활성화
class OrderServiceUnitTest {

    @Mock //가짜 객체 생성
    ProductRepo productRepo;

    @Mock //가짜 객체 생성
    MemberDBRepo memberDBRepo;

    @Mock //가짜 객체 생성
    OrderRepo orderRepo;

    @InjectMocks //실제 테스트할 대상 클래스(Mock 객체를 자동으로 주입받는다.)
    OrderService orderService;

    InitData initData;
    OrderCreateDto orderCreateDto;

    @BeforeEach//테스트 실행하기 전 이 메서드 실행
    public void init() {
        initData = new InitData();

        //given(when절에 필요한 데이터들 생성)
        initData.memberId = 1L;
        initData.productIds = List.of(1L, 2L);
        initData.counts = List.of(1L, 2L);

        initData.product1 = Product.builder()
                              .productNumber("TEST1")
                              .productName("티셔츠 1")
                              .productType(ProductType.CLOTHES)
                              .price(1000L)
                              .stock(2L)
                              .build();

        initData.product2 = Product.builder()
                              .productNumber("TEST2")
                              .productName("티셔츠 2")
                              .productType(ProductType.CLOTHES)
                              .price(2000L)
                              .stock(4L)
                              .build();

        initData.member = Member.builder()
                            .email("test1@gmail.com")
                            .password("1234")
                            .build();

        orderCreateDto = new OrderCreateDto(initData.memberId,
          initData.productIds, initData.counts);
    }

    @Test
    @DisplayName("tset1")
    public void test1() {
        //given(then절에 필요한 데이터)
        int a = 10;

        //when(실제 수행할 메서드)
        a++;

        //then(테스트 결과를 확인)
        assertThat(a).isEqualTo(11);

    }

    //TC1
    @Test
    @DisplayName("[HAPPY]주문 요청이 정상적으로 잘 등록된다")
    public void createOrderSuccess() {
        //given(when절에 필요한 데이터들 생성)
        Long stockId = 1L;//공통 데이터 말고 추가적인 테스트 데이터 필요 시 추가

        //DB와 통신하지 않게 우리가 proxy처럼 임의로 실행 시켜줘야한다.
        when(productRepo.findAllById(initData.productIds)).thenReturn(
          List.of(initData.product1, initData.product2));
        when(memberDBRepo.findById(initData.memberId)).thenReturn(Optional.of(initData.member));

        //save 호출 시 save된 order 객체 반환되게
        when(orderRepo.save(any())).thenAnswer(invocation ->
                                                 invocation.getArgument(0));

        //when(테스트할 메서드)
        LocalDateTime orderTime = LocalDateTime.now();//시간 생성
        OrderResponseDto dto = orderService.createOrder(orderCreateDto, orderTime);

        //then(값 검증)
        ArgumentCaptor<Order> capture = ArgumentCaptor.forClass(Order.class);
        verify(orderRepo).save(capture.capture());

        assertThat(dto.isSuccess()).isTrue();
        assertThat(dto.getOrderStatus()).isEqualTo(OrderStatus.COMPLETED);

    }


    @Test
    @DisplayName("[Exception]주문 요청시 재고 부족하면 예외처리가 정상적으로 잘 동작한다")
    public void productStockValid() {
        //given(when절에 필요한 데이터들 생성)
        Long memberId = 1L;
        List<Long> productIds = List.of(1L, 2L);
        List<Long> counts = List.of(10L, 20L);

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

        Member member = Member.builder()
                          .email("test1@gmail.com")
                          .password("1234")
                          .build();

        OrderCreateDto createDto = new OrderCreateDto(memberId, productIds, counts);

        //DB와 통신하지 않게 우리가 proxy처럼 임의로 실행 시켜줘야한다.
        when(productRepo.findAllById(productIds)).thenReturn(
          List.of(product1, product2));
        when(memberDBRepo.findById(memberId)).thenReturn(Optional.of(member));

        //when(테스트할 메서드)

        //then(값 검증)
        LocalDateTime orderTime = LocalDateTime.now();//시간 생성
        assertThatThrownBy(() -> orderService.createOrder(createDto, orderTime))
          .isInstanceOf(RuntimeException.class)//해당 예외 클래스가 어떤건지 지정합니다.
          .hasMessage("재고가 음수이니 주문 할 수 없습니다!"); //해당 예외 메시지가 어떤건지 지정
    }


    //@Test
    @DisplayName("[Exception]주문 시간 날짜 오류 테스트")
    public void orderTimeException() {

        //DB와 통신하지 않게 우리가 proxy처럼 임의로 실행 시켜줘야한다.
        when(productRepo.findAllById(initData.productIds)).thenReturn(
          List.of(initData.product1, initData.product2));
        when(memberDBRepo.findById(initData.memberId)).thenReturn(Optional.of(initData.member));

        //when(테스트할 메서드)
        LocalDateTime orderTime = LocalDateTime.now();//시간 생성
        OrderResponseDto dto = orderService.createOrder(orderCreateDto, orderTime);

        //then(값 검증)
        assertThat(dto).isNotNull();
    }

    //테스트 용 초기데이터 클래스
    public class InitData {

        public Long memberId;
        public List<Long> productIds;
        public List<Long> counts;

        public Product product1;

        public Product product2;

        public Member member;
    }
}