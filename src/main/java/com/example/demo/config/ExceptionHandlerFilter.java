package com.example.demo.config;

import com.example.demo.model.enums.Status;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class ExceptionHandlerFilter extends OncePerRequestFilter {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            sendErrorResponse(response, e);
        }
    }

    public void sendErrorResponse(HttpServletResponse response, Throwable ex) {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

        AuthenticationErrorResponse aer = null;
        if (ex instanceof ExpiredJwtException) {
            aer = new AuthenticationErrorResponse(Status.DECLINED, "Token Expired", 0);
        } else if (ex instanceof IllegalArgumentException) {
            aer = new AuthenticationErrorResponse(Status.DECLINED, "Token is required", null);
        } else {
            aer = new AuthenticationErrorResponse(Status.DECLINED, "Error during authentication.", null);
        }

        try {
            response.getWriter().write(objectMapper.writeValueAsString(aer));
        } catch (IOException e) {
            log.error("Error during AuthenticationErrorResponse JSON serialization", e);
        }
    }
}