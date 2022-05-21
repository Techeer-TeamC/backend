package com.Techeer.Team_C.domain.product.dto;

import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductRegisterRequestDto {
    @NotNull
    String id;

    @NotNull
    Integer desired_price;


}
