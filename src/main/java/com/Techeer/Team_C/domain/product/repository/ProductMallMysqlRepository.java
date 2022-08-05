package com.Techeer.Team_C.domain.product.repository;

import com.Techeer.Team_C.domain.product.entity.Mall;
import com.Techeer.Team_C.domain.product.entity.Product;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductMallMysqlRepository extends JpaRepository<Mall, Long> {

    Optional<List<Mall>> findAllByProduct(Product product);

    Optional<List<Mall>> findTop3ByProduct(Product product);

}
