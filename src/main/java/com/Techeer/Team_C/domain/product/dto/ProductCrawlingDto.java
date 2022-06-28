package com.Techeer.Team_C.domain.product.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductCrawlingDto {
    private String title;
    private String image;
    private List<MallDto> mallDtoInfo;

}
