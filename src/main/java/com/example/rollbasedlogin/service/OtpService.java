package com.example.rollbasedlogin.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.springframework.stereotype.Service;

@Service
public class OtpService {

    private Map<String, String> otpStorage = new HashMap<>();

    public String generateOtp(String email) {

        Random random = new Random();
        String otp = String.valueOf(1000 + random.nextInt(9000));

        otpStorage.put(email, otp);

        System.out.println("OTP for " + email + " is: " + otp);

        return otp;
    }

    public boolean validateOtp(String email, String otp) {

        String storedOtp = otpStorage.get(email);

        if (storedOtp != null && storedOtp.equals(otp)) {
            otpStorage.remove(email);
            return true;
        }

        return false;
    }

    public boolean hasOtp(String email) {
        return email != null && otpStorage.containsKey(email);
    }
}
