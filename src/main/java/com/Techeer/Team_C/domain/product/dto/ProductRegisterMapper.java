package com.Techeer.Team_C.domain.product.dto;

import com.Techeer.Team_C.domain.product.entity.ProductRegister;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProductRegisterMapper {

   public ProductRegisterResponseDto toResponseDto(ProductRegister entity) {
      return ProductRegisterResponseDto.builder()
              .productRegisterId(entity.getProductRegisterId())
              .userId(entity.getUser().getUserId())
              .productId(entity.getProduct().getProductId())
              .desiredPrice(entity.getDesiredPrice())
              .build();
   }
}
