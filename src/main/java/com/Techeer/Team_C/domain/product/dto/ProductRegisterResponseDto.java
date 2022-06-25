package com.Techeer.Team_C.domain.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductRegisterResponseDto {
    private Long productRegisterId;

    private Long userId;

    private Long productId;

    private int desiredPrice;
}
