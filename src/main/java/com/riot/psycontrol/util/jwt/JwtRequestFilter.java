package com.riot.psycontrol.util.jwt;

import com.riot.psycontrol.util.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String token = null;
        //Get Authorization header
        String bearerToken = httpServletRequest.getHeader("Authorization");
        //Look for Bearer token into Authorization Header if exists
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            //Extract JWT without Bearer prefix
            token = bearerToken.substring(7);
        }

        try {
            //If token exists and is valid, extract authentication object (UserDetails) and set in security context
            if (token != null && jwtProvider.validateToken(token)) {
                Authentication auth = jwtProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        } catch (CustomException ex) {
            //this is very important, since it guarantees the user is not authenticated at all
            SecurityContextHolder.clearContext();
            httpServletResponse.sendError(ex.getHttpStatus().value(), ex.getMessage());
            return;
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
