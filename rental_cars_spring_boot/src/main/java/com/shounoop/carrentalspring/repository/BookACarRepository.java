package com.shounoop.carrentalspring.repository;

import com.shounoop.carrentalspring.entity.BookACar;
import com.shounoop.carrentalspring.enums.BookCarStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookACarRepository extends JpaRepository<BookACar, Long> {
    Optional<BookACar> findByPaymentSessionId(String paymentSessionId);
    List<BookACar> findAllByUserId(Long userId);
    List<BookACar> findByCarIdAndBookCarStatus(Long carId, BookCarStatus status);
    long countByBookCarStatus(BookCarStatus status);
}
