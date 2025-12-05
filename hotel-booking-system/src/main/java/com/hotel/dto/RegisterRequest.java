package com.hotel.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO (Data Transfer Object) - об'єкти для передачі даних між клієнтом і сервером.
 * Вони відокремлюють внутрішню структуру Entity від зовнішнього API.
 */

/**
 * DTO для реєстрації нового користувача.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    
    @NotBlank(message = "Username не може бути порожнім")
    @Size(min = 3, max = 50, message = "Username має бути від 3 до 50 символів")
    private String username;
    
    @NotBlank(message = "Пароль не може бути порожнім")
    @Size(min = 6, message = "Пароль має бути мінімум 6 символів")
    private String password;
    
    @NotBlank(message = "Email не може бути порожнім")
    @Email(message = "Невірний формат email")
    private String email;
    
    @NotBlank(message = "Повне ім'я не може бути порожнім")
    private String fullName;
    
    @NotBlank(message = "Телефон не може бути порожнім")
    private String phone;
    
    @NotBlank(message = "Номер документа не може бути порожнім")
    private String documentNumber;
}
