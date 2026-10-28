package org.example.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtService {

    public static final String ACCESS = "access";
    public static final String REFRESH = "refresh";
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.access-expiration-ms}")
    private Long accessExpirationMs;

    @Value("${jwt.refresh-expiration-ms}")
    private Long refreshExpirationMs;

    private SecretKey getSigningKey(){
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateAccessToken(UserDetails userDetails){
        String role = userDetails.getAuthorities().stream().findFirst()
                .map(a->a.getAuthority())
                .orElseThrow();
        return Jwts.builder()
                .subject(userDetails.getUsername())          // the email
                .claim("type", ACCESS)
                .claim("role", role)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + accessExpirationMs))
                .signWith(getSigningKey())
                .compact();
    }

    public String generateRefreshToken(UserDetails userDetails){
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .claim("type", REFRESH)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()+refreshExpirationMs))
                .signWith(getSigningKey())
                .compact();
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String extractEmail(String token){
        return extractAllClaims(token).getSubject();
    }

    public boolean isTokenValid(String token, UserDetails user, String expectedType) {
        try {
            Claims claims = extractAllClaims(token);
            return claims.getSubject().equals(user.getUsername())
                    && expectedType.equals(claims.get("type", String.class));
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
