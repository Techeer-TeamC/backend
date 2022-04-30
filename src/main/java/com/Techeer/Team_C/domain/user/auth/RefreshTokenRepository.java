package com.Techeer.Team_C.domain.user.auth;

import com.Techeer.Team_C.domain.user.auth.RefreshToken;
import org.springframework.stereotype.Repository;
//import org.springframework.data.jpa.repository.JpaRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class RefreshTokenRepository{ //userId값으로 토큰을 가져올 때 사용
    private static Map<String,RefreshToken> store = new HashMap<>();  //메모리에 저장할 자료구조//
    public Optional<RefreshToken> findByKey(String key){
        return Optional.ofNullable(store.get(key));
    }
    public RefreshToken save(RefreshToken token){
       store.put(token.getKey(),token);
        return token;
    }

}
