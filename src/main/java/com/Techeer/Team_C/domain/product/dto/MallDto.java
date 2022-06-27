package com.Techeer.Team_C.domain.product.dto;

import com.Techeer.Team_C.domain.product.entity.Product;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MallDto {

    private String link;
    private String name;
    private Integer price;
    private Integer delivery;
    private String interestFree;
    private String paymentOption;


}
