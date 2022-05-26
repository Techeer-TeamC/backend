package com.Techeer.Team_C.domain.user.dto;


import javax.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PasswordChangeRequestDto {

    @NotEmpty(message = "현재 비밀번호 값은 필수사항 입니다.")
    private String oldPassword;

    @NotEmpty(message = "새로 설정할 비밀번호를 다시 입력해야 합니다.")
    private String reNewPassword;

    @NotEmpty(message = "새로 설정할 비밀번호는 필수사항 입니다.")
    private String newPassword;

}
