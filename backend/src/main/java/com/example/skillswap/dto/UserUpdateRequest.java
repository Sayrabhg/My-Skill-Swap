package com.example.skillswap.dto;

import lombok.Data;

@Data
public class UserUpdateRequest {

    // Basic info
    private String name;
    private String bio;
    private String language;
    private String avatar;

    // Contact info
    private String email;
    private String mobileNumber;

    // Personal info
    private String gender;

    // Address info
    private String address;
    private String city;
    private String state;
    private String country;
    private String postalCode;

    // Social / optional links
    private String website;
    private String linkedin;
    private String github;

    // First login flag
    private Boolean isFirstLogin; // Use Boolean, not boolean, so null = no change
}