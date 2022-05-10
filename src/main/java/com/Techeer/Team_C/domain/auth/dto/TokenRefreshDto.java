package com.Techeer.Team_C.domain.auth.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TokenRefreshDto {

    private String accessToken;
    private String refreshToken;
}