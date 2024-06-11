package com.shounoop.carrentalspring.services.customer;

import com.shounoop.carrentalspring.dto.BookACarDto;
import com.shounoop.carrentalspring.dto.CarDto;
import com.shounoop.carrentalspring.dto.ChangePasswordDto;
import com.shounoop.carrentalspring.dto.UserDto;
import com.shounoop.carrentalspring.entity.User;

import java.util.List;
import java.util.Optional;

public interface CustomerService {
    Optional<User> getCustomerById(Long customerId);
    void updateUserProfile(User userProfileDto);
    void changePassword(ChangePasswordDto changePasswordDto);


    List<CarDto> getAllCars();
    boolean bookACar(BookACarDto bookACarDto);
    CarDto getCarById(Long id);
    List<BookACarDto> getBookingsByUserId(Long userId);
    String bookAndPayForCar(BookACarDto bookACarDto) throws Exception;
    boolean cancelBooking(Long bookingId);
    void updateBookingStatusToPaid(String paymentIntentId);
}
