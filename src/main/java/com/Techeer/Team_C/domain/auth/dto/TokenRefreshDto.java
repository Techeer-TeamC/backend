package com.Techeer.Team_C.domain.auth.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TokenRefreshDto {

    @ApiModelProperty(example = "accessToken")
    private String accessToken;

    @ApiModelProperty(example = "refreshToken")
    private String refreshToken;
}
