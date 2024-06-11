package com.shounoop.carrentalspring.controller;

import com.shounoop.carrentalspring.dto.BookACarDto;
import com.shounoop.carrentalspring.dto.CarDto;
import com.shounoop.carrentalspring.dto.ChangePasswordDto;
import com.shounoop.carrentalspring.entity.User;
import com.shounoop.carrentalspring.services.customer.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @GetMapping("/{customerId}")
    public ResponseEntity<Optional<User>> getCustomerProfile(@PathVariable Long customerId) throws IOException {
        System.out.println("customerId               :"+customerId);
            Optional<User> customer = customerService.getCustomerById(customerId);

            if (customer.isPresent()) {
                return ResponseEntity.ok(customer);
            }
                return ResponseEntity.notFound().build(); // Return 404 status with empty body


    }
    @PutMapping("/profile")
    public ResponseEntity<?> updateUserProfile(@RequestBody User userProfileDto) {
        try {
            customerService.updateUserProfile(userProfileDto);
            return ResponseEntity.ok("Profile updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update profile: " + e.getMessage());
        }
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordDto  changePasswordDto) {
        try {
            customerService.changePassword(changePasswordDto);
            return ResponseEntity.ok("Password changed successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to change password: " + e.getMessage());
        }
    }
    @GetMapping("/cars")
    public ResponseEntity<List<CarDto>> getAllCars() {
        return ResponseEntity.ok(customerService.getAllCars());
    }

    @PostMapping("/car/book")
    public ResponseEntity<Void> bookACar(@RequestBody BookACarDto bookACarDto) {
        boolean isSuccessful = customerService.bookACar(bookACarDto);

        if (isSuccessful) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @PostMapping("/car/book-and-pay")
    public ResponseEntity<Map<String, String>> bookAndPayForCar(@RequestBody BookACarDto bookACarDto) {
        try {
            String paymentSessionId = customerService.bookAndPayForCar(bookACarDto);

            if (paymentSessionId != null) {
                Map<String, String> response = new HashMap<>();
                response.put("paymentSessionId", paymentSessionId);
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("error", "Booking or payment failed."));
            }
        } catch (Exception e) {
            e.printStackTrace(); // Log the exception
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("error", "An error occurred: " + e.getMessage()));
        }
    }



    @GetMapping("/car/{carId}")
    public ResponseEntity<CarDto> getCarById(@PathVariable Long carId) {
        CarDto carDto = customerService.getCarById(carId);

        if (carDto != null) {
            return ResponseEntity.ok(carDto);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("/car/bookings/{userId}")
    public ResponseEntity<List<BookACarDto>> getBookingsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(customerService.getBookingsByUserId(userId));
    }

    @DeleteMapping("/car/cancel-booking/{bookingId}")
    public ResponseEntity<Void> cancelBooking(@PathVariable Long bookingId) {
        boolean isCanceled = customerService.cancelBooking(bookingId);

        if (isCanceled) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

}
