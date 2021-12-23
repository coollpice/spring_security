package com.boot.security.config;

import com.boot.security.model.ERole;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Set;

/**
 * 로그인 성공 시 핸들링
 */
@Slf4j
public class CustomAuthSuccessHandler implements AuthenticationSuccessHandler {

    private final String DEFAULT_LOGIN_SUCCESS_URL = "/home";
    private RequestCache requestCache = new HttpSessionRequestCache();
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        clearAuthenticationAttributes(request);
        redirectStrategy(request, response, authentication);
    }

    private void clearAuthenticationAttributes(HttpServletRequest request) {

        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        }
    }

    /**
     * 인증이 필요한 페이지 접근 시 미인증 상태여서 로그인 페이지로 이동된 경우
     * 로그인한 사용자의 접근권한에 따른 페이지로 이동한다.
     */
    private void redirectStrategy(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        SavedRequest savedRequest = requestCache.getRequest(request, response);
        if (savedRequest == null) {
            redirectStrategy.sendRedirect(request, response, DEFAULT_LOGIN_SUCCESS_URL);
        } else {
            log.info("[호출전 URL] ===> {}" , savedRequest.getRedirectUrl());
            Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
            if (roles.contains(ERole.ADMIN.getValue())) {
                redirectStrategy.sendRedirect(request , response , savedRequest.getRedirectUrl());
            } else if (roles.contains(ERole.MANAGER.getValue())) {
                redirectStrategy.sendRedirect(request, response, savedRequest.getRedirectUrl());
            }else{
                redirectStrategy.sendRedirect(request, response, savedRequest.getRedirectUrl());
            }
        }
    }
}
