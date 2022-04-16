package com.Techeer.Team_C.domain.user.dto;

import javax.validation.constraints.NotEmpty;



public class SignupFormDto {
    @NotEmpty(message ="id값은 필수사항 입니다.")
    private String userId;
    @NotEmpty(message ="비밀번호 값은 필수사항 입니다.")
    private String password;
    @NotEmpty(message ="닉네임 값은 필수사항 입니다.")
    private String userName;

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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }



}
