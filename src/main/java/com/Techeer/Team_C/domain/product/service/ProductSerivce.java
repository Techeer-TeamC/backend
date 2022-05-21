package com.Techeer.Team_C.domain.product.service;


import static com.Techeer.Team_C.global.error.exception.ErrorCode.EMPTY_TOKEN_DATA;
import static com.Techeer.Team_C.global.error.exception.ErrorCode.PRODUCT_NOT_FOUND;
import static com.Techeer.Team_C.global.error.exception.ErrorCode.USER_NOT_FOUND;

import com.Techeer.Team_C.domain.product.dto.ProductDto;
import com.Techeer.Team_C.domain.product.dto.ProductRegisterRequestDto;
import com.Techeer.Team_C.domain.product.entity.Product;
import com.Techeer.Team_C.domain.product.entity.ProductRegister;
import com.Techeer.Team_C.domain.product.repository.ProductMysqlRepository;

import com.Techeer.Team_C.domain.product.repository.ProductRegisterMysqlRepository;
import com.Techeer.Team_C.domain.user.entity.User;
import com.Techeer.Team_C.domain.user.repository.UserRepository;
import com.Techeer.Team_C.global.error.exception.BusinessException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class ProductSerivce {

    private final ProductMysqlRepository productMysqlRepository;
    private final ProductRegisterMysqlRepository productRegisterMysqlRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;


    private ProductDto of(Product product) {
        return modelMapper.map(product, ProductDto.class);

    }

    public Optional<ProductDto> findProduct(Long id) {

        Optional<Product> product = productMysqlRepository.findById(id);
        if (!product.isPresent()) {
            throw new BusinessException("해당 상품 정보가 존재하지 않습니다.", PRODUCT_NOT_FOUND);
        }

        Optional<ProductDto> productDto = product.map(q -> of(q));

        return productDto;
    }

    public List<ProductDto> pageList(String keyword, Pageable page) {
        Page<Product> lists = productMysqlRepository.findByNameContaining(keyword, page);
        List<ProductDto> results = lists.getContent().stream().map(q -> of(q))
                .collect(Collectors.toList());

        return results;
    }


    public void addResister(ProductRegisterRequestDto productRegisterRequestDto) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null) {
            throw new BusinessException("Security Context 에 인증 정보가 없습니다", EMPTY_TOKEN_DATA);
        }
        Long id = Long.parseLong(authentication.getName());

        Optional<User> userById = userRepository.findById(id);
        if (!userById.isPresent()) {
            throw new BusinessException("존재하지 않는 사용자 입니다.", USER_NOT_FOUND);
        }
        Optional<Product> productById = productMysqlRepository.findById(
                Long.parseLong(productRegisterRequestDto.getId()));
        if (!productById.isPresent()) {
            throw new BusinessException("존재하지 않는 물품 입니다.", PRODUCT_NOT_FOUND);
        }

        ProductRegister insertData = new ProductRegister();
        insertData.setDesired_price(productRegisterRequestDto.getDesired_price());
        insertData.setUser(userById.get());
        insertData.setProduct(productById.get());

        productRegisterMysqlRepository.save(insertData);

    }
}
