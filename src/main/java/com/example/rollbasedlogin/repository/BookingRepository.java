package com.example.rollbasedlogin.repository;

import com.example.rollbasedlogin.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    // For AI feature
    List<Booking> findByCabType(String cabType);

    // For Driver Dashboard
    List<Booking> findByDriverEmail(String driverEmail);

    // For HR/IT Personal Booking Lists
    List<Booking> findByHrEmail(String hrEmail);
}