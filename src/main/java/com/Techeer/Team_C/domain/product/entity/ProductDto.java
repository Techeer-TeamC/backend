package com.Techeer.Team_C.domain.product.entity;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDto {

    private String title;
    private String image;
    private List<Mall> mallInfo;


}
