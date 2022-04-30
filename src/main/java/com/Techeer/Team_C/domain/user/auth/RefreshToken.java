package com.Techeer.Team_C.domain.user.auth;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor

public class RefreshToken {

    private String key;
    private String value;

    @Builder
    public RefreshToken(String key, String value) {
        this.key = key;
        this.value = value;
    }


    public RefreshToken updateValue(String token) {
        this.value = token;
        return this;
    }
}
