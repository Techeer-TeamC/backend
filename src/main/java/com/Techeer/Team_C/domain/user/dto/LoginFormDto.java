package com.Techeer.Team_C.domain.user.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter

public class LoginFormDto {
    @NotEmpty(message ="id값은 필수사항 입니다.")
    private String userId;
    @NotEmpty(message ="비밀번호 값은 필수사항 입니다.")
    private String password;

    public UsernamePasswordAuthenticationToken toAuthentication() {
        return new UsernamePasswordAuthenticationToken(userId, password);
    }


}
