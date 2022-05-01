package com.Techeer.Team_C.domain.auth.repository;

import com.Techeer.Team_C.domain.auth.entity.RefreshToken;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/*
해당 repositorys는 mysql이 아닌, 메모리기반의 repository입니다.
향후 mysql과 연결해 직접 데이터를 가져오고 저장하는 기능을 구현할 예정입니다.
 */

@Repository
public class RefreshTokenRepository{ //userId값으로 토큰을 가져올 때 사용
    private static Map<String, RefreshToken> store = new HashMap<>();  //메모리에 저장할 자료구조//
    public Optional<RefreshToken> findByKey(String key){
        return Optional.ofNullable(store.get(key));
    }
    public RefreshToken save(RefreshToken token){
       store.put(token.getKey(),token);
        return token;
    }

}
