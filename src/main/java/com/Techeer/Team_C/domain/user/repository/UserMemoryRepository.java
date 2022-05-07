package com.Techeer.Team_C.domain.user.repository;

import com.Techeer.Team_C.domain.user.entity.User;
import java.util.*;

/*
해당 repositorys는 mysql이 아닌, 메모리기반의 repository입니다.
 */


public class UserMemoryRepository implements UserRepository {

    private static long sequence = 0L;
    private static Map<Long, User> store = new HashMap<>();  //메모리에 저장할 자료구


    @Override
    public User save(User user) {
        user.setId(++sequence);
        store.put(user.getId(), user);
        return user;
    }

    // user의 ID를 키 값으로 하는 member 객체를 store에 저장
    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    //저장된 ID값을 가지고 member객체 return
    public Optional<User> findByEmail(String email) {

        Set<Long> keySet = store.keySet();
        for (Long key : keySet) {
            if (email.equals(store.get(key).getEmail())) {
                return Optional.ofNullable(store.get(key));
            }
        }
        return null;
    }

}
