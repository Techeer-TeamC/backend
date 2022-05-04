package com.Techeer.Team_C.service;

import com.Techeer.Team_C.domain.User;
import com.Techeer.Team_C.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> AllUser() {
        return userRepository.findAll();
    }
}
