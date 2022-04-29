package com.Techeer.Team_C.domain.user.controller;

import com.Techeer.Team_C.domain.user.dto.LoginFormDto;
import com.Techeer.Team_C.domain.user.dto.SignupFormDto;
import com.Techeer.Team_C.domain.user.dto.UserDto;
import com.Techeer.Team_C.domain.user.jwt.JwtTokenProvider;
import com.Techeer.Team_C.domain.user.repository.UserRepository;
import com.Techeer.Team_C.domain.user.entity.User;
import com.Techeer.Team_C.global.error.exception.BusinessException;
import com.Techeer.Team_C.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;


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

    @Autowired
    public UserController(UserService userService,PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider, UserRepository userRepository){
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public String login(@RequestBody @Valid final LoginFormDto user) {


        UserDto member = userService.findMember(user.getUserId())
                .orElseThrow(() -> new BusinessException("가입되지 않은 E-MAIL 입니다", EMAIL_NOT_FOUND));

        if (!passwordEncoder.matches(user.getPassword(), member.getPassword())) {
            throw new BusinessException("잘못된 비밀번호 입니다", INVALID_PASSWORD);
        }
        return jwtTokenProvider.createToken(member.getUsername(), member.getRoles());
    }

    @PostMapping("/signup")
    public String join(@RequestBody @Valid final SignupFormDto user) {

        User member = new User();
        member.setUserId(user.getUserId());
        member.setPassword(passwordEncoder.encode(user.getPassword()));
        member.setUserName(user.getUserName());
        member.setRoles(Collections.singletonList("ROLE_USER"));


        try {
            userService.join(member);
        }catch(IllegalStateException e){
            e.printStackTrace();
            throw new BusinessException("중복된 이메일입니다.", EMAIL_DUPLICATION);
        }
        return member.getUserId();
    }


//    @GetMapping("/all")
//    public List<UserDto> AllUser() {
//        return userService.findUsers();
//    }


}
