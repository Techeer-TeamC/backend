package com.Techeer.Team_C.domain.product.dto;

import com.Techeer.Team_C.domain.product.entity.ProductRegister;

public class ProductRegisterMapper {

   public ProductRegisterResponseDto toResponseDto(ProductRegister entity) {
      return ProductRegisterResponseDto.builder()
              .productRegisterId(entity.getProductRegisterId())
              .user(entity.getUser())
              .product(entity.getProduct())
              .desiredPrice(entity.getDesiredPrice())
              .build();
   }
}
