package com.Techeer.Team_C.domain.product.dto;

import com.Techeer.Team_C.domain.product.entity.Product;
import com.Techeer.Team_C.domain.user.entity.User;
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

    private User user;

    private Product product;

    private int desiredPrice;
}
