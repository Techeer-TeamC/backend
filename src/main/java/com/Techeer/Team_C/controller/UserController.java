package com.Techeer.Team_C.controller;

import com.Techeer.Team_C.domain.User;
import com.Techeer.Team_C.dto.UserDto;
import com.Techeer.Team_C.jwt.JwtTokenProvider;
import com.Techeer.Team_C.repository.UserRepository;
import com.Techeer.Team_C.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
//@RequestMapping("/user")
//@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public UserController(UserService memberService,PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider, UserRepository memberRepository){
        this.userService = memberService;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = memberRepository;
    }

    @PostMapping("users/login")
    public String login(@RequestBody Map<String, String> user) {


        UserDto member = userService.findMember(user.get("userId"))
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 E-MAIL 입니다."));
        if (!passwordEncoder.matches(user.get("password"), member.getPassword())) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }
        return jwtTokenProvider.createToken(member.getUsername(), member.getRoles());
    }

    @PostMapping("users/signup")
    public String join(@RequestBody Map<String, String> user) {

        User member = new User();
        member.setUserId(user.get("userId"));
        member.setPassword(passwordEncoder.encode(user.get("password")));
        member.setUserName(user.get("userName"));
        member.setRoles(Collections.singletonList("ROLE_USER"));


        try {
            userService.join(member);
        }catch(IllegalStateException e){
            e.printStackTrace();
        }
        return member.getUserId();
    }


//    @GetMapping("/all")
//    public ResponseEntity<List<User>> AllUser() {
//        return ResponseEntity.ok(userService.AllUser());
//    }


}
