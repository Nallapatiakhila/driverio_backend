package com.example.rollbasedlogin.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.example.rollbasedlogin.model.Driver;
import com.example.rollbasedlogin.model.User;
import com.example.rollbasedlogin.repository.DriverRepository;
import com.example.rollbasedlogin.repository.UserRepository;
import com.example.rollbasedlogin.service.EmailService;
import com.example.rollbasedlogin.service.OtpService;
import com.example.rollbasedlogin.util.JwtUtil;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private DriverRepository driverRepo;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private OtpService otpService;

    @Autowired
    private EmailService emailService;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    // Blacklisted temporary / fake / throwaway domains
    private static final java.util.Set<String> BLOCKED_DOMAINS = java.util.Set.of(
        "yopmail.com", "mailinator.com", "tempmail.com", "dispostable.com", 
        "10minutemail.com", "guerrillamail.com", "sharklasers.com", 
        "getairmail.com", "burnermail.io", "trashmail.com", "fakeinbox.com",
        "temp-mail.org", "maildrop.cc", "throwawaymail.com", "tempmailaddress.com"
    );

    private boolean isValidEmail(String email) {
        if (email == null || !email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")) {
            return false;
        }
        int atIndex = email.indexOf("@");
        if (atIndex == -1 || atIndex == email.length() - 1) {
            return false;
        }
        String domain = email.substring(atIndex + 1).toLowerCase();
        
        if (!domain.contains(".") || domain.startsWith(".") || domain.endsWith(".")) {
            return false;
        }

        return !BLOCKED_DOMAINS.contains(domain);
    }

    // =========================
    // REGISTER WITH ROBUST VALIDATION (NO OTP)
    // =========================
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        String email = user.getEmail();
        if (email == null || !isValidEmail(email)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Registration rejected: Please provide a valid, legitimate email address. Disposable or throwaway domains are not allowed.");
        }

        if (userRepo.findByEmail(email).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Email already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);

        if (user.getRole() != null && user.getRole().equalsIgnoreCase("driver")) {
            Driver driver = new Driver();
            driver.setName(user.getUsername());
            driver.setEmail(user.getEmail());
            driver.setCabType(user.getCabType());
            driver.setAvailable(true);
            driverRepo.save(driver);
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                .body("User registered successfully");
    }


    // =========================
    // LOGIN WITH HR OTP
    // =========================
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request,
                                   HttpSession session) {

        String email = request.get("email");
        String password = request.get("password");
        String otpInput = request.get("otp"); // optional

        Optional<User> userOpt = userRepo.findByEmail(email);

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid email");
        }

        User user = userOpt.get();

        if (!passwordEncoder.matches(password, user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid password");
        }

        // Set HR session attribute if user is HR
        if (user.getRole().equalsIgnoreCase("HR")) {
            session.setAttribute("HR_VERIFIED", true);
        }

        // ✅ SUCCESS LOGIN AFTER OTP
        String token = jwtUtil.generateToken(user.getEmail(), user.getRole());

        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        response.put("role", user.getRole());
        response.put("email", user.getEmail());

        return ResponseEntity.ok(response);
    }


    // =========================
    // VERIFY OTP
    // =========================
    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(
            @RequestParam String email,
            @RequestParam String otp,
            HttpSession session) {

        if (otpService.validateOtp(email, otp)) {
            session.setAttribute("OTP_VERIFIED_" + email, true);
            Optional<User> userOpt = userRepo.findByEmail(email);
            if (userOpt.isPresent() && userOpt.get().getRole().equalsIgnoreCase("HR")) {
                session.setAttribute("HR_VERIFIED", true);
            }
            return ResponseEntity.ok("VERIFIED");
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("INVALID_OTP");
    }
}

