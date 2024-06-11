package com.shounoop.carrentalspring.controller;

import com.shounoop.carrentalspring.dto.StatisticsDto;
import com.shounoop.carrentalspring.services.statistics.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
public class StatisticsController {
    private final StatisticsService statisticsService;

    @GetMapping("/users")
    public ResponseEntity<StatisticsDto> getUserStatistics() {
        StatisticsDto statistics = statisticsService.getUserStatistics();
        return ResponseEntity.ok(statistics);
    }

    @GetMapping("/bookings")
    public ResponseEntity<StatisticsDto> getBookingStatistics() {
        StatisticsDto statistics = statisticsService.getBookingStatistics();
        return ResponseEntity.ok(statistics);
    }
}
