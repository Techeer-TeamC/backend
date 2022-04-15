package com.Techeer.Team_C.service;

import com.Techeer.Team_C.domain.User;
import com.Techeer.Team_C.dto.UserDto;
import com.Techeer.Team_C.repository.UserRepository;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

//@Service - config/Loginconfig 를 통해 bean 설정

public class UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public UserService(UserRepository userRepository, ModelMapper modelMapper){
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    private UserDto of(User user){
        return modelMapper.map(user,UserDto.class);
        // Serice에서는 user entitiy에 바로 접근하지 않고, User entitiy를 user Dto로 변경하여 dto에 접근
    }


    /**
     * 회원가입
     * @param user
     * @return string UserId;
     */
    public String join(User user) {
        duplicateIDCheck(user);


        userRepository.save(user);
        return user.getUserId();
    }

//    private String getEncodedPassword(String password){
//        return "{noop}" + password;
//    }  db 오류 날 시 해당 코드 사용 필요


    /**
     * 중복 ID체크
     * @param user
     */
    private void duplicateIDCheck(User user) {
        userRepository.findById(user.getUserId())
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 가입된 아이디 입니다.");
                });
    }

    /**
     * 전체 회원 조회
     * @return
     */
    public List<UserDto> findUsers() {
        List<User>  usersList= userRepository.findAll();

        List<UserDto> userDtoList = usersList.stream().map(q -> of(q)).collect(Collectors.toList());
        return userDtoList;
    }

    /**
     * 특정 id값을 가지는 회원정보 조회
     * @param id
     * @return
     */
    public Optional<UserDto> findMember(String id){

        Optional<User> userById =  userRepository.findById(id);
        Optional<UserDto> userDtoById = userById.map(q -> of(q));
        return userDtoById;
    }
}
