package com.example.rollbasedlogin.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.rollbasedlogin.model.Driver;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {

    Optional<Driver> findByEmail(String email);

    // 🔥 THIS IS THE IMPORTANT ONE
    List<Driver> findByAvailableTrueAndCabType(String cabType);

}
