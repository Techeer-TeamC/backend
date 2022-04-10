package com.Techeer.Team_C.controller;

import com.Techeer.Team_C.domain.User;
import com.Techeer.Team_C.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    @Autowired
    private final UserService userService;

    @GetMapping("/all")
    public ResponseEntity<List<User>> AllUser() {
        return ResponseEntity.ok(userService.AllUser());
    }
}
