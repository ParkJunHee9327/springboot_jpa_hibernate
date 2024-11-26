package com.ezen.boot_JPA.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;

@Slf4j
@Component
public class LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String errorMessage;
        if(exception instanceof BadCredentialsException) {
            errorMessage = "Id or password is incorrect. Please try again.";
        } else if (exception instanceof InternalAuthenticationServiceException) {
            errorMessage = "Can't process login due to internal system issue. Please contact with managers.";
        } else if (exception instanceof UsernameNotFoundException) {
            errorMessage = "Please check your account and try again.";
        } else if (exception instanceof AuthenticationCredentialsNotFoundException) {
            errorMessage = "Authentication request denied. Please contact with managers.";
        } else {
            errorMessage = "Please contact with managers.";
        }

        errorMessage = URLEncoder.encode(errorMessage, "UTF-8");
        setDefaultFailureUrl("/user/login?error=true&exception=" + errorMessage);
        super.onAuthenticationFailure(request, response, exception);
    }
}
