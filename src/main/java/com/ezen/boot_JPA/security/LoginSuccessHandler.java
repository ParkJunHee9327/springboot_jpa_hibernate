package com.ezen.boot_JPA.security;

import com.ezen.boot_JPA.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Getter
    @Setter
    private String authUrl; // 로그인 성공 시 가게 될 경로

    @Getter
    @Setter
    private String authEmail;

    // 리디렉트로 갈 때 요청에 대한 값을 어떻게 줄 것인지. 데이터를 싣고 리디렉트 해 줄 것이다. 성공 후 가야 하는 경로도 설정하는 객체다.
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
    // request 객체의 임시 저장소. 직전에 갔던 경로 등의 정보를 저장한다.
    private RequestCache requestCache = new HttpSessionRequestCache();

    @Autowired
    public UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        setAuthEmail(authentication.getName());
        setAuthUrl("/board/list");

        boolean isOk = userService.updateLastLogin(getAuthEmail());

        HttpSession httpSession = request.getSession();
        if(!isOk || httpSession == null) {
            return;
        } else {
            httpSession.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        }

        // 로그인 전의 경로를 저장한다.
        // 홈페이지 접속 직후 바로 로그인을 하면 null로 나온다. 기본 페이지는 기본적으로 할당된 경로이기에 "사용자가 다녀왔던 이전의 경로"로 보지 않는다.
        // 다른 페이지로 갔다가 기본 경로로 가고 로그인을 하면 당연히 로그인 전의 경로로 저장된다.
        SavedRequest savedRequest = requestCache.getRequest(request, response);
        log.info(" > savedRequest: {}", savedRequest);
        redirectStrategy.sendRedirect(request, response,
                (savedRequest != null ? savedRequest.getRedirectUrl() : getAuthUrl())
        );
    }
}
