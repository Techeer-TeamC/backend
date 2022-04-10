package com.Techeer.Team_C.domain;

//import com.vladmihalcea.hibernate.type.array.IntArrayType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
//import org.hibernate.annotations.TypeDef;

//import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
//@Table(name = "test")
//@TypeDef(name = "int-array", typeClass = IntArrayType.class)
//@Entity
public class User {
    // 추후 mysql의 schema에 따른 property 맞추기
//    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @Builder
    public User(String name) {
        this.name = name;
    }
}
