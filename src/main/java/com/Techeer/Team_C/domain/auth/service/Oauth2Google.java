package com.Techeer.Team_C.domain.auth.service;

import com.Techeer.Team_C.domain.auth.entity.AuthorizationGoogle;
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
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static com.Techeer.Team_C.global.error.exception.ErrorCode.ACCESS_TOKEN_NOT_FOUND;
import static com.Techeer.Team_C.global.error.exception.ErrorCode.SOCIAL_LOGIN_USER_NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
public class Oauth2Google {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String GOOGLE_SNS_CLIENT_ID;

    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String GOOGLE_SNS_CALLBACK_URL;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String GOOGLE_SNS_CLIENT_SECRET;

    public AuthorizationGoogle getAccessTokenByCode(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("client_id", GOOGLE_SNS_CLIENT_ID);
        params.add("client_secret", GOOGLE_SNS_CLIENT_SECRET);
        params.add("redirect_uri", GOOGLE_SNS_CALLBACK_URL);
        params.add("grant_type", "authorization_code");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        String url = "https://oauth2.googleapis.com/token";
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
            AuthorizationGoogle authorization = objectMapper.readValue(response.getBody(), AuthorizationGoogle.class);
            return authorization;
        } catch (RestClientException | JsonProcessingException ex) {
            ex.printStackTrace();
            throw new BusinessException("소셜로그인 접근 오류", ACCESS_TOKEN_NOT_FOUND);
        }
    }

    public User getUserByAccessToken(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);

        String url = "https://www.googleapis.com/oauth2/v1/userinfo";
        Optional<User> userByEmail = null;
        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);
            String userInfo = response.getBody();

            JsonNode jsonNode = objectMapper.readTree(userInfo);
            String email = String.valueOf(jsonNode.get("email"));
            String name = String.valueOf(jsonNode.get("name"));

            userByEmail = userRepository.findByEmail(email);
            if (!userByEmail.isPresent()) {
                User user = new User();
                user.setEmail(email.substring(1, email.length() - 1));
                user.setMemberName(name.substring(1, name.length() - 1));
                user.setRole(Role.ROLE_USER);
                user.setActivated(true);

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
