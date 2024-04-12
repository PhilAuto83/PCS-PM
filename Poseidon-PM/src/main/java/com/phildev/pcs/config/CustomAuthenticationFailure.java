package com.phildev.pcs.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;

import java.io.IOException;

/***
 * This class is used to redirect users to login page with different error parameters
 * It helps handling custom errors depending on whether this is a credentials error or session error
 */
public class CustomAuthenticationFailure implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        if(exception instanceof SessionAuthenticationException){
            response.sendRedirect("login?sessionError");
        }else{
            response.sendRedirect("login?error");
        }

    }
}
