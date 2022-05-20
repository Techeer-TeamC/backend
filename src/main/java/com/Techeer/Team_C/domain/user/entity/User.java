package com.Techeer.Team_C.domain.user.entity;

//import com.Techeer.Team_C.domain.product.entity.ProductRegisterId;
import com.Techeer.Team_C.global.utils.dto.BaseTimeEntity;
import com.Techeer.Team_C.global.utils.dto.BooleanToYNConverter;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;

import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;

import lombok.Setter;


import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.lang.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
//JWT와 관련된 모듈

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "User")
@EntityListeners(AuditingEntityListener.class)
@Entity
public class User extends BaseTimeEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    private Long id;

    @Column(unique = true)
    @NotNull
    private String email;

    @NotNull
    private String memberName;

    @Nullable
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @NotNull
    @Convert(converter = BooleanToYNConverter.class)
    private boolean activated;

   // private ProductRegisterId product_registerId;

    @Override
    public String getUsername() {
        return id.toString();
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
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(role.toString()));
        return authorities;
    }

    public User update(String memberName) {
        this.memberName = memberName;

        return this;
    }
}
