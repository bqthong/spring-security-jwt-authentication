package com.spring.app.security.jwt;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JwtTokenProvider {
    private final static Logger LOGGER = LoggerFactory.getLogger(JwtTokenProvider.class);
    private static final String JWT_SECRET_KEY = "JWTSecretKey";
    private static final Integer EXPIRY_TIME = 68400000;
    
    public String generateToken(Authentication authentication) {
        UserDetails authenticatedUser = (UserDetails) authentication.getPrincipal();
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + EXPIRY_TIME); 
        String username = authenticatedUser.getUsername();
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET_KEY)
                .compact();
    }
    
    public String getUsernameFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(JWT_SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }
    
    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(JWT_SECRET_KEY).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            LOGGER.error("Invalid JWT signature");
        } catch (MalformedJwtException e) {
            LOGGER.error("Invalid JWT token");
        } catch (ExpiredJwtException e) {
            LOGGER.error("Expired JWT token");
        } catch (UnsupportedJwtException e) {
            LOGGER.error("Unsupported JWT token");
        } catch (IllegalArgumentException e) {
            LOGGER.error("JWT claims string is empty.");
        }
        return false;
    }
}
