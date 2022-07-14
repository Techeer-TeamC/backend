package com.Techeer.Team_C.domain.auth.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AuthorizationGoogle {
    private String access_token;
    private String expires_in;
    private String id_token;
    private String scope;
    private String token_type;
}
