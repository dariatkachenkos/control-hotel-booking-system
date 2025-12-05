package com.hotel.dto;

import com.hotel.entity.Room;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

/**
 * DTO для номера готелю.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomDto {
    
    private Long id;
    
    @NotBlank(message = "Номер кімнати обов'язковий")
    private String roomNumber;
    
    @NotNull(message = "Тип номера обов'язковий")
    private Room.RoomType type;
    
    @NotNull(message = "Ціна обов'язкова")
    @DecimalMin(value = "0.01", message = "Ціна має бути більше 0")
    private BigDecimal pricePerNight;
    
    @NotNull(message = "Місткість обов'язкова")
    @Min(value = 1, message = "Мінімум 1 гість")
    private Integer capacity;
    
    private String description;
    
    private Boolean available;
    
    private Long hotelId;       // ID готелю
    private String hotelName;   // Назва готелю (для відповіді)
}
