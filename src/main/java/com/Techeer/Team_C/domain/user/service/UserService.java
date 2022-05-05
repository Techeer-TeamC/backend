package com.Techeer.Team_C.domain.user.service;

import com.Techeer.Team_C.domain.user.dto.UserDto;
import com.Techeer.Team_C.domain.user.entity.User;
import com.Techeer.Team_C.domain.user.repository.UserRepository;

import com.Techeer.Team_C.global.error.exception.BusinessException;
import org.apache.catalina.security.SecurityUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.Techeer.Team_C.global.error.exception.ErrorCode.*;

//@Service - config/Loginconfig 를 통해 bean 설정

public class UserService {

    @Autowired
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;


    public UserService(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    private UserDto of(User user) {
        return modelMapper.map(user, UserDto.class);
        // Serice에서는 user entitiy에 바로 접근하지 않고, User entitiy를 user Dto로 변경하여 dto에 접근
    }


    /**
     * 회원가입
     *
     * @param userdto
     * @return string UserId;
     */
    public String join(UserDto userdto) {

        //중복 이메일 검사
        userRepository.findById(userdto.getId())
                .ifPresent(m -> {
                    throw new BusinessException("중복된 이메일입니다.", EMAIL_DUPLICATION);
                });

        User user = new User();
        user.setId(userdto.getId());
        user.setUserId((userdto.getUserId()));
        user.setPassword((userdto.getPassword()));
        user.setUserName((userdto.getUserName()));
        user.setRoles(userdto.getRoles());

        userRepository.save(user);
        return user.getUserId();
    }

//    private String getEncodedPassword(String password){
//        return "{noop}" + password;
//    }  db 오류 날 시 해당 코드 사용 필요

//
//    /**
//     * 전체 회원 조회
//     * @return
//     */
//    public List<UserDto> findUsers() {
//        List<User>  usersList= userRepository.findAll();
//
//        List<UserDto> userDtoList = usersList.stream().map(q -> of(q)).collect(Collectors.toList());
//        return userDtoList;
//    }

    /**
     * 특정 id값을 가지는 회원정보 조회
     *
     * @param id
     * @return
     */
    public Optional<UserDto> findMember(String userId) {

        Optional<User> userByUserId = userRepository.findByUserId(userId);
        Optional<UserDto> userDtoByUserId = userByUserId.map(q -> of(q));
        return userDtoByUserId;
    }


    public Optional<UserDto> getMyinfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == "anonymousUser"
                || authentication.getName() == null) {
            System.out.printf(authentication.getName());
            throw new BusinessException("Security Context 에 인증 정보가 없습니다", EMPTY_TOKEN_DATA);
        }

        Long userId = Long.parseLong(authentication.getName());
        Optional<User> userById = userRepository.findById(userId);
        Optional<UserDto> userDtoById = userById.map(q -> of(q));
        return userDtoById;
    }

}
