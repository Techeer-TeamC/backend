package com.Techeer.Team_C.domain.auth.dto;

import com.Techeer.Team_C.domain.user.entity.User;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
public class SessionUser implements Serializable {
    private String name;
    private String email;

    public SessionUser(User user) {
        this.name = user.getMemberName();
        this.email = user.getEmail();
    }
}
