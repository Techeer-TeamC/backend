package com.Techeer.Team_C.domain.product.dto;

import com.Techeer.Team_C.domain.product.entity.Mall;
import java.util.List;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ProductDto {

    private long productId;

    private String image;

    private String name;

    private List<Mall> mallInfo;
    
}
