package com.example.rollbasedlogin.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.rollbasedlogin.model.Booking;
import com.example.rollbasedlogin.repository.BookingRepository;

@RestController
@RequestMapping("/api/driver")
@CrossOrigin(origins = "*")
public class DriverController {

    @Autowired
    private BookingRepository bookingRepo;

    @GetMapping("/mytrips")
    public List<Booking> getDriverTrips(@RequestParam String email) {
        return bookingRepo.findByDriverEmail(email);
    }
}
