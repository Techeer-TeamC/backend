package com.Techeer.Team_C.domain.product.repository;

import com.Techeer.Team_C.domain.product.entity.ProductRegister;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRegisterMysqlRepository extends JpaRepository<ProductRegister, Long> {
}
