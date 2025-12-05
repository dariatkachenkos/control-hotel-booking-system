package com.hotel.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "guests")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Guest {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Повне ім'я гостя.
     */
    @Column(nullable = false)
    private String fullName;
    
    /**
     * Номер телефону.
     */
    @Column(nullable = false)
    private String phone;
    
    /**
     * Номер паспорта або ID документа.
     */
    @Column(nullable = false)
    private String documentNumber;
    
    /**
     * Зв'язок OneToOne з User.
     * @JoinColumn - створює колонку user_id в таблиці guests
     */
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
    
    /**
     * Список бронювань цього гостя.
     * OneToMany - один гість може мати багато бронювань
     * mappedBy = "guest" - зв'язок керується з боку Booking
     */
    @OneToMany(mappedBy = "guest", cascade = CascadeType.ALL)
    private List<Booking> bookings = new ArrayList<>();
}
