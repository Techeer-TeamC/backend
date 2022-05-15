package com.Techeer.Team_C.domain.user.service;

import com.Techeer.Team_C.domain.user.dto.PasswordChangeRequestDto;
import com.Techeer.Team_C.domain.user.dto.UserDto;
import com.Techeer.Team_C.domain.user.entity.User;
import com.Techeer.Team_C.domain.user.repository.UserRepository;

import com.Techeer.Team_C.global.error.exception.BusinessException;

import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import org.springframework.security.crypto.password.PasswordEncoder;


import static com.Techeer.Team_C.global.error.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class UserService {


    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;


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
        user.setRole(userdto.getRole());
        user.setActivated(true);

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
        if (authentication == null || authentication.getName() == "anonymousUser1"
                || authentication.getName() == null) {
            System.out.printf(authentication.getName());
            throw new BusinessException("Security Context 에 인증 정보가 없습니다", EMPTY_TOKEN_DATA);
        }

        Long id = Long.parseLong(authentication.getName());
        Optional<User> userById = userRepository.findById(id);
        Optional<UserDto> userDtoById = userById.map(q -> of(q));
        return userDtoById;
    }

    public void changePassword(PasswordChangeRequestDto formData) {
        if (!formData.getNewPassword()
                .equals(formData.getReNewPassword())) {  // 1. 새로운 비밀번호의 재입력값이 입력한 새 비밀번호와 일치 검사
            throw new BusinessException("새 비밀번호의 값과 재 입력한 비밀번호의 값이 일치하지 않습니다.",
                    NOT_DUPLICATE_PASSWORD);
        }

        if (formData.getNewPassword()
                .equals(formData.getOldPassword())) { // 2. 변경할 비밀번호가 새 비밀번호과 같은지 검사
            throw new BusinessException("새 비밀번호의 값은 이전 비밀번호와 다르게 입력해 주세요.", DUPLICATE_PASSWORDS);
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long id = Long.parseLong(authentication.getName());

        User user = userRepository.findById(id).get();
        String oldPassword = user.getPassword(); //기존의 비밀번호

        if (passwordEncoder.matches(formData.getNewPassword(),
                oldPassword)) {   // 3. 기존의 입력 비밀번호가 적합한지 검사
            throw new BusinessException("기존의 비밀번호가 잘못되었습니다.", INVALID_PASSWORD);
        }

        user.setPassword(passwordEncoder.encode(formData.getNewPassword()));
        userRepository.save(user);

    }

}
