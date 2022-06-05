package com.Techeer.Team_C.global.utils.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaseResponseDto {
    private boolean success;
    private String status;

    public static BaseResponseDto fromEntity(String status, boolean success) {
        return BaseResponseDto.builder()
                .status(status)
                .success(success)
                .build();
    }
}
