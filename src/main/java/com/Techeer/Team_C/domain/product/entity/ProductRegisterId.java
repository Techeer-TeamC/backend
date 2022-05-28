package com.Techeer.Team_C.domain.product.entity;

import java.io.Serializable;
import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ProductRegisterId implements Serializable {

    private Long user;
    private Long product;

    @Builder
    public ProductRegisterId(Long userId, Long productId){
        this.user=userId;
        this.product=productId;
    }
}
