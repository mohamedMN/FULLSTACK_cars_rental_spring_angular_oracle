package com.shounoop.carrentalspring.dto;

import lombok.Data;

@Data
public class StatisticsDto {
    private long totalUsers;
    private long usersWithBookings;
    private long totalBookings;
    private long pendingBookings;
    private long approvedBookings;
    private long rejectedBookings;
}
