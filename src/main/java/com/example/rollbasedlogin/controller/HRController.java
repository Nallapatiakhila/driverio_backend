package com.example.rollbasedlogin.controller;

import com.example.rollbasedlogin.model.Booking;
import com.example.rollbasedlogin.model.User;
import com.example.rollbasedlogin.repository.BookingRepository;
import com.example.rollbasedlogin.repository.UserRepository;
import com.example.rollbasedlogin.service.EmailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.List;

@RestController
@RequestMapping("/api/hr")
@CrossOrigin(origins = "http://localhost:3000")
public class HRController {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @PostMapping("/book")
    public ResponseEntity<?> bookCab(@RequestBody Booking booking) {

        try {

            booking.setStatus("ASSIGNED");

            String otp = String.valueOf((int)(Math.random() * 9000) + 1000);
            booking.setOtp(otp);

            booking.setDropTime("After 30 seconds");

            if (booking.getCabType().equalsIgnoreCase("Cab")) {
                booking.setDriverEmail("driver1@gmail.com");
            } else {
                booking.setDriverEmail("driver2@gmail.com");
            }

            Booking savedBooking = bookingRepository.save(booking);

            Optional<User> driverOpt = userRepository.findByEmail(savedBooking.getDriverEmail());

            if (driverOpt.isPresent()) {

                User driver = driverOpt.get();

                String hrEmail = savedBooking.getHrEmail();
                String driverEmail = driver.getEmail();

                if (hrEmail != null) {
                    String employeeMsg = "Cab Assigned 🚗\nDriver: "
                            + driverEmail
                            + "\nOTP: " + savedBooking.getOtp()
                            + "\nPickup: " + savedBooking.getPickup()
                            + "\nDrop: " + savedBooking.getDropLocation();

                    emailService.sendEmail(hrEmail, "Cab Assigned", employeeMsg);
                }

                if (driverEmail != null) {
                    String driverMsg = "New Booking 🚗\nEmployee: "
                            + savedBooking.getEmployeeName()
                            + "\nPickup: " + savedBooking.getPickup()
                            + "\nDrop: " + savedBooking.getDropLocation();

                    emailService.sendEmail(driverEmail, "New Booking Assigned", driverMsg);
                }
            }

            return ResponseEntity.ok(savedBooking);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Booking failed: " + e.getMessage());
        }
    }

    // 🔥 ADD THIS METHOD
    @GetMapping("/all-bookings")
    public ResponseEntity<List<Booking>> getAllBookings(@RequestParam(required = false) String email) {
        if (email != null && !email.isBlank()) {
            return ResponseEntity.ok(bookingRepository.findByHrEmail(email));
        }
        return ResponseEntity.ok(bookingRepository.findAll());
    }
}