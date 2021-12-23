package com.boot.security.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 로그인 실패 시 핸들링
 */
@Slf4j
public class CustomAuthFailHandler implements AuthenticationFailureHandler {

    private final String DEFAULT_FAILURE_URL = "/login?error=true";

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        String errorMsg = null;

        log.info("[에러종류] ===> {}", exception.getClass().getName());

        if (exception instanceof BadCredentialsException || exception instanceof InternalAuthenticationServiceException) {
            errorMsg = "아이디나 비밀번호가 맞지 않습니다. 다시 확인해 주십시오.";
        } else if (exception instanceof DisabledException) {
            errorMsg = "계징이 비활성화 되었습니다. 관리자에게 문의하세요";
        } else if (exception instanceof CredentialsExpiredException) {
            errorMsg = "비밀번호 유효기간이 만료 되었습니다. 관리자에게 문의하세요";
        } else{
            errorMsg = "알수 없는 이유로 로그인에 실패하였습니다. 관리자에게 문의하세요";
        }

        request.setAttribute("errorMessage", errorMsg);
        request.getRequestDispatcher(DEFAULT_FAILURE_URL).forward(request,response);
    }
}
