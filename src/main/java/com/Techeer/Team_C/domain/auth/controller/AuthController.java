package com.Techeer.Team_C.domain.auth.controller;

import com.Techeer.Team_C.domain.user.dto.LoginFormDto;
import com.Techeer.Team_C.domain.auth.dto.TokenDto;
import com.Techeer.Team_C.domain.auth.dto.TokenRefreshDto;
import com.Techeer.Team_C.domain.auth.service.AuthService;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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


    @PostMapping("/new")
    @ApiOperation(value = "로그인", notes = "로그인 API")
    public ResponseEntity<TokenDto> login(@RequestBody @Valid final LoginFormDto user) {

        return ResponseEntity.ok(authService.login(user));

    }

    @PostMapping("/reissue")
    @ApiOperation(value = "refreshToekn 재발급", notes = "refreshToken 재발급 API, , 헤더에 토큰 정보 필요")
    public ResponseEntity<TokenDto> reissue(@RequestBody TokenRefreshDto tokenRefreshDto) {

        return ResponseEntity.ok(authService.reissue(tokenRefreshDto));

    }

    @DeleteMapping("/")
    @ApiOperation(value = "로그아웃", notes = "DB에 저장된 refreshToken을 삭제 , 헤더에 토큰 정보 필요")
    public ResponseEntity<Void> logout(@RequestBody TokenRefreshDto tokenRefreshDto) {

        authService.logout(tokenRefreshDto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/kakao")
    @ApiOperation(value = "kakao 소셜 로그인", notes = "소셜로그인 API (kakao)")
    public void kakaoCallback(@RequestParam String code) {
        System.out.println(code);
    }

    @GetMapping("/google")
    @ApiOperation(value = "google 소셜 로그인", notes = "소설로그인 API (google)")
    public void googleCallback(@RequestParam String code) {
        System.out.println(code);
    }
}
