package com.Techeer.Team_C.domain.user.repository;

import com.Techeer.Team_C.domain.user.entity.User;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface UserMysqlRepository extends JpaRepository<User, Long>, UserRepository {

    @Override
    Optional<User> findByEmail(String id);

}
