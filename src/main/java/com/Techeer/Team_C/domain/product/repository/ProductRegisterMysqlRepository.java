package com.Techeer.Team_C.domain.product.repository;

import com.Techeer.Team_C.domain.product.entity.ProductRegister;
import com.Techeer.Team_C.domain.product.entity.ProductRegisterId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRegisterMysqlRepository extends JpaRepository<ProductRegister, ProductRegisterId> {

}
