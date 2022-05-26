package com.Techeer.Team_C.domain.auth.dto;

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

    private String grantType;
    private String accessToken;
    private String refreshToken;
    private Long accessTokenExpiresIn;

    public JSONObject toJson() {
        JSONObject obj = new JSONObject();
        obj.put("grantType", grantType);
        obj.put("accessToken", accessToken);
        obj.put("refreshToken", refreshToken);
        obj.put("accessTokenExpiresIn", accessTokenExpiresIn);

        return obj;

    }
}

