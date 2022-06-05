package com.Techeer.Team_C.domain.product.repository;

import com.Techeer.Team_C.domain.product.entity.Product;
import com.Techeer.Team_C.domain.product.entity.ProductRegister;
import com.Techeer.Team_C.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ProductRegisterMysqlRepository extends JpaRepository<ProductRegister, Long> {

    List<ProductRegister> findAllByUser(Long userId);

    @Transactional
    default ProductRegister build(User user, Product product, int desiredPrice, boolean status) {
        ProductRegister productRegister = ProductRegister.builder()
                .user(user)
                .product(product)
                .desiredPrice(desiredPrice)
                .status(status)
                .build();

        saveAndFlush(productRegister);

        return productRegister;
    }
}
