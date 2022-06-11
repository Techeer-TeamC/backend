package com.Techeer.Team_C.domain.user.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter

public class SignupFormDto {

    @ApiModelProperty(example = "회원 아이디(이메일)")
    @NotEmpty(message = "id값은 필수사항 입니다.")
    private String email;

    @ApiModelProperty(example = "회원 비밀번호")
    @NotEmpty(message = "비밀번호 값은 필수사항 입니다.")
    private String password;

    @ApiModelProperty(example = "회원 이름(닉네임)")
    @NotEmpty(message = "닉네임 값은 필수사항 입니다.")
    private String memberName;


}
