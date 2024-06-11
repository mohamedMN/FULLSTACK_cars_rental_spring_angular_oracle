package com.shounoop.carrentalspring.controller;

import com.shounoop.carrentalspring.entity.BookACar;
import com.shounoop.carrentalspring.enums.BookCarStatus;
import com.shounoop.carrentalspring.repository.BookACarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class CarController {

    private final BookACarRepository bookACarRepository;

    @GetMapping("/api/customer/car/{carId}/reserved-dates")
    public List<ReservedDatesDto> getReservedDates(@PathVariable Long carId) {
        List<BookACar> bookings = bookACarRepository.findByCarIdAndBookCarStatus(carId, BookCarStatus.APPROVED);
        return bookings.stream()
                .map(booking -> new ReservedDatesDto(booking.getFromDate(), booking.getToDate()))
                .collect(Collectors.toList());
    }

    public static class ReservedDatesDto {
        private final Date fromDate;
        private final Date toDate;

        public ReservedDatesDto(Date fromDate, Date toDate) {
            this.fromDate = fromDate;
            this.toDate = toDate;
        }

        public Date getFromDate() {
            return fromDate;
        }

        public Date getToDate() {
            return toDate;
        }
    }
}
