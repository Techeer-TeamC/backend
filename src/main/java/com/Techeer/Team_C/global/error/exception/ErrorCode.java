package com.Techeer.Team_C.global.error.exception;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ErrorCode {

    // Common
    INVALID_INPUT_VALUE(400, "C001", " Invalid Input Value"),
    METHOD_NOT_ALLOWED(405, "C002", " Invalid Input Value"),
    ENTITY_NOT_FOUND(400, "C003", " Entity Not Found"),
    INTERNAL_SERVER_ERROR(500, "C004", "Server Error"),
    INVALID_TYPE_VALUE(400, "C005", " Invalid Type Value"),
    HANDLE_ACCESS_DENIED(403, "C006", "Access is Denied"),

    // Member
    EMAIL_DUPLICATION(400, "M001", "Email is Duplication"),
    LOGIN_INPUT_INVALID(400, "M002", "Login input is invalid"),
    EMAIL_NOT_FOUND(400, "M003", "Not found this email"),
    INVALID_PASSWORD(400, "M004", "Invalid password"),

    //jwtToken
    INVALID_JTW_TOKEN_SIGNATURE(400,"J001", "The token's signature is invalid."),
    EXPIRED_JTW_TOKEN(400, "J002","Token data has expired"),
    UNSUPPORTED_JTW_TOKEN(400,"J003" , "Unsupported token"),
    INVLAID_JTW_TOKEN(400,"J004", "Jwt token is invalid"),
    EMPTY_TOKEN_DATA(400,"J005","There is no token data in the header."),



    //refresh Token
    INVALID_REFRESH_TOKEN(400, "J005" , "Refresh token is invalid"),
    MISMATCHED_USER_INFORMATION(400, "J006", "Token data is not matched"),
    LOGOUT_USER(400, "J007", "Logged out user"),



    ;
    private final String code;
    private final String message;
    private int status;

    ErrorCode(final int status, final String code, final String message) {
        this.status = status;
        this.message = message;
        this.code = code;
    }

    public String getMessage() {
        return this.message;
    }

    public String getCode() {
        return code;
    }

    public int getStatus() {
        return status;
    }


}