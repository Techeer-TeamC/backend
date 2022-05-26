package com.Techeer.Team_C.domain.auth.repository;

import com.Techeer.Team_C.domain.auth.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;


@Repository
public interface RefreshTokenRepository extends
        JpaRepository<RefreshToken, Long> { //user의 id값을 key로 token을 가져옵니다.

    Optional<RefreshToken> findByKey(String key);

}
