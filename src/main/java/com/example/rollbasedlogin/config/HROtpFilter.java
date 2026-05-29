package com.example.rollbasedlogin.config;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.rollbasedlogin.service.EmailService;
import com.example.rollbasedlogin.service.OtpService;

@Component
public class HROtpFilter extends OncePerRequestFilter {

    @Autowired
    private OtpService otpService;

    @Autowired
    private EmailService emailService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        filterChain.doFilter(request, response);
    }
}
