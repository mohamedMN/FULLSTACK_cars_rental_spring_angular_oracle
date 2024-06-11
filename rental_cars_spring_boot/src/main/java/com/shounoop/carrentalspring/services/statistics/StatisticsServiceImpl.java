package com.shounoop.carrentalspring.services.statistics;

import com.shounoop.carrentalspring.dto.StatisticsDto;
import com.shounoop.carrentalspring.repository.BookACarRepository;
import com.shounoop.carrentalspring.repository.UserRepository;
import com.shounoop.carrentalspring.enums.BookCarStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {
    private final UserRepository userRepository;
    private final BookACarRepository bookACarRepository;

    @Override
    public StatisticsDto getUserStatistics() {
        long totalUsers = userRepository.count();
        long usersWithBookings = bookACarRepository.findAll()
                .stream()
                .map(bookACar -> bookACar.getUser().getId())
                .distinct()
                .count();

        StatisticsDto statisticsDto = new StatisticsDto();
        statisticsDto.setTotalUsers(totalUsers);
        statisticsDto.setUsersWithBookings(usersWithBookings);

        return statisticsDto;
    }

    @Override
    public StatisticsDto getBookingStatistics() {
        long totalBookings = bookACarRepository.count();
        long pendingBookings = bookACarRepository.countByBookCarStatus(BookCarStatus.PENDING);
        long approvedBookings = bookACarRepository.countByBookCarStatus(BookCarStatus.APPROVED);
        long rejectedBookings = bookACarRepository.countByBookCarStatus(BookCarStatus.REJECTED);

        StatisticsDto statisticsDto = new StatisticsDto();
        statisticsDto.setTotalBookings(totalBookings);
        statisticsDto.setPendingBookings(pendingBookings);
        statisticsDto.setApprovedBookings(approvedBookings);
        statisticsDto.setRejectedBookings(rejectedBookings);

        return statisticsDto;
    }
}
