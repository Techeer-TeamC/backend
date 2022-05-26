package com.Techeer.Team_C.domain.user.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter

public class SignupFormDto {

    @NotEmpty(message = "id값은 필수사항 입니다.")
    private String email;
    @NotEmpty(message = "비밀번호 값은 필수사항 입니다.")
    private String password;
    @NotEmpty(message = "닉네임 값은 필수사항 입니다.")
    private String memberName;


}
