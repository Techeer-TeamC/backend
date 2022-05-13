package com.Techeer.Team_C.domain.user.dto;

import com.Techeer.Team_C.domain.user.entity.Role;
import lombok.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONObject;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class UserDto {

    private Long id;
    private String email;
    private String memberName;
    private String password;
    private Role role;

    public JSONObject toJson() {
        JSONObject obj = new JSONObject();
        obj.put("id", id);
        obj.put("email", email);
        obj.put("memberName", memberName);
        obj.put("password", password);
        obj.put("Role", role);

        return obj;
    }
}
