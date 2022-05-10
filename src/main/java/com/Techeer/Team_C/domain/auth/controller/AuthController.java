package com.Techeer.Team_C.domain.auth.controller;

import com.Techeer.Team_C.domain.user.dto.LoginFormDto;
import com.Techeer.Team_C.domain.auth.dto.TokenDto;
import com.Techeer.Team_C.domain.auth.dto.TokenRefreshDto;
import com.Techeer.Team_C.domain.auth.service.AuthService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

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

    @GetMapping("/kakao")
    public void kakaoCallback(@RequestParam String code) {
        System.out.println(code);
    }

    @GetMapping("/google")
    public void googleCallback(@RequestParam String code) {
        System.out.println(code);
    }
}
