package com.Techeer.Team_C.domain.product.service;

import com.Techeer.Team_C.domain.product.dto.MallDto;
import com.Techeer.Team_C.domain.product.dto.ProductCrawlingDto;
import com.Techeer.Team_C.domain.product.dto.ProductDto;
import com.Techeer.Team_C.domain.product.dto.ProductPageListResponseDto;
import com.Techeer.Team_C.domain.product.dto.ProductRegisterEditDto;
import com.Techeer.Team_C.domain.product.dto.ProductRegisterRequestDto;
import com.Techeer.Team_C.domain.product.entity.Mall;
import com.Techeer.Team_C.domain.product.entity.Product;
import com.Techeer.Team_C.domain.product.entity.ProductHistory;
import com.Techeer.Team_C.domain.product.entity.ProductRegister;
import com.Techeer.Team_C.domain.product.repository.ProductHistoryMysqlRepository;
import com.Techeer.Team_C.domain.product.repository.ProductMallMysqlRepository;
import com.Techeer.Team_C.domain.product.repository.ProductMysqlRepository;
import com.Techeer.Team_C.domain.product.repository.ProductRegisterMysqlRepository;
import com.Techeer.Team_C.domain.user.entity.User;
import com.Techeer.Team_C.domain.user.repository.UserRepository;
import com.Techeer.Team_C.global.error.exception.BusinessException;
import java.util.ArrayList;
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

import static com.Techeer.Team_C.global.error.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductMysqlRepository productMysqlRepository;
    private final ProductRegisterMysqlRepository productRegisterMysqlRepository;
    private final ProductCrawler productCrawler;
    private final ModelMapper modelMapper;
    private final ProductMallMysqlRepository productMallMysqlRepository;
    private final UserRepository userRepository;
    private final ProductHistoryMysqlRepository productHistoryMysqlRepository;

    private ProductDto dtoConverter(Product product) {
        return modelMapper.map(product, ProductDto.class);
    }

    public ProductDto findProduct(Long id) {

        Optional<Product> product = productMysqlRepository.findById(id);
        if (!product.isPresent()) {
            throw new BusinessException("해당 상품 정보가 존재하지 않습니다.", PRODUCT_NOT_FOUND);
        }

        return product.map(productEntity -> dtoConverter(productEntity))
                .get();
    }

    public ProductPageListResponseDto pageList(String keyword, Pageable page) {
        Page<Product> lists = productMysqlRepository.findByNameContaining(keyword, page);

        ProductPageListResponseDto result = new ProductPageListResponseDto();

        result.setData(lists.getContent()
                .stream()
                .map(productEntity -> dtoConverter(productEntity))
                .collect(Collectors.toList()));

        result.setTotalCount(productMysqlRepository.countByNameContaining(keyword));

        return result;
    }

    public Integer searchCount(String keyword) {
        return productMysqlRepository.countByNameContaining(keyword);
    }

    public List<ProductRegister> registerList() {
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        if (authentication == null || authentication.getName() == null) {
            throw new BusinessException("Security Context 에 인증 정보가 없습니다", EMPTY_TOKEN_DATA);
        }
        Long id = Long.parseLong(authentication.getName());

        Optional<User> userById = userRepository.findById(id);
        if (!userById.isPresent()) {
            throw new BusinessException("존재하지 않는 사용자 입니다.", USER_NOT_FOUND);
        }

        return productRegisterMysqlRepository.findAllByUserAndStatus(userById.get(), true);
    }

    @Transactional
    public void autoUpdate() {
        List<Product> productList = new ArrayList<>(productMysqlRepository.findAll());
        for (Product product : productList) {   // 저장된 상품에 대해 크롤링 재진행
            ProductCrawlingDto productCrawlingDto = productCrawler.DanawaCrawling(
                    product.getUrl());

            List<String> mallNameList = productHistoryMysqlRepository.getSavedMallName();

            // 크롤링한 데이터를 히스토리에 저장하기 위한 로직
            for (MallDto mallData : productCrawlingDto.getMallDtoInfo()){
                if (mallNameList.contains(mallData.getName())){
                    ProductHistory newHistory = ProductHistory.builder()
                            .product(product)
                            .minimumPrice(mallData.getPrice())
                            .mallName(mallData.getName())
                            .build();
                    productHistoryMysqlRepository.save(newHistory);
                }
            }

            // 아래는 메일 발송을 위한 로직
//            Integer latestMinimum = productCrawlingDto.getMinimumPrice(); // 새로 크롤링된 최저값
//            if (latestMinimum < product.getMinimumPrice()){ // 가격이 더 하락했다면
//                for (ProductRegister productRegister :  productRegisterMysqlRepository.findByProduct(
//                        product)) { // 해당 제품을 등록한 사람을 찾고
//                    if (productRegister.getDesiredPrice() >= latestMinimum) { // 그 사람 요구 가격보다 낮은지
//
//                    }
//                }
//            }
        }
    }

    @Transactional
    public ProductRegister saveRegister(ProductRegisterRequestDto productRegisterRequestDto,
                                        String productName) {

        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        if (authentication == null || authentication.getName() == null) {
            throw new BusinessException("Security Context 에 인증 정보가 없습니다", EMPTY_TOKEN_DATA);
        }
        Long id = Long.parseLong(authentication.getName());

        Optional<User> userById = userRepository.findById(id);
        if (!userById.isPresent()) {
            throw new BusinessException("존재하지 않는 사용자 입니다.", USER_NOT_FOUND);
        }
        Optional<Product> productByName = productMysqlRepository.findByName(productName);
        if (!productByName.isPresent()) {
            productByName = productCrawler.storeProduct(
                    productCrawler.DanawaCrawling(productRegisterRequestDto.getUrl()));
        }
        Optional<ProductRegister> productRegisterById = productRegisterMysqlRepository.findByUserAndProduct(
                userById.get(), productByName.get());
        if (productRegisterById.isPresent()) {
            ProductRegister entity = productRegisterById.get();
            if (!entity.isStatus()) {
                entity.update(productRegisterRequestDto.getDesiredPrice(),true);
                return entity;
            } else {
                throw new BusinessException("이미 등록한 상품입니다.", DUPLICATE_PRODUCTREGISTER);
            }
        }

        int minimumPrice = productByName.get()
                .getMallInfo()
                .get(0)
                .getPrice();

        ProductRegister productRegister = productRegisterMysqlRepository.build(userById.get(),
                productByName.get(), productRegisterRequestDto.getDesiredPrice(), minimumPrice, true);
        productRegisterMysqlRepository.save(productRegister);

        return productRegister;
    }

    @Transactional
    public ProductRegister editRegister(ProductRegisterEditDto productRegisterEditDto,
                                        Long productId) {
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        if (authentication == null || authentication.getName() == null) {
            throw new BusinessException("Security Context 에 인증 정보가 없습니다", EMPTY_TOKEN_DATA);
        }
        Long id = Long.parseLong(authentication.getName());

        Optional<User> userById = userRepository.findById(id);
        if (!userById.isPresent()) {
            throw new BusinessException("존재하지 않는 사용자 입니다.", USER_NOT_FOUND);
        }
        Optional<ProductRegister> productRegisterById = productRegisterMysqlRepository.findByUserAndProduct(
                userById.get(), productMysqlRepository.findById(productId)
                        .get());

        if (!productRegisterById.isPresent()) {
            throw new BusinessException("등록하지 않은 물품 입니다.", PRODUCTREGISTER_NOT_FOUND);
        }

        ProductRegister entity = productRegisterById.get();
        entity.update(productRegisterEditDto.getDesiredPrice(), true);

        productRegisterMysqlRepository.save(entity);
        return entity;
    }

    @Transactional
    public void deleteRegister(Long productId) {
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
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

        Optional<ProductRegister> productRegisterById = productRegisterMysqlRepository.findByUserAndProduct(
                userById.get(), productById.get());
        productRegisterById.ifPresentOrElse(productRegister -> {
            productRegisterById.get()
                    .setStatus(false);
            productRegisterMysqlRepository.save(productRegister);
        }, () -> {
            throw new BusinessException("등록하지 않은 물품입니다.", PRODUCTREGISTER_NOT_FOUND);
        });

    }

    public List<Mall> getProductMallList(Long productId) {
        Optional<Product> productById = productMysqlRepository.findById(productId);
        if (!productById.isPresent()) {
            throw new BusinessException("존재하지 않는 물품입니다.", PRODUCT_NOT_FOUND);
        }

        Optional<List<Mall>> RegisteredProductMallList =
                productMallMysqlRepository.findAllByProduct(
                        Product.builder().
                                productId(productId)
                                .build());
        if (!RegisteredProductMallList.isPresent()) {
            throw new BusinessException("mall 정보가 존재하지 않습니다.", Mall_NOT_FOUND);
        }

        return RegisteredProductMallList.get();
    }
}
