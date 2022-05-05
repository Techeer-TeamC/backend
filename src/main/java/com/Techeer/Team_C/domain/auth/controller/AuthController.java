package com.Techeer.Team_C.domain.auth.controller;

import com.Techeer.Team_C.domain.auth.jwt.JwtTokenProvider;
import com.Techeer.Team_C.domain.user.dto.LoginFormDto;
import com.Techeer.Team_C.domain.auth.dto.TokenDto;
import com.Techeer.Team_C.domain.auth.dto.TokenRefreshDto;
import com.Techeer.Team_C.domain.user.dto.UserDto;
import com.Techeer.Team_C.domain.user.repository.UserRepository;
import com.Techeer.Team_C.domain.auth.service.AuthService;
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
@RequestMapping(API_PREFIX + "/auth")
@RequiredArgsConstructor

public class AuthController {

    private final AuthService authService;


    @PostMapping("/")
    public TokenDto login(@RequestBody @Valid final LoginFormDto user) {

        return authService.login(user);
    }

    @PostMapping("/reissue")
    public TokenDto reissue(@RequestBody TokenRefreshDto tokenRefreshDto) {
        return authService.reissue(tokenRefreshDto);
    }
}
