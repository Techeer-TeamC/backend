package com.Techeer.Team_C.domain.alarm.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MailDto {

    private String address; // 메일 받을 사람
    private String title;   // 메일 타이틀
    private String content; // 내용 -> html 파일로 변경
    // 요구한 금액, 현재 금액, 물건의 이름, 물건의 링크,
}
