package com.Techeer.Team_C.domain.product.repository;

import com.Techeer.Team_C.domain.product.entity.Product;
import com.Techeer.Team_C.domain.product.entity.ProductHistory;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProductHistoryMysqlRepository extends JpaRepository<ProductHistory, Long> {

    List<ProductHistory> findTop3ByProduct(Product product);
}
