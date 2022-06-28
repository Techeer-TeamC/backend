package com.Techeer.Team_C.domain.auth.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.json.simple.JSONObject;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenDto {

    @ApiModelProperty(example = "토큰의 인증 타입")
    private String grantType;

    @ApiModelProperty(example = "accessToken")
    private String accessToken;

    @ApiModelProperty(example = "refreshToken")
    private String refreshToken;

    @ApiModelProperty(example = "accessToken의 만료 시간")
    private Long accessTokenExpiresIn;

}

