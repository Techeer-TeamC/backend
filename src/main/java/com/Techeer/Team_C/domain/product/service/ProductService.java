package com.Techeer.Team_C.domain.product.service;

import static com.Techeer.Team_C.global.error.exception.ErrorCode.EMPTY_TOKEN_DATA;
import static com.Techeer.Team_C.global.error.exception.ErrorCode.PRODUCTREGISTER_NOT_FOUND;
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

        Product temp = new Product();

        temp.setStatus(true);

        temp.setName("phone");

        temp.setOriginPrice(100000);

        temp.setMinimumPrice(100000);

        temp.setLink("https:...");

        Product temp1 = new Product();

        temp1.setStatus(true);

        temp1.setName("macbookMax");

        temp1.setOriginPrice(100000);

        temp1.setMinimumPrice(100000);

        temp1.setLink("https:...");

        Product temp2 = new Product();

        temp2.setStatus(true);

        temp2.setName("macBookMax14");

        temp2.setOriginPrice(100000);

        temp2.setMinimumPrice(100000);

        temp2.setLink("https:...");

        Product temp3 = new Product();

        temp3.setStatus(true);

        temp3.setName("macAook16");

        temp3.setOriginPrice(100000);

        temp3.setMinimumPrice(100000);

        temp3.setLink("https:...");

        Product temp4 = new Product();

        temp4.setStatus(true);

        temp4.setName("Iomne");

        temp4.setOriginPrice(100000);

        temp4.setMinimumPrice(100000);

        temp4.setLink("https:...");

        productMysqlRepository.save(temp);

        productMysqlRepository.save(temp1);

        productMysqlRepository.save(temp3);

        productMysqlRepository.save(temp4);

        return lists.getContent().stream().map(productEntity -> dtoConverter(productEntity)).collect(Collectors.toList());
    }

    public List<ProductRegister> registerList(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        return productRegisterMysqlRepository.findAllByUser(user.get());
    }

    @Transactional
    public void saveRegister(ProductRegisterRequestDto productRegisterRequestDto, Long productId) {

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

        ProductRegister productRegister = productRegisterMysqlRepository.findById(productRegisterRequestDto.getProductRegisterId()).orElseGet(() -> productRegisterMysqlRepository.build(userById.get(), productById.get(), productRegisterRequestDto.getDesiredPrice(), true));

//        ProductRegister insertData = new ProductRegister();
//        insertData.setDesiredPrice(productRegisterRequestDto.getDesiredPrice());
//        insertData.setUser(userById.get());
//        insertData.setProduct(productById.get());

        productRegisterMysqlRepository.save(productRegister);
    }

//    public void editRegister(ProductRegisterEditDto productEditDto, Long productId) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication == null || authentication.getName() == null) {
//            throw new BusinessException("Security Context 에 인증 정보가 없습니다", EMPTY_TOKEN_DATA);
//        }
//        Long id = Long.parseLong(authentication.getName());
//
//        Optional<User> userById = userRepository.findById(id);
//        if (!userById.isPresent()) {
//            throw new BusinessException("존재하지 않는 사용자 입니다.", USER_NOT_FOUND);
//        }
//        Optional<Product> productById = productMysqlRepository.findById(productId);
//        if (!productById.isPresent()) {
//            throw new BusinessException("존재하지 않는 물품 입니다.", PRODUCT_NOT_FOUND);
//        }
//
//        Optional<ProductRegister> productRegister = productRegisterMysqlRepository.findById(
//                productEditDto.getProductRegisterId());
//
////        productRegisterMysqlRepository.update(insertData);
//    }

    public void deleteRegister(Long productRegisterId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null) {
            throw new BusinessException("Security Context 에 인증 정보가 없습니다", EMPTY_TOKEN_DATA);
        }

        Long userId = Long.parseLong(authentication.getName());
        Optional<User> userById = userRepository.findById(userId);
        if (!userById.isPresent()) {
            throw new BusinessException("존재하지 않는 사용자입니다.", USER_NOT_FOUND);
        }

        Optional<ProductRegister> productRegisterById = productRegisterMysqlRepository.findById(productRegisterId);
        productRegisterById.ifPresentOrElse(productRegister -> {
            modelMapper.map(productRegisterById, ProductRegisterRequestDto.class).setStatus(false);
        }, () -> {
            throw new BusinessException("존재하지 않는 사용자입니다.", USER_NOT_FOUND);
        });

        if (!productRegisterById.isPresent()) {
            throw new BusinessException("등록하지 않은 물품입니다", PRODUCTREGISTER_NOT_FOUND);
        }

        Optional<Product> productById = productMysqlRepository.findById(modelMapper.map(productRegisterById, ProductDto.class).getProductId());
        if (!productById.isPresent()) {
            throw new BusinessException("존재하지 않는 물품입니다.", PRODUCT_NOT_FOUND);
        }
    }
}
