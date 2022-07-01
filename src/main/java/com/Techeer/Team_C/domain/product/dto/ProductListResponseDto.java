package com.Techeer.Team_C.domain.product.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class ProductListResponseDto {

    private Integer totalNumber;
    private List<ProductListDto> productListDtoList;

}
