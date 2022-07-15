package com.Techeer.Team_C.domain.product.repository;

import com.Techeer.Team_C.domain.product.entity.Product;
import com.Techeer.Team_C.domain.product.entity.ProductHistory;
import java.util.List;
import java.util.Stack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface ProductHistoryMysqlRepository extends JpaRepository<ProductHistory, Long> {

    List<ProductHistory> findTop3ByProduct(Product product);

    @Query(value = "SELECT * FROM product_history where product_id = :id order by created_date desc LIMIT 30"
        , nativeQuery = true)
    Stack<ProductHistory> findTop30ByProductAndOrderByCreatedDateDesc(Long id);
}
