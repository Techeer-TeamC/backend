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
    EMAIL_DUPLICATION(401, "M001", "Email is duplication"),
    LOGIN_INPUT_INVALID(401, "M002", "Login input is invalid"),
    EMAIL_NOT_FOUND(401, "M003", "Not found this email"),
    INVALID_PASSWORD(401, "M004", "Invalid password"),
    NO_PERMISSION(403, "M005", "Do not have permission."),
    USER_NOT_FOUND(401,"M006","Not found this User"),

    //jwtToken
    INVALID_JTW_TOKEN_SIGNATURE(401, "J001", "The token's signature is invalid."),
    EXPIRED_JTW_TOKEN(401, "J002", "Token data has expired"),
    UNSUPPORTED_JTW_TOKEN(401, "J003", "Unsupported token"),
    INVALID_JTW_TOKEN(401, "J004", "Jwt token is invalid"),
    EMPTY_TOKEN_DATA(401, "J005", "There is no token data in the header."),


    //refresh Token
    MISMATCHED_USER_INFORMATION(401, "J006", "Token data is not matched"),
    LOGOUT_USER(401, "J007", "Logged out user"),
    INVALID_REFRESH_TOKEN(401, "J008", "Refresh token is invalid"),

    //password Change
    NOT_DUPLICATE_PASSWORD(400,"P001", "NewPassword doesn't match re-entered password"),
    DUPLICATE_PASSWORDS(400,"P002","The new password and the old password are the same."),

    //Product
    PRODUCT_NOT_FOUND(400,"I001","Not found product"),
    Mall_NOT_FOUND(400,"I002", "Not found Mall"),

    //ProductResister
    PRODUCTREGISTER_NOT_FOUND(400, "I003", "Product not registered"),
    DUPLICATE_PRODUCTREGISTER(400, "I004", "Product already registered" )
    ;
    private final String code;
    private final String message;
    private final int status;


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
