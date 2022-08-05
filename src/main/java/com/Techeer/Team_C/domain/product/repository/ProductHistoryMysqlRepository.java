package com.Techeer.Team_C.domain.product.repository;

import com.Techeer.Team_C.domain.product.entity.ProductHistory;
import java.util.Stack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface ProductHistoryMysqlRepository extends JpaRepository<ProductHistory, Long> {

    @Query(value = "SELECT * FROM product_history WHERE product_id = :id ORDER BY created_date DESC LIMIT :limitDataNum"
        , nativeQuery = true)
    Stack<ProductHistory> findByProductAndOrderByCreatedDateDesc(Long id, int limitDataNum);

    @Query(value =
        "SELECT p.* FROM (SELECT *, (@step \\:= @step+1) AS step "
            + "FROM product_history AS ph, (SELECT @step\\:= 0) AS s "
            + "WHERE ph.product_id= :id "
            + "ORDER BY ph.created_date DESC) AS p "
            + "WHERE p.step % :timeStandard <= :mallNum "
            + "AND p.step % :timeStandard != 0 "
            + "LIMIT :limitDataNum"
        , nativeQuery = true)
    Stack<ProductHistory> findPriceHistoryForSpecificTime(Long id,
        int mallNum, int timeStandard, int limitDataNum);

}
