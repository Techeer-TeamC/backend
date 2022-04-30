package com.Techeer.Team_C.domain.user.repository;

import com.Techeer.Team_C.domain.user.entity.RefreshToken;
import com.Techeer.Team_C.domain.user.entity.User;
import org.springframework.stereotype.Repository;
//import org.springframework.data.jpa.repository.JpaRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
//@Repository
//public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
//    Optional<RefreshToken> findByKey(String key); //userId값으로 해당하는 토큰을 가져옵니다.
//}
@Repository
public class RefreshTokenRepository{
    private static Map<String,RefreshToken> store = new HashMap<>();  //메모리에 저장할 자료구조//
    Optional<RefreshToken> findByKey(String key){
        return Optional.ofNullable(store.get(key));
    }
    public RefreshToken save(RefreshToken token){
       store.put(token.getKey(),token);
        return token;
    }

}
