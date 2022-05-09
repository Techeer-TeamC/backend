package com.Techeer.Team_C.global.config;

import com.Techeer.Team_C.domain.auth.jwt.CustomAuthenticationEntryPoint;
import com.Techeer.Team_C.domain.auth.jwt.JwtAuthenticationFilter;
import com.Techeer.Team_C.domain.auth.jwt.JwtTokenProvider;
import com.Techeer.Team_C.domain.auth.service.CustomOAuth2UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity

public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public WebSecurityConfig(JwtTokenProvider jwtTokenProvider){
        this.jwtTokenProvider=jwtTokenProvider;
    }
    // 암호화에 필요한 PasswordEncoder 를 Bean 등록합니다.

    @Bean
    public PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder();}



    // authenticationManager를 Bean 등록합니다.
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http

                .httpBasic().disable() // rest api 만을 고려하여 기본 설정은 해제하겠습니다.
                .csrf().disable() // csrf 보안 토큰 disable처리.
                //.anonymous().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 토큰 기반 인증이므로 세션 역시 사용하지 않습니다.
                .and()
                .authorizeRequests() // 요청에 대한 사용권한 체크
                //.antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/api/v1/user/login").hasRole("USER")
                .antMatchers("/", "/css/**", "/images/**", "/js/**", "/h2-console/**").permitAll()
                //.antMatchers("/api/v1/**").hasRole("USER") // /api/v1/** 은 USER권한만 접근 가능
                .anyRequest().permitAll() // 그외 나머지 요청은 누구나 접근 가능
                .and()
                .logout()
                .logoutSuccessUrl("/api/v1")
                .and()
                .oauth2Login()
                .defaultSuccessUrl("/api/v1")
                .userInfoEndpoint()
                .userService(customOAuth2UserService)
                .and().and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider),
                        UsernamePasswordAuthenticationFilter.class);
        // JwtAuthenticationFilter를 UsernamePasswordAuthenticationFilter 전에 넣는다
    }
}
