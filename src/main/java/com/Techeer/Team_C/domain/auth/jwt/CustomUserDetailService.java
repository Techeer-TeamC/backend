package com.Techeer.Team_C.domain.auth.jwt;


import static com.Techeer.Team_C.global.error.exception.ErrorCode.EMAIL_NOT_FOUND;

import com.Techeer.Team_C.domain.user.repository.UserRepository;
import com.Techeer.Team_C.global.error.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository memberRepository;

    @Autowired
    public CustomUserDetailService(UserRepository memberRepository) {
        this.memberRepository = memberRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException("사용자를 찾을 수 없습니다.", EMAIL_NOT_FOUND));
    }
}
