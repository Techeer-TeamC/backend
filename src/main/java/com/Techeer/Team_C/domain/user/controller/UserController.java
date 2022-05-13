package com.Techeer.Team_C.domain.user.controller;

import com.Techeer.Team_C.domain.user.dto.SignupFormDto;
import com.Techeer.Team_C.domain.user.dto.UserDto;
import com.Techeer.Team_C.domain.auth.jwt.JwtTokenProvider;
import com.Techeer.Team_C.domain.user.entity.Role;
import com.Techeer.Team_C.domain.user.repository.UserRepository;
import com.Techeer.Team_C.domain.auth.service.AuthService;
import com.Techeer.Team_C.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.Optional;

import static com.Techeer.Team_C.global.utils.Constants.API_PREFIX;

@RestController
@RequestMapping(API_PREFIX + "/users")

public class UserController {

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;


    @Autowired
    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 회원가입
     */
    @PostMapping("/new")
    public String join(@RequestBody @Valid final SignupFormDto user) {

        UserDto member = new UserDto();
        member.setEmail(user.getEmail());
        member.setPassword(passwordEncoder.encode(user.getPassword()));
        member.setMemberName(user.getMemberName());
        member.setRole(Role.ROLE_USER);

        userService.join(member);

        JSONObject obj = new JSONObject();
        obj.put("success", true);
        obj.put("status", 200);
        return obj.toString();


    }


    @GetMapping("/")
    public String getMyMemberInfo() {

        Optional<UserDto> userData = userService.getMyinfo();

        JSONObject obj = new JSONObject();
        JSONObject data = new JSONObject(userData.get().toJson());
        obj.put("success", true);
        obj.put("status", 200);
        obj.put("data", data);

        return obj.toString();


    }

}
