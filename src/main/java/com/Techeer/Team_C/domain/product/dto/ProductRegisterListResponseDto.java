package com.Techeer.Team_C.domain.product.dto;

import com.Techeer.Team_C.domain.product.entity.ProductRegister;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductRegisterListResponseDto {
    private List<ProductRegister> product;
}
