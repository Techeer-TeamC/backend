package com.Techeer.Team_C.domain.user.controller;

import com.Techeer.Team_C.domain.user.dto.SignupFormDto;
import com.Techeer.Team_C.domain.user.dto.UserDto;
import com.Techeer.Team_C.domain.auth.jwt.JwtTokenProvider;
import com.Techeer.Team_C.domain.user.repository.UserRepository;
import com.Techeer.Team_C.domain.user.entity.User;
import com.Techeer.Team_C.domain.auth.service.AuthService;
import com.Techeer.Team_C.global.error.exception.BusinessException;
import com.Techeer.Team_C.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.Optional;


import static com.Techeer.Team_C.global.error.exception.ErrorCode.*;
import static com.Techeer.Team_C.global.utils.Constants.API_PREFIX;

@RestController
@RequestMapping(API_PREFIX+"/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthService authService;

    @Autowired
    public UserController(UserService userService,PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider, UserRepository userRepository, AuthService authService){
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
        this.authService = authService;
    }

    /**
     * 회원가입
     */
    @PostMapping("/")
    public String join(@RequestBody @Valid final SignupFormDto user) {

        UserDto member = new UserDto();
        member.setUserId(user.getUserId());
        member.setPassword(passwordEncoder.encode(user.getPassword()));
        member.setUserName(user.getUserName());
        member.setRoles(Collections.singletonList("ROLE_USER"));



        userService.join(member);

        return member.getUserId();
    }



    @GetMapping("/")
    public Optional<UserDto> getMyMemberInfo() {
        return userService.getMyinfo();
    }

}
