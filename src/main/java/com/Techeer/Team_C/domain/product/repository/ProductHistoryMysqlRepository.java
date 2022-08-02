package com.Techeer.Team_C.domain.product.repository;

import com.Techeer.Team_C.domain.product.entity.Product;
import com.Techeer.Team_C.domain.product.entity.ProductHistory;
import java.util.List;
import java.util.Stack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface ProductHistoryMysqlRepository extends JpaRepository<ProductHistory, Long> {

    List<ProductHistory> findTop3ByProduct(Product product);

    @Query(value = "SELECT * FROM product_history where product_id = :id order by created_date desc LIMIT :limitDataNum"
        , nativeQuery = true)
    Stack<ProductHistory> findByProductAndOrderByCreatedDateDesc(Long id, int limitDataNum);

    @Query(value =
        "select p.* from (select *, (@step \\:= @step+1) as step from product_history as ph,"
            + "(select @step\\:= 0) as s where ph.product_id= :id order by ph.created_date desc) as p "
            + "where p.step % :timeStandard <= :mallNum "
            + "and p.step % :timeStandard != 0 "
            + "LIMIT :limitDataNum"
        , nativeQuery = true)
    Stack<ProductHistory> findPriceHistoryForSpecificTime(Long id,
        int mallNum, int timeStandard, int limitDataNum);

}
