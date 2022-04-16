package com.Techeer.Team_C.User.dto;

import javax.validation.constraints.NotEmpty;



public class LoginFormDto {
    @NotEmpty(message ="id값은 필수사항 입니다.")
    private String userId;
    @NotEmpty(message ="비밀번호 값은 필수사항 입니다.")
    private String password;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}
