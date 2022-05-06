package com.Techeer.Team_C.domain.user.dto;

import lombok.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class UserDto {

    private Long id;
    private String userId;
    private String userName;
    private String password;
    private List<String> roles = new ArrayList<>();


}
