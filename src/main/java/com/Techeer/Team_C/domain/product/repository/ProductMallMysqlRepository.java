package com.Techeer.Team_C.domain.product.repository;

import com.Techeer.Team_C.domain.product.entity.Mall;
import com.Techeer.Team_C.domain.product.entity.Product;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductMallMysqlRepository extends JpaRepository<Mall, Long> {

    Optional<List<Mall>> findAllByProduct(Product product);

    @Query(value = "SELECT * from mall where product_id = :productId LiMIT 3", nativeQuery = true)
    Optional<List<Mall>> findMallByProduct(Long productId);

}
