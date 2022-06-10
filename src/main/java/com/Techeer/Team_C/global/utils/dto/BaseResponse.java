package com.Techeer.Team_C.global.utils.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@Builder
@NoArgsConstructor
public class BaseResponse<T> {
    private boolean success;
    private int status;
    private T data;

    public BaseResponse(boolean success, int status, T data){
        this.success = success;
        this.status = status;
        this.data = data;
    }

    public BaseResponse(boolean success, int status){
        this.success = success;
        this.status = status;
        this.data = null;
    }

    public static BaseResponse fromEntity(int status, boolean success) {
        return BaseResponse.builder()
                .status(status)
                .success(success)
                .build();
    }
}
