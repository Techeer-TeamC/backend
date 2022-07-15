package com.Techeer.Team_C.domain.product.repository;

import com.Techeer.Team_C.domain.product.entity.Product;
import com.Techeer.Team_C.domain.product.entity.ProductRegister;
import com.Techeer.Team_C.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ProductRegisterMysqlRepository extends JpaRepository<ProductRegister, Long> {


    List<ProductRegister> findAllByUserAndStatus(User user, boolean status);
    Optional<ProductRegister> findByUserAndProduct(User user, Product product);

    List<ProductRegister> findByProduct(Product product);

    @Transactional
    default ProductRegister build(User user, Product product, int desiredPrice, int minimumPrice, boolean status) {
        ProductRegister productRegister = ProductRegister.builder()
                .user(user)
                .product(product)
                .desiredPrice(desiredPrice)
                .minimumPrice(minimumPrice)
                .status(status)
                .build();

        return productRegister;
    }
}
