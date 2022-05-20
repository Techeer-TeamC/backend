package com.Techeer.Team_C.domain.product.service;


import static com.Techeer.Team_C.global.error.exception.ErrorCode.EMPTY_TOKEN_DATA;
import static com.Techeer.Team_C.global.error.exception.ErrorCode.PRODUCT_NOT_FOUND;

import com.Techeer.Team_C.domain.product.dto.ProductDto;
import com.Techeer.Team_C.domain.product.entity.Product;
import com.Techeer.Team_C.domain.product.repository.ProductMysqlRepository;
//import com.Techeer.Team_C.domain.product.repository.ProductRegisterMysqlRepository;
import com.Techeer.Team_C.domain.user.dto.UserDto;
import com.Techeer.Team_C.domain.user.entity.User;
import com.Techeer.Team_C.domain.user.repository.UserRepository;
import com.Techeer.Team_C.global.error.exception.BusinessException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class ProductSerivce {
    private final ProductMysqlRepository productMysqlRepository;
  //  private final ProductRegisterMysqlRepository productRegisterMysqlRepository;
    private final ModelMapper modelMapper;


    private ProductDto of(Product product) {
        return modelMapper.map(product, ProductDto.class);

    }

    public Optional<ProductDto> findProduct(Long id) {

        Product temp = new Product();
        temp.setActivated(true);
        temp.setOrigin_price(500);
        temp.setMinimum_price(0);
        temp.setName("아이이");
        temp.setProduct_detail("아아아아아");
        temp.setProduct_image("asdsad");
        temp.setShipment("asdads");


        productMysqlRepository.save(temp);

        Optional<Product> product = productMysqlRepository.findById(id);
        if(!product.isPresent()) {
            throw new BusinessException("해당 상품 정보가 존재하지 않습니다.", PRODUCT_NOT_FOUND);
        }

        Optional<ProductDto> productDto = product.map(q -> of(q));

        return productDto;
    }

}
