package com.Techeer.Team_C.domain.product.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Mall {
    private String link;
    private String price;
    private String delivery;
    private String interestFree;
    private String paymentOption;

}
