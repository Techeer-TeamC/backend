package com.Techeer.Team_C.domain.auth.entity;

import com.Techeer.Team_C.global.utils.dto.BaseTimeEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Table(name = "refresh_token")
@Entity
public class RefreshToken extends BaseTimeEntity {

    @Id
    @Column(name = "rt_key")    //key값은 user의 ID(Long)
    @NotNull
    private String key;

    @Column(name = "rt_value")  //token의 내용
    @NotNull
    private String value;

    @Builder
    public RefreshToken(String key, String value) {
        this.key = key;
        this.value = value;
    }


    public RefreshToken updateValue(String token) {
        this.value = token;
        return this;
    }
}
