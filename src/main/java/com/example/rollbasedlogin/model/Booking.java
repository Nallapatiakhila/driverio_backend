package com.example.rollbasedlogin.model;

import jakarta.persistence.*;

@Entity
@Table(name = "booking")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String employeeName;
    private String hrEmail;

    private String pickup;
    private String dropLocation;

    private String pickupTime;
    private String dropTime;

    private String cabType;
    private String driverEmail;

    private String status;
    private String otp;

    // ✅ VERY IMPORTANT (JPA needs this)
    public Booking() {
    }

    // Getters and Setters

    public Long getId() { return id; }

    public String getEmployeeName() { return employeeName; }
    public void setEmployeeName(String employeeName) { this.employeeName = employeeName; }

    public String getHrEmail() { return hrEmail; }
    public void setHrEmail(String hrEmail) { this.hrEmail = hrEmail; }

    public String getPickup() { return pickup; }
    public void setPickup(String pickup) { this.pickup = pickup; }

    public String getDropLocation() { return dropLocation; }
    public void setDropLocation(String dropLocation) { this.dropLocation = dropLocation; }

    public String getPickupTime() { return pickupTime; }
    public void setPickupTime(String pickupTime) { this.pickupTime = pickupTime; }

    public String getDropTime() { return dropTime; }
    public void setDropTime(String dropTime) { this.dropTime = dropTime; }

    public String getCabType() { return cabType; }
    public void setCabType(String cabType) { this.cabType = cabType; }

    public String getDriverEmail() { return driverEmail; }
    public void setDriverEmail(String driverEmail) { this.driverEmail = driverEmail; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getOtp() { return otp; }
    public void setOtp(String otp) { this.otp = otp; }
}