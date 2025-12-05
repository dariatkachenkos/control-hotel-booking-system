package com.hotel.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    
    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }
    
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        
        // 1. Отримуємо заголовок Authorization
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;
        
        // 2. Перевіряємо, чи є заголовок і чи починається з "Bearer "
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            // Якщо немає токена - пропускаємо далі (можливо, публічний ендпоінт)
            filterChain.doFilter(request, response);
            return;
        }
        
        // 3. Витягуємо токен (прибираємо "Bearer ")
        jwt = authHeader.substring(7);
        
        try {
            // 4. Витягуємо username з токена
            username = jwtService.extractUsername(jwt);
            
            // 5. Якщо username є і користувач ще не автентифікований
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                
                // 6. Завантажуємо дані користувача з бази
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                
                // 7. Перевіряємо валідність токена
                if (jwtService.isTokenValid(jwt, userDetails)) {
                    
                    // 8. Створюємо об'єкт автентифікації
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null, // credentials не потрібні
                            userDetails.getAuthorities() // права доступу
                    );
                    
                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );
                    
                    // 9. Встановлюємо автентифікацію в контекст безпеки
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (Exception e) {
            // Якщо токен невалідний - логуємо і продовжуємо
            logger.error("Помилка обробки JWT токена: " + e.getMessage());
        }
        
        // 10. Продовжуємо ланцюжок фільтрів
        filterChain.doFilter(request, response);
    }
}
