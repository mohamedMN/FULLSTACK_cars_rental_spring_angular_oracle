package com.shounoop.carrentalspring.dto;

import lombok.Data;

@Data

public class ChangePasswordDto {
    private String currentPassword;
    private String newPassword;
    private Long userId;

    // Getters and setters
    // Constructor
}

