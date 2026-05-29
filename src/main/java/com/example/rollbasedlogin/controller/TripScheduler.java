package com.example.rollbasedlogin.controller;

import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.rollbasedlogin.model.Booking;
import com.example.rollbasedlogin.model.Driver;
import com.example.rollbasedlogin.repository.BookingRepository;
import com.example.rollbasedlogin.repository.DriverRepository;

@Component
public class TripScheduler {

    @Autowired
    private BookingRepository bookingRepo;

    @Autowired
    private DriverRepository driverRepo;

    @Scheduled(fixedRate = 30000)
    public void manageTrips() {

        List<Booking> bookings = bookingRepo.findAll();

        // 1️⃣ Complete trips after 30 sec
        for (Booking booking : bookings) {

            if ("ASSIGNED".equals(booking.getStatus())) {

                booking.setStatus("COMPLETED");

                Driver driver =
                        driverRepo.findByEmail(booking.getDriverEmail()).orElse(null);

                if (driver != null) {
                    driver.setAvailable(true);
                    driverRepo.save(driver);
                }

                bookingRepo.save(booking);
            }
        }

        // 2️⃣ Assign waiting bookings
        for (Booking booking : bookings) {

            if ("WAITING".equals(booking.getStatus())) {

                List<Driver> drivers =
                        driverRepo.findByAvailableTrueAndCabType(booking.getCabType());

                if (!drivers.isEmpty()) {

                    Driver driver = drivers.get(0);

                    booking.setDriverEmail(driver.getEmail());
                    booking.setStatus("ASSIGNED");

                    int otp = 1000 + new Random().nextInt(9000);
                    booking.setOtp(String.valueOf(otp));

                    booking.setDropTime("After 30 seconds");

                    driver.setAvailable(false);

                    driverRepo.save(driver);
                    bookingRepo.save(booking);
                }
            }
        }

        System.out.println("Scheduler executed...");
    }
}
