package com.example.rollbasedlogin.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.rollbasedlogin.model.Booking;
import com.example.rollbasedlogin.repository.BookingRepository;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    @Autowired
    private BookingRepository bookingRepo;

    // ✅ Get ALL bookings (HR + IT)
    @GetMapping("/all-bookings")
    public List<Booking> getAllBookings() {
        return bookingRepo.findAll();   // <-- THIS is important
    }
}
