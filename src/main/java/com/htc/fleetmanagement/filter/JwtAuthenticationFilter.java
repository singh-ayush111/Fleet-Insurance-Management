package com.htc.fleetmanagement.filter;

import java.io.IOException;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.htc.fleetmanagement.entity.Token;
import com.htc.fleetmanagement.repository.TokenRepository;
import com.htc.fleetmanagement.service.impl.CustomUserDetailService;
import com.htc.fleetmanagement.service.impl.JwtService;
import io.jsonwebtoken.ExpiredJwtException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private CustomUserDetailService customUserDetailService;
    
    @Autowired
    private TokenRepository tokenRepository;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String authHeader = request.getHeader("Authorization");
            String token = null;
            String username = null;

            // Extract token from Authorization header
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                token = authHeader.substring(7);
                try {
                    username = jwtService.extractUsername(token);
                } catch (ExpiredJwtException e) {
                    // If token is expired, mark it in database
                    var tokenEntity = tokenRepository.findByJwttoken(token);
                    if (tokenEntity.isPresent()) {
                        Token t = tokenEntity.get();
                        t.setExpired(true);
                        tokenRepository.save(t);
                        LoggerFactory.getLogger(JwtAuthenticationFilter.class)
                            .info("Marked expired token as expired in database");
                    }
                    LoggerFactory.getLogger(JwtAuthenticationFilter.class)
                        .warn("JWT token is expired");
                }
            }
            
            // Check if token is valid in database (not expired or revoked)
            Boolean isTokenValid = false;
            if (token != null) {
                isTokenValid = tokenRepository.findByJwttoken(token)
                        .map(t -> !t.isExpired() && !t.isRevoked())
                        .orElse(false);
            }

            // If username is extracted and no authentication is set
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = customUserDetailService.loadUserByUsername(username);

                // Validate token signature, expiration and database status
                if (jwtService.validateToken(token, userDetails) && isTokenValid) {
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // Set authentication in SecurityContext
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    LoggerFactory.getLogger(JwtAuthenticationFilter.class).debug("User authenticated: {}", username);
                } else {
                    LoggerFactory.getLogger(JwtAuthenticationFilter.class).warn("Token validation failed for user: {}", username);
                }
            }
        } catch (Exception e) {
            LoggerFactory.getLogger(JwtAuthenticationFilter.class).error("Error in JWT processing: {}", e.getMessage(), e);
        }
        filterChain.doFilter(request, response);
    }
}
