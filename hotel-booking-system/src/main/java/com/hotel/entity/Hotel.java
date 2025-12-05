package com.hotel.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "hotels")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Hotel {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Назва готелю.
     */
    @Column(nullable = false)
    private String name;
    
    /**
     * Адреса готелю.
     */
    @Column(nullable = false)
    private String address;
    
    /**
     * Місто розташування.
     */
    @Column(nullable = false)
    private String city;
    
    /**
     * Кількість зірок (рейтинг готелю).
     */
    @Column(nullable = false)
    private Integer stars;
    
    /**
     * Опис готелю.
     */
    @Column(length = 1000)
    private String description;
    

    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Room> rooms = new ArrayList<>();
    

    public void addRoom(Room room) {
        rooms.add(room);
        room.setHotel(this);
    }
    

    public void removeRoom(Room room) {
        rooms.remove(room);
        room.setHotel(null);
    }
}
