package com.Techeer.Team_C.domain.user.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter

public class LoginFormDto {

    @ApiModelProperty(example = "회원 아이디(이메일)")
    @NotEmpty(message = "id값은 필수사항 입니다.")
    private String email;

    @ApiModelProperty(example = "회원 비밀번호")
    @NotEmpty(message = "비밀번호 값은 필수사항 입니다.")
    private String password;

    public UsernamePasswordAuthenticationToken toAuthentication() {
        return new UsernamePasswordAuthenticationToken(email, password);
    }


}
