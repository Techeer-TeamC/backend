package com.Techeer.Team_C.domain.auth.service;

import com.Techeer.Team_C.domain.auth.jwt.JwtTokenProvider;
import com.Techeer.Team_C.domain.auth.entity.RefreshToken;
import com.Techeer.Team_C.domain.auth.repository.RefreshTokenRepository;
import com.Techeer.Team_C.domain.auth.dto.TokenRefreshDto;
import com.Techeer.Team_C.domain.user.dto.LoginFormDto;
import com.Techeer.Team_C.domain.auth.dto.TokenDto;
import com.Techeer.Team_C.domain.user.dto.UserDto;
import com.Techeer.Team_C.domain.user.service.UserService;
import com.Techeer.Team_C.global.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.Techeer.Team_C.global.error.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    /**
     * 로그인
     *
     * @param loginformDto 사용자가 입력한 로그인 데이터
     * @return TokenTdo accessToken, refreshToken, 토큰만료시간의 정보를 담은 데이터 반환
     */
    public TokenDto login(LoginFormDto loginformDto) {

        UserDto member = userService.findMember(loginformDto.getEmail())
                .orElseThrow(() -> new BusinessException("가입되지 않은 E-MAIL 입니다", EMAIL_NOT_FOUND));

        if (!passwordEncoder.matches(loginformDto.getPassword(), member.getPassword())) {
            throw new BusinessException("잘못된 비밀번호 입니다", INVALID_PASSWORD);
        }

        // 1. Login ID/PW 를 기반으로 AuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken = loginformDto.toAuthentication();

        // 2. 실제로 검증 (사용자 비밀번호 체크) 이 이루어지는 부분
        //    authenticate 메서드가 실행이 될 때 CustomUserDetailsService 에서 만들었던 loadUserByUsername 메서드가 실행됨
        Authentication authentication = authenticationManagerBuilder.getObject()
                .authenticate(authenticationToken);

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        TokenDto tokenDto = tokenProvider.createToken(authentication);

        // 4. RefreshToken 저장
        RefreshToken refreshToken = RefreshToken.builder().key(authentication.getName())
                .value(tokenDto.getRefreshToken()).build();

        refreshTokenRepository.save(refreshToken);

        // 5. 토큰 발급
        return tokenDto;
    }

    /**
     * refresh토큰 재발행
     *
     * @param tokenRefreshDto refresh 토큰에 대한 정보
     * @return tokenDto 재발급된 accessToken , refreshToken , 토큰만료시간을 담은 dto
     */
    public TokenDto reissue(TokenRefreshDto tokenRefreshDto) {

        Authentication authentication = authenticationValidCheck(tokenRefreshDto);

        RefreshToken refreshToken = refreshTokenValidCheck(authentication, tokenRefreshDto);

        //  새로운 토큰 생성
        TokenDto newTokenDto = tokenProvider.createToken(authentication);

        //  저장소 정보 업데이트
        RefreshToken newRefreshToken = refreshToken.updateValue(newTokenDto.getRefreshToken());
        refreshTokenRepository.save(newRefreshToken);

        // 토큰 발급
        return newTokenDto;
    }

    /**
     * db에 저장된 refrshToken 삭제
     * @param tokenRefreshDto refresh 토큰에 대한 정보
     */
    public void logout(TokenRefreshDto tokenRefreshDto) {

        Authentication authentication = tokenProvider.getAuthentication(
                tokenRefreshDto.getAccessToken());

        RefreshToken refreshToken = refreshTokenValidCheck(authentication, tokenRefreshDto);

        refreshTokenRepository.delete(refreshToken);
    }

    public Authentication authenticationValidCheck(TokenRefreshDto tokenRefreshDto) {
        // Refresh Token 검증
        if (!tokenProvider.validateToken(tokenRefreshDto.getRefreshToken())) {
            throw new BusinessException("유효하지 않은 refresh Token 입니다.", INVALID_REFRESH_TOKEN);
        }
        // Access Token 에서 Member ID 가져오기
        Authentication authentication = tokenProvider.getAuthentication(
                tokenRefreshDto.getAccessToken());

        return authentication;
    }

    public RefreshToken refreshTokenValidCheck(Authentication authentication,
            TokenRefreshDto tokenRefreshDto) {
        //  저장소에서 Member ID 를 기반으로 Refresh Token 값 가져옴
        RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName())
                .orElseThrow(
                        () -> new BusinessException("로그아웃된 사용자 입니다", MISMATCHED_USER_INFORMATION));

        // Refresh Token 일치하는지 검사
        if (!refreshToken.getValue().equals(tokenRefreshDto.getRefreshToken())) {
            throw new BusinessException("토큰과 유저정보가 서로 일치하지 않습니다.", LOGOUT_USER);
        }
        return refreshToken;
    }
}

