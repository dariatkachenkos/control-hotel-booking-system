package com.hotel.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO для готелю.
 * Використовується для створення та оновлення готелів.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotelDto {
    
    private Long id;
    
    @NotBlank(message = "Назва готелю не може бути порожньою")
    private String name;
    
    @NotBlank(message = "Адреса не може бути порожньою")
    private String address;
    
    @NotBlank(message = "Місто не може бути порожнім")
    private String city;
    
    @NotNull(message = "Кількість зірок обов'язкова")
    @Min(value = 1, message = "Мінімум 1 зірка")
    @Max(value = 5, message = "Максимум 5 зірок")
    private Integer stars;
    
    private String description;
    
    private Integer roomCount; // Кількість номерів (для відповіді)
}
