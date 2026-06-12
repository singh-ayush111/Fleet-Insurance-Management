package com.htc.fleetmanagement.service.impl;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtService {
	
	@Autowired
    private CustomUserDetailService customUserDetailService;
 
	//
    @Value("${app.jwt.secret:357638792F423F4428472B4B6250655368566D597133743677397A2443264629}")
    private String secret;
 
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }
 
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
 
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
 
    // Extract roles from token
    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(getSignKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            LoggerFactory.getLogger(JwtService.class).warn("JWT token is expired: {}", token);
            throw new ExpiredJwtException(null, null, "JWT token is expired");
        } catch (Exception e) {
            LoggerFactory.getLogger(JwtService.class).error("Error parsing JWT token: {}", token, e);
            throw new RuntimeException("Invalid JWT token", e);
        }
    }
 
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
 
    // Validate token by checking username and expiration
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
 
 
    // Generate token with username and roles
    public String generateToken(String username) {
        UserDetails userDetails = customUserDetailService.loadUserByUsername(username);

        
        List<String> roles = userDetails.getAuthorities().stream()
                                       .map(authority -> authority.getAuthority())
                                       .collect(Collectors.toList());
        
        
        // Include roles as a claim in the token
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("roles", roles)  
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))  
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }
 
    // Generate token with custom claims
    private String createToken(Map<String, Object> claims, String username, long expirationInMillis) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationInMillis))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    
    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}