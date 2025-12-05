package com.hotel.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO для входу користувача (логін).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    
    @NotBlank(message = "Username не може бути порожнім")
    private String username;
    
    @NotBlank(message = "Пароль не може бути порожнім")
    private String password;
}
