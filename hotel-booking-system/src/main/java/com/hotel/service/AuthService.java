package com.hotel.service;

import com.hotel.dto.AuthResponse;
import com.hotel.dto.LoginRequest;
import com.hotel.dto.RegisterRequest;
import com.hotel.entity.Guest;
import com.hotel.entity.User;
import com.hotel.exception.BadRequestException;
import com.hotel.repository.GuestRepository;
import com.hotel.repository.UserRepository;
import com.hotel.security.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class AuthService {
    
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
    
    private final UserRepository userRepository;
    private final GuestRepository guestRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    
    // Впровадження залежностей через конструктор
    public AuthService(UserRepository userRepository,
                      GuestRepository guestRepository,
                      PasswordEncoder passwordEncoder,
                      JwtService jwtService,
                      AuthenticationManager authenticationManager,
                      UserDetailsService userDetailsService) {
        this.userRepository = userRepository;
        this.guestRepository = guestRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
    }
    
    /**
     * Реєстрація нового користувача.
     */
    public AuthResponse register(RegisterRequest request) {
        logger.info("Реєстрація нового користувача: {}", request.getUsername());
        
        // Перевірка унікальності username
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BadRequestException("Користувач з таким username вже існує");
        }
        
        // Перевірка унікальності email
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Користувач з таким email вже існує");
        }
        
        // Створюємо нового користувача
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword())); // Хешуємо пароль!
        user.setEmail(request.getEmail());
        user.setRole(User.Role.USER); // За замовчуванням - звичайний користувач
        
        // Зберігаємо користувача
        user = userRepository.save(user);
        
        // Створюємо профіль гостя
        Guest guest = new Guest();
        guest.setFullName(request.getFullName());
        guest.setPhone(request.getPhone());
        guest.setDocumentNumber(request.getDocumentNumber());
        guest.setUser(user);
        guestRepository.save(guest);
        
        logger.info("Користувач {} успішно зареєстрований", request.getUsername());
        
        // Генеруємо JWT токен
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        String token = jwtService.generateToken(userDetails);
        
        return new AuthResponse(token, user.getUsername(), user.getRole().name());
    }
    
    /**
     * Реєстрація адміністратора (використовується для тестування).
     */
    public AuthResponse registerAdmin(RegisterRequest request) {
        logger.info("Реєстрація адміністратора: {}", request.getUsername());
        
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BadRequestException("Користувач з таким username вже існує");
        }
        
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Користувач з таким email вже існує");
        }
        
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setRole(User.Role.ADMIN); // Роль адміністратора
        
        user = userRepository.save(user);
        
        Guest guest = new Guest();
        guest.setFullName(request.getFullName());
        guest.setPhone(request.getPhone());
        guest.setDocumentNumber(request.getDocumentNumber());
        guest.setUser(user);
        guestRepository.save(guest);
        
        logger.info("Адміністратор {} успішно зареєстрований", request.getUsername());
        
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        String token = jwtService.generateToken(userDetails);
        
        return new AuthResponse(token, user.getUsername(), user.getRole().name());
    }
    
    /**
     * Вхід користувача (логін).
     */
    public AuthResponse login(LoginRequest request) {
        logger.info("Спроба входу користувача: {}", request.getUsername());
        
        // Автентифікація через Spring Security
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        
        // Якщо автентифікація успішна - генеруємо токен
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new BadRequestException("Користувача не знайдено"));
        
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        String token = jwtService.generateToken(userDetails);
        
        logger.info("Користувач {} успішно увійшов", request.getUsername());
        
        return new AuthResponse(token, user.getUsername(), user.getRole().name());
    }
}
