package com.Techeer.Team_C.domain.product.service;

import static com.Techeer.Team_C.global.error.exception.ErrorCode.EMPTY_TOKEN_DATA;
import static com.Techeer.Team_C.global.error.exception.ErrorCode.PRODUCTREGISTER_NOT_FOUND;
import static com.Techeer.Team_C.global.error.exception.ErrorCode.PRODUCT_NOT_FOUND;
import static com.Techeer.Team_C.global.error.exception.ErrorCode.USER_NOT_FOUND;

import com.Techeer.Team_C.domain.product.dto.ProductDto;
import com.Techeer.Team_C.domain.product.dto.ProductRegisterEditDto;
import com.Techeer.Team_C.domain.product.dto.ProductRegisterRequestDto;
import com.Techeer.Team_C.domain.product.entity.Product;
import com.Techeer.Team_C.domain.product.entity.ProductRegister;
import com.Techeer.Team_C.domain.product.repository.ProductMysqlRepository;
import com.Techeer.Team_C.domain.product.repository.ProductRegisterMysqlRepository;
import com.Techeer.Team_C.domain.user.entity.User;
import com.Techeer.Team_C.domain.user.repository.UserRepository;
import com.Techeer.Team_C.global.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class ProductService {

    private final ProductMysqlRepository productMysqlRepository;
    private final ProductRegisterMysqlRepository productRegisterMysqlRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;

    private ProductDto dtoConverter(Product product) {
        return modelMapper.map(product, ProductDto.class);
    }

    public Optional<ProductDto> findProduct(Long id) {

        Optional<Product> product = productMysqlRepository.findById(id);
        if (!product.isPresent()) {
            throw new BusinessException("해당 상품 정보가 존재하지 않습니다.", PRODUCT_NOT_FOUND);
        }

        return product.map(productEntity -> dtoConverter(productEntity));
    }

    public List<ProductDto> pageList(String keyword, Pageable page) {
        Page<Product> lists = productMysqlRepository.findByNameContaining(keyword, page);

        return lists.getContent().stream().map(productEntity -> dtoConverter(productEntity)).collect(Collectors.toList());
    }

    public List<ProductRegister> registerList(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        return productRegisterMysqlRepository.findAllByUser(user.get());
    }

    @Transactional
    public ProductRegister saveRegister(ProductRegisterRequestDto productRegisterRequestDto, Long productId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null) {
            throw new BusinessException("Security Context 에 인증 정보가 없습니다", EMPTY_TOKEN_DATA);
        }
        Long id = Long.parseLong(authentication.getName());

        Optional<User> userById = userRepository.findById(id);
        if (!userById.isPresent()) {
            throw new BusinessException("존재하지 않는 사용자 입니다.", USER_NOT_FOUND);
        }
        Optional<Product> productById = productMysqlRepository.findById(productId);
        if (!productById.isPresent()) {
            throw new BusinessException("존재하지 않는 물품 입니다.", PRODUCT_NOT_FOUND);
        }

        ProductRegister productRegister = productRegisterMysqlRepository.build(userById.get(), productById.get(), productRegisterRequestDto.getDesiredPrice(), true);
        productRegisterMysqlRepository.save(productRegister);

        return productRegister;
    }

    @Transactional
    public ProductRegister editRegister(ProductRegisterEditDto productRegisterEditDto, Long productId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null) {
            throw new BusinessException("Security Context 에 인증 정보가 없습니다", EMPTY_TOKEN_DATA);
        }
        Long id = Long.parseLong(authentication.getName());

        Optional<User> userById = userRepository.findById(id);
        if (!userById.isPresent()) {
            throw new BusinessException("존재하지 않는 사용자 입니다.", USER_NOT_FOUND);
        }
        Optional<ProductRegister> productRegisterById = productRegisterMysqlRepository.findByUserAndProduct(userById.get(), productMysqlRepository.findById(productId).get());

        if (!productRegisterById.isPresent()) {
            throw new BusinessException("등록하지 않은 물품 입니다.", PRODUCTREGISTER_NOT_FOUND);
        }

        ProductRegister entity = productRegisterById.get();
        entity.update(entity.getUser(), entity.getProduct(), productRegisterEditDto.getDesiredPrice());

        productRegisterMysqlRepository.save(entity);
        return entity;
    }

    @Transactional
    public void deleteRegister(Long productId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null) {
            throw new BusinessException("Security Context 에 인증 정보가 없습니다", EMPTY_TOKEN_DATA);
        }

        Long userId = Long.parseLong(authentication.getName());
        Optional<User> userById = userRepository.findById(userId);
        if (!userById.isPresent()) {
            throw new BusinessException("존재하지 않는 사용자입니다.", USER_NOT_FOUND);
        }

        Optional<Product> productById = productMysqlRepository.findById(productId);

        if (!productById.isPresent()) {
            throw new BusinessException("존재하지 않는 물품입니다.", PRODUCT_NOT_FOUND);
        }

        Optional<ProductRegister> productRegisterById = productRegisterMysqlRepository.findByUserAndProduct(userById.get(), productById.get());
        productRegisterById.ifPresentOrElse(productRegister -> {
            productRegisterById.get().setStatus(false);
            productRegisterMysqlRepository.save(productRegister);
        }, () -> {
            throw new BusinessException("등록하지 않은 물품입니다.", PRODUCTREGISTER_NOT_FOUND);
        });

    }
}
