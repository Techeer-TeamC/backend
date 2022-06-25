package com.Techeer.Team_C.domain.user.repository;


import com.Techeer.Team_C.domain.user.entity.User;


import java.util.Optional;

public interface UserRepository {

    User save(User user);  //회원 저장

    Optional<User> findById(Long Userid);  //회원의 email 값으로 정보 찾기

    Optional<User> findByEmail(String email);
}
