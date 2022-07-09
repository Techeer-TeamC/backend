package com.Techeer.Team_C.domain.user.controller;

import com.Techeer.Team_C.domain.user.dto.PasswordChangeRequestDto;
import com.Techeer.Team_C.domain.user.dto.SignupFormDto;
import com.Techeer.Team_C.domain.user.dto.UserDto;
import com.Techeer.Team_C.domain.auth.jwt.JwtTokenProvider;
import com.Techeer.Team_C.domain.user.entity.Role;
import com.Techeer.Team_C.domain.user.repository.UserRepository;
import com.Techeer.Team_C.domain.auth.service.AuthService;
import com.Techeer.Team_C.domain.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    @ApiOperation(value = "회원가입", notes = "회원가입 API")
    public ResponseEntity<Void> join(@RequestBody @Valid final SignupFormDto user) {

        UserDto member = new UserDto();
        member.setEmail(user.getEmail());
        member.setPassword(passwordEncoder.encode(user.getPassword()));
        member.setMemberName(user.getMemberName());
        member.setRole(Role.ROLE_USER);
        userService.join(member);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();


    }


    @GetMapping("/")
    @ApiOperation(value = "User 정보 조회", notes = "정보조회 API , 헤더에 토큰 정보 필요")
    public ResponseEntity<UserDto> getMyMemberInfo() {

        return ResponseEntity.ok(userService.getMyinfo().get());


    }

    @PutMapping("/")
    @ApiOperation(value = "비밀번호 변경", notes = "비밀번호 변경 API, 헤더에 토큰 정보 필요")
    public ResponseEntity<Void> changePassword(
            @RequestBody @Valid final PasswordChangeRequestDto dto) {

        userService.changePassword(dto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
