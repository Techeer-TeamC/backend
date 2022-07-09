package com.Techeer.Team_C.domain.product.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProductListDto {

    private String title;
    private String url;
    private String imageUrl;
    private Integer minimumPrice;

}
