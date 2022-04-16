package com.Techeer.Team_C.controller;

import com.Techeer.Team_C.domain.User;
import com.Techeer.Team_C.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController // JSON 형태 결과값을 반환해줌 (@ResponseBody가 필요없음)
@RequestMapping("/user")
@RequiredArgsConstructor// final 객체를 Constructor Injection 해줌. (Autowired 역할)
public class UserController {

    private final UserService userService;

    @GetMapping("/all")
    public ResponseEntity<List<User>> AllUser() {
        return ResponseEntity.ok(userService.AllUser());
    }
}
