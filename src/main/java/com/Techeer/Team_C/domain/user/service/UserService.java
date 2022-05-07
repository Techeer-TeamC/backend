package com.Techeer.Team_C.domain.user.service;

import com.Techeer.Team_C.domain.user.dto.UserDto;
import com.Techeer.Team_C.domain.user.entity.User;
import com.Techeer.Team_C.domain.user.repository.UserRepository;

import com.Techeer.Team_C.global.error.exception.BusinessException;
import javax.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.Optional;

import static com.Techeer.Team_C.global.error.exception.ErrorCode.*;


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
     * @param userdto 사용자가 입력한 회원가입 데이터
     * @return string email 가입에 성공한 email(이메일)
     */
    @Transactional
    public String join(UserDto userdto) {

        //중복 이메일 검사
        userRepository.findByEmail(userdto.getEmail())
                .ifPresent(m -> {
                    throw new BusinessException("중복된 이메일입니다.", EMAIL_DUPLICATION);
                });

        User user = new User();
        user.setEmail((userdto.getEmail()));
        user.setPassword((userdto.getPassword()));
        user.setMemberName((userdto.getMemberName()));
        user.setRoles(userdto.getRoles());

        userRepository.save(user);
        return user.getEmail();
    }


    /**
     * 특정 id값을 가지는 회원정보 조회
     *
     * @param email 조회 할 기준의 이메일값 (email)
     * @return Optional<UserDto> userDto 데이터
     */
    @Transactional
    public Optional<UserDto> findMember(String email) {

        Optional<User> userByemail = userRepository.findByEmail(email);
        Optional<UserDto> userDtoByemail = userByemail.map(q -> of(q));
        return userDtoByemail;
    }

    /**
     * header에 담긴 토큰정보를 가져와 해당 user객체의 정보 반환
     *
     * @return 해당 user객체의 데이터
     */
    @Transactional
    public Optional<UserDto> getMyinfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == "anonymousUser"
                || authentication.getName() == null) {
            System.out.printf(authentication.getName());
            throw new BusinessException("Security Context 에 인증 정보가 없습니다", EMPTY_TOKEN_DATA);
        }

        Long id = Long.parseLong(authentication.getName());
        Optional<User> userById = userRepository.findById(id);
        Optional<UserDto> userDtoById = userById.map(q -> of(q));
        return userDtoById;
    }

}
