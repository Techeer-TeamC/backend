package com.Techeer.Team_C.domain.user.repository;



import com.Techeer.Team_C.domain.user.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    User save(User user);  //회원 저장
    Optional<User> findById(String userid);  //회원의 userId 값으로 정보 찾기
    List<User> findAll();  //모든 회원의 정보 찾기
}
