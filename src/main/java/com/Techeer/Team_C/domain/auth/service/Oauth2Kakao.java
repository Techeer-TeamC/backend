package com.Techeer.Team_C.domain.auth.service;

import com.Techeer.Team_C.domain.auth.entity.AuthorizationKakao;
import com.Techeer.Team_C.domain.user.entity.Role;
import com.Techeer.Team_C.domain.user.entity.User;
import com.Techeer.Team_C.domain.user.repository.UserRepository;
import com.Techeer.Team_C.global.error.exception.BusinessException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static com.Techeer.Team_C.global.error.exception.ErrorCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class Oauth2Kakao {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;

    @Value("${spring.security.oauth2.client.registration.kakao.authorization-grant-type}")
    private String grantType;

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String kakaoOauth2ClientId;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String redirectUrl;

    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    private String clientSecret;

    public AuthorizationKakao getAccessTokenByCode(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", grantType);
        params.add("client_id", kakaoOauth2ClientId);
        params.add("redirect_uri", redirectUrl);
        params.add("code", code);
        params.add("client_secret", clientSecret);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        String url = "https://kauth.kakao.com/oauth/token";
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

            AuthorizationKakao authorization = objectMapper.readValue(response.getBody(), AuthorizationKakao.class);

            return authorization;
        } catch (RestClientException | JsonProcessingException ex) {
            ex.printStackTrace();
            throw new BusinessException("소셜로그인 접근 오류", ACCESS_TOKEN_NOT_FOUND);
        }
    }

    /**
     * accessToken 을 이용한 유저정보 받기
     *
     * @return
     */
    public User getUserByAccessToken(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        String url = "https://kapi.kakao.com/v2/user/me";
        Optional<User> userByEmail = null;
        try {
            String userInfo = restTemplate.postForObject(url, request, String.class);

            JsonNode jsonNode = objectMapper.readTree(userInfo);
            String rawEmail = String.valueOf(jsonNode.get("kakao_account")
                    .get("email"));
            String rawName = String.valueOf(jsonNode.get("kakao_account")
                    .get("profile")
                    .get("nickname"));

            String email = rawEmail.substring(1, rawEmail.length() - 1);
            String name = rawName.substring(1, rawName.length() - 1);

            userByEmail = userRepository.findByEmail(email);
            if (!userByEmail.isPresent()) {
                User user = User.builder()
                        .email(email)
                        .memberName(name)
                        .role(Role.ROLE_USER)
                        .activated(true)
                        .build();

                return userRepository.save(user);
            }
            return userByEmail.get();
        } catch (RestClientException ex) {
            ex.printStackTrace();
            throw new BusinessException("Social login user not found", SOCIAL_LOGIN_USER_NOT_FOUND);
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return userByEmail.get();
    }
}
