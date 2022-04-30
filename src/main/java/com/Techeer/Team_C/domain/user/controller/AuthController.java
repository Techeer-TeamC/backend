package com.Techeer.Team_C.domain.user.controller;

import com.Techeer.Team_C.domain.user.auth.JwtTokenProvider;
import com.Techeer.Team_C.domain.user.dto.LoginFormDto;
import com.Techeer.Team_C.domain.user.dto.TokenDto;
import com.Techeer.Team_C.domain.user.dto.TokenRefreshDto;
import com.Techeer.Team_C.domain.user.dto.UserDto;
import com.Techeer.Team_C.domain.user.repository.UserRepository;
import com.Techeer.Team_C.domain.user.service.AuthService;
import com.Techeer.Team_C.domain.user.service.UserService;
import com.Techeer.Team_C.global.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.Techeer.Team_C.global.error.exception.ErrorCode.*;
import static com.Techeer.Team_C.global.utils.Constants.API_PREFIX;

@RestController
@RequestMapping(API_PREFIX+"/auth")
@RequiredArgsConstructor

public class AuthController {
    private final UserService userService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthService authService;

    @Autowired
    public AuthController(UserService userService,PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider, UserRepository userRepository, AuthService authService){
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
        this.authService = authService;
    }

    @PostMapping("/")
    public TokenDto login(@RequestBody @Valid final LoginFormDto user) {


        UserDto member = userService.findMember(user.getUserId())
                .orElseThrow(() -> new BusinessException("가입되지 않은 E-MAIL 입니다", EMAIL_NOT_FOUND));

        if (!passwordEncoder.matches(user.getPassword(), member.getPassword())) {
            throw new BusinessException("잘못된 비밀번호 입니다", INVALID_PASSWORD);
        }
        return authService.login(user);
    }

    @PostMapping("/reissue")
    public TokenDto reissue(@RequestBody TokenRefreshDto tokenRefreshDto) {
        return authService.reissue(tokenRefreshDto);
    }
}
