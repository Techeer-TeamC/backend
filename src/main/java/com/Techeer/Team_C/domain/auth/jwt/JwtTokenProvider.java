package com.Techeer.Team_C.domain.auth.jwt;

import com.Techeer.Team_C.domain.auth.dto.TokenDto;
import com.Techeer.Team_C.domain.user.entity.Role;
import io.jsonwebtoken.*;

import javax.servlet.ServletRequest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import java.util.*;
import java.util.stream.Collectors;

import static com.Techeer.Team_C.global.error.exception.ErrorCode.*;

@Slf4j
@Component
public class JwtTokenProvider {

    private static final String AUTHORITIES_KEY = "auth";
    private static final String BEARER_TYPE = "bearer";
    @Value("${jwt.secret}")   //.yml파일을 통해 secret키 관리
    private String secretKey;

    private long tokenValidTime = 30 * 60 * 1000L; //토큰 유효시간 30분
    private long refreshTokenValidTime = 7 * 24 * 60 * 60 * 1000L; // refresh 유효시간 : 7일

    // 객체 초기화, secretKey를 Base64로 인코딩한다.
    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder()
                .encodeToString(secretKey.getBytes());
    }

    // JWT 토큰 생성

    /**
     * JWT토큰 생성
     *
     * @param authentication header의 토큰으로부터 가져온 권한정보
     * @return TokenDto
     */
    public TokenDto createToken(Authentication authentication) {
        // 권한들 가져오기
        String authorities = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();

        // Access Token 생성
        Date accessTokenExpiresIn = new Date(now + tokenValidTime);
        String accessToken = Jwts.builder()
                .setSubject(authentication.getName())       // payload "sub": "name"
                .claim(AUTHORITIES_KEY, authorities)        // payload "auth": "ROLE_USER"
                .setExpiration(accessTokenExpiresIn)        // payload "exp": 1516239022 (예시)
                .signWith(SignatureAlgorithm.HS256, secretKey)   // header "alg": "HS512"
                .compact();

        // Refresh Token 생성
        String refreshToken = Jwts.builder()
                .setExpiration(new Date(now + refreshTokenValidTime))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        return TokenDto.builder()
                .grantType(BEARER_TYPE)
                .accessToken(accessToken)
                .accessTokenExpiresIn(accessTokenExpiresIn.getTime())
                .refreshToken(refreshToken)
                .build();
    }

    public TokenDto createTokenSocialLogin(Long userId) {

        Role authority = Role.ROLE_USER;
        long now = (new Date()).getTime();

        // Access Token 생성
        Date accessTokenExpiresIn = new Date(now + tokenValidTime);
        String accessToken = Jwts.builder()
                .setSubject(String.valueOf(userId))       // payload "sub": "name"
                .claim(AUTHORITIES_KEY, authority)        // payload "auth": "ROLE_USER"
                .setExpiration(accessTokenExpiresIn)        // payload "exp": 1516239022 (예시)
                .signWith(SignatureAlgorithm.HS256, secretKey)   // header "alg": "HS512"
                .compact();

        // Refresh Token 생성
        String refreshToken = Jwts.builder()
                .setExpiration(new Date(now + refreshTokenValidTime))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        return TokenDto.builder()
                .grantType(BEARER_TYPE)
                .accessToken(accessToken)
                .accessTokenExpiresIn(accessTokenExpiresIn.getTime())
                .refreshToken(refreshToken)
                .build();
    }

    public Authentication getAuthentication(String accessToken) {
        // 토큰 복호화
        Claims claims = parseClaims(accessToken);

        if (claims.get(AUTHORITIES_KEY) == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        // 클레임에서 권한 정보 가져오기
        Collection<? extends GrantedAuthority> authorities = Arrays.stream(claims.get(AUTHORITIES_KEY)
                        .toString()
                        .split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        // UserDetails 객체를 만들어서 Authentication 리턴
        UserDetails principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    //토큰 복호화 함수
    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(accessToken)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    // 토큰의 유효성 + 만료일자 확인
    public boolean validateToken(ServletRequest request, String jwtToken) {
        try {
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(jwtToken);
            return !claims.getBody()
                    .getExpiration()
                    .before(new Date());
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
            request.setAttribute("exception", INVALID_JTW_TOKEN_SIGNATURE);

        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
            request.setAttribute("exception", EXPIRED_JTW_TOKEN);


        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
            request.setAttribute("exception", UNSUPPORTED_JTW_TOKEN);


        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
            request.setAttribute("exception", INVALID_JTW_TOKEN);

        }
        return false;

    }

    public boolean validateToken(String jwtToken) {
        try {
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(jwtToken);
            return !claims.getBody()
                    .getExpiration()
                    .before(new Date());
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
        } catch (SignatureException e) {
            log.info("기존의 JWT 토큰을 확인할 수 없습니다..");
        }
        return false;
    }


}
