package com.Techeer.Team_C.repository;

import com.Techeer.Team_C.domain.User;
import org.springframework.stereotype.Repository;

import java.util.*;

/*
해당 repositorys는 mysql이 아닌, 메모리기반의 repository입니다.
향후 mysql과 연결해 직접 데이터를 가져오고 저장하는 기능을 구현할 예정입니다.
 */

@Repository
public class UserMemoryRepository implements UserRepository{

    private static Map<String, User> store = new HashMap<>();  //메모리에 저장할 자료구


    @Override
    public User save(User member) {
        store.put(member.getUserId(), member);
        return member;
    }
    // user의 ID를 키 값으로 하는 member 객체를 store에 저장
    @Override
    public Optional<User> findById(String userId) {
        return Optional.ofNullable(store.get(userId));
    }
    //저장된 ID값을 가지고 member객체 return


    @Override
    public List<User> findAll() {
        return new ArrayList<>(store.values());
    }
}
