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

@Service

public class UserService {

    private final UserRepository memberRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public UserService(UserRepository memberRepository, ModelMapper modelMapper){
        this.memberRepository = memberRepository;
        this.modelMapper = modelMapper;
    }

    private UserDto of(User member){
        return modelMapper.map(member,UserDto.class);  //User entitiy를 user Dto로 변경
    }


    /**
     * 회원가입
     * @param member
     * @return string UserId;
     */
    public String join(User member) {
        duplicateIDCheck(member);

        //memberRepository.save(member);
        // member.setPassword(getEncodedPassword(member.getPassword()));
        memberRepository.save(member);
        return member.getUserId();
    }

    private String getEncodedPassword(String password){
        return "{noop}" + password;
    }


    /**
     * 중복 ID체크
     * @param member
     */
    private void duplicateIDCheck(User member) {
        memberRepository.findById(member.getUserId())
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 가입된 아이디 입니다."); //optional로 감싸서 다음과 같은 문법 사용가능.
                });
    }

    /**
     * 전체 회원 조회
     * @return
     */
    public List<UserDto> findMembers() {
        List<User>  membersList= memberRepository.findAll();

        List<UserDto> memberDtoList = membersList.stream().map(q -> of(q)).collect(Collectors.toList());
        return memberDtoList;
    }

    /**
     * 특정 id값을 가지는 회원정보 조회
     * @param id
     * @return
     */
    public Optional<UserDto> findMember(String id){

        Optional<User> memberById =  memberRepository.findById(id);
        Optional<UserDto> memberDtoById = memberById.map(q -> of(q));
        return memberDtoById;

    }
}
