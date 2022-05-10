package com.Techeer.Team_C.domain.auth.jwt;

import com.Techeer.Team_C.global.error.exception.ErrorCode;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Slf4j
@Component

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException {

        String exception = String.valueOf(request.getAttribute("exception"));
        ErrorCode errorCode;

        if (exception == "null") {
            errorCode = ErrorCode.EMPTY_TOKEN_DATA;
            setResponse(response, errorCode); //Json형태로 Error값을 담음
            return;
        }

        if (exception.equals("INVALID_JTW_TOKEN_SIGNATURE")) {
            errorCode = ErrorCode.INVALID_JTW_TOKEN_SIGNATURE;
            setResponse(response, errorCode); //Json형태로 Error값을 담음
            return;
        }
        if (exception.equals("EXPIRED_JTW_TOKEN")) {
            errorCode = ErrorCode.EXPIRED_JTW_TOKEN;
            setResponse(response, errorCode);
            return;
        }
        if (exception.equals("UNSUPPORTED_JTW_TOKEN")) {
            errorCode = ErrorCode.UNSUPPORTED_JTW_TOKEN;
            setResponse(response, errorCode);
            return;
        }
        if (exception.equals("INVLAID_JTW_TOKEN")) {
            errorCode = ErrorCode.INVLAID_JTW_TOKEN;
            setResponse(response, errorCode);
            return;
        }


    }

    private void setResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        JSONObject json = new JSONObject();
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("utf-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        json.put("code", errorCode.getCode());
        json.put("message", errorCode.getMessage());
        response.getWriter().print(json);
    }


}
