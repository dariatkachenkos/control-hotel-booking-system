package com.hotel.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    
    // Секретний ключ з application.properties
    @Value("${jwt.secret}")
    private String secretKey;
    
    // Час життя токена в мілісекундах
    @Value("${jwt.expiration}")
    private long jwtExpiration;
    
    /**
     * Витягти username з токена.
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    
    /**
     * Витягти конкретний claim з токена.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    
    /**
     * Згенерувати токен для користувача.
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> extraClaims = new HashMap<>();
        // Додаємо роль до токена
        extraClaims.put("role", userDetails.getAuthorities().iterator().next().getAuthority());
        return generateToken(extraClaims, userDetails);
    }
    
    /**
     * Згенерувати токен з додатковими claims.
     */
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts.builder()
                .claims(extraClaims)                              // Додаткові дані
                .subject(userDetails.getUsername())               // Username як subject
                .issuedAt(new Date(System.currentTimeMillis()))   // Час створення
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration)) // Час закінчення
                .signWith(getSigningKey())                        // Підписуємо ключем
                .compact();                                        // Створюємо рядок
    }
    
    /**
     * Перевірити, чи токен дійсний.
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }
    
    /**
     * Перевірити, чи токен протермінований.
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
    
    /**
     * Витягти дату закінчення токена.
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
    
    /**
     * Витягти всі claims з токена.
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
    
    /**
     * Отримати ключ для підпису.
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
