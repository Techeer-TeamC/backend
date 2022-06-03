package com.Techeer.Team_C.domain.product.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ProductRegisterRequestDto {

    private long productRegisterId;

    @NotNull
    private int desiredPrice;

    private boolean status;

}
