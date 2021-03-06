package com.Techeer.Team_C.domain.product.repository;

import com.Techeer.Team_C.domain.product.entity.Product;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface ProductMysqlRepository extends JpaRepository<Product,Long> {
    Page<Product> findByNameContaining(String name, Pageable pageable);
    Integer countByNameContaining(@Param("name") String name);
    Optional<Product> findByName(String name);
    List<Product> findAll();
}
