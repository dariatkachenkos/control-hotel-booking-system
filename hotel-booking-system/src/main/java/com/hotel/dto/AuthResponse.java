package com.hotel.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO для відповіді після успішної автентифікації.
 * Містить JWT токен та інформацію про користувача.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    
    private String token;       // JWT токен
    private String username;    // Ім'я користувача
    private String role;        // Роль (USER або ADMIN)
    private String message;     // Повідомлення
    
    // Конструктор для швидкого створення відповіді
    public AuthResponse(String token, String username, String role) {
        this.token = token;
        this.username = username;
        this.role = role;
        this.message = "Успішна авторизація";
    }
}
