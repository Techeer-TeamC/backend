package com.Techeer.Team_C.domain.product.repository;

import com.Techeer.Team_C.domain.product.entity.Product;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductMysqlRepository extends JpaRepository<Product,Long> {
    Optional<Product> findByName(String name);
}
