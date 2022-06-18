package com.Techeer.Team_C.domain.product.dto;

import com.Techeer.Team_C.domain.product.entity.ProductRegister;

import java.util.List;

public class ProductRegisterListMapper {

    public ProductRegisterListResponseDto toResponseDto(List<ProductRegister> entity) {
        return ProductRegisterListResponseDto.builder().product(entity).build();
    }
}
