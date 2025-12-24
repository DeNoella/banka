package com.example.demo.config;

import org.springframework.stereotype.Component;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Order(1)
public class RequestLoggingFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, jakarta.servlet.ServletException {
        System.out.println("Incoming " + request.getMethod() + " " + request.getRequestURI());
        System.out.println("Origin: " + request.getHeader("Origin"));
        System.out.println("AC-Request-Method: " + request.getHeader("Access-Control-Request-Method"));
        System.out.println("Authorization header present?: " + (request.getHeader("Authorization") != null));
        filterChain.doFilter(request, response);
    }
}