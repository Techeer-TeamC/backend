package com.Techeer.Team_C.domain.auth.controller;

import com.Techeer.Team_C.domain.user.dto.LoginFormDto;
import com.Techeer.Team_C.domain.auth.dto.TokenDto;
import com.Techeer.Team_C.domain.auth.dto.TokenRefreshDto;
import com.Techeer.Team_C.domain.auth.service.AuthService;

import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.Techeer.Team_C.global.utils.Constants.API_PREFIX;

@RestController
@RequestMapping(API_PREFIX + "/auth")
@RequiredArgsConstructor

public class AuthController {

    private final AuthService authService;


    @PostMapping("/")
    public String login(@RequestBody @Valid final LoginFormDto user) {

        TokenDto tokenData = authService.login(user);

        JSONObject obj = new JSONObject();
        JSONObject data = new JSONObject(tokenData.toJson());
        obj.put("data", data);
        obj.put("success", true);
        obj.put("status", 200);

        return obj.toString();
    }

    @PostMapping("/reissue")
    public String reissue(@RequestBody TokenRefreshDto tokenRefreshDto) {

        TokenDto tokenData = authService.reissue(tokenRefreshDto);

        JSONObject obj = new JSONObject();
        JSONObject data = new JSONObject(tokenData.toJson());
        obj.put("data", data);
        obj.put("success", true);
        obj.put("status", 200);

        return obj.toString();
    }

    @DeleteMapping("/")
    public String logout(@RequestBody TokenRefreshDto tokenRefreshDto) {

        authService.logout(tokenRefreshDto);
        JSONObject obj = new JSONObject();

        obj.put("success", true);
        obj.put("status", 200);

        return obj.toString();
    }
}
