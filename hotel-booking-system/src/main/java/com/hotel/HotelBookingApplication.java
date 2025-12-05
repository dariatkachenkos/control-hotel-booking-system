package com.hotel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class HotelBookingApplication {
    
    public static void main(String[] args) {
        // Запуск Spring Boot додатку
        SpringApplication.run(HotelBookingApplication.class, args);
        System.out.println("===========================================");
        System.out.println("  Hotel Booking System запущено!");
        System.out.println("  API: http://localhost:8080");
        System.out.println("  H2 Console: http://localhost:8080/h2-console");
        System.out.println("===========================================");
    }
}
