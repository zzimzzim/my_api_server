package com.example.my_api_server.repo;

import com.example.my_api_server.entity.Product;
import jakarta.persistence.LockModeType;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long> {

    //FOR NO KEY UPDATE 레코드 락(PG), FOR UPDATE(Mysql)
    //동일한 레코드에 대해서 동시에 업데이트를 방지한다.
    //그래서 트랜잭션이 동일한 로우에 대해서 최신 스냅샷을 읽기때문에 동시성 이슈가 없어지게됩니다.(정합성 보장!)
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Product p WHERE p.id IN :ids ORDER BY p.id")
    //JPQL(자바 객체로 쿼리 구성하는 방법)
    List<Product> findAllByIdsWithXLock(List<Long> ids);


    //    @Lock(LockModeType.PESSIMISTIC_READ) //FOR_SHARE
    //FOR-UPDATE Lock(PG에서는 자체 최적화로 FOR NO KEY UPDATE, Mysql에서는 for udpate)
    @Lock(LockModeType.PESSIMISTIC_WRITE) //FOR UPDATE
    @Query("SELECT p FROM Product p WHERE p.id IN :ids ORDER BY p.id")
    List<Product> findAllByIdsWithLock(List<Long> ids);
    //데드락 방지로 인한 동일한 순서로 lock 획득

}
