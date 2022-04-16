package com.Techeer.Team_C.domain.user.entity;

//import com.vladmihalcea.hibernate.type.array.IntArrayType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
//import org.hibernate.annotations.TypeDef;

//import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
//JTW와 관련된 모듈

@Getter
@Setter
@NoArgsConstructor
//@Table(name = "test")
//@TypeDef(name = "int-array", typeClass = IntArrayType.class)
//@Entity
public class User implements UserDetails{
    // 추후 mysql의 schema에 따른 property 맞추기
//    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userId;
    private String userName;
    private String password;
    private List<String> roles = new ArrayList<>(); //User_Role or Admin Role

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
  

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getPassword(){
        return password;
    }
}
