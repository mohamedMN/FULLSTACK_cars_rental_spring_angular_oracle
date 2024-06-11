package com.shounoop.carrentalspring.services.statistics;

import com.shounoop.carrentalspring.dto.StatisticsDto;

public interface StatisticsService {
    StatisticsDto getUserStatistics();
    StatisticsDto getBookingStatistics();
}
