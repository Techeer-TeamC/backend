package com.Techeer.Team_C.domain.product.repository;

import com.Techeer.Team_C.domain.product.entity.ProductHistory;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProductHistoryMysqlRepository extends JpaRepository<ProductHistory, Long> {

    @Query(value = "SELECT mall_name FROM product_history order by created_date asc limit 3", nativeQuery = true)
    List<String> getSavedMallName();
}
