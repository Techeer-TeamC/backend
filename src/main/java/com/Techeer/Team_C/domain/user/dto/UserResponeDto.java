package com.Techeer.Team_C.domain.user.dto;

import com.Techeer.Team_C.domain.user.entity.Role;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class UserResponeDto {

    @ApiModelProperty(example = "회원 식별 Id")
    private Long userId;

    @ApiModelProperty(example = "회원 아이디(이메일)")
    private String email;

    @ApiModelProperty(example = "회원 닉네임")
    private String memberName;

    @ApiModelProperty(example = "회원의 권한 (USER / ADMIN)")
    private Role role;

}
