package com.Techeer.Team_C.domain.user.dto;
//DB의 테이블구조와 직접적으로 연관된 entity를 사용하는것은 좋지 않기때문에 중간의 데이터 교환을 위한 Dto가 필요하다.
// entity의 속성과 동일하게 작성한다.

import lombok.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class UserDto {
    private Long id;
    private String userId;
    private String userName;
    private String password;
    private List<String> roles = new ArrayList<>();


}
