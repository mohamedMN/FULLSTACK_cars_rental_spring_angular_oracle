package com.shounoop.carrentalspring.services.customer;

import com.shounoop.carrentalspring.dto.BookACarDto;
import com.shounoop.carrentalspring.dto.CarDto;
import com.shounoop.carrentalspring.dto.ChangePasswordDto;
import com.shounoop.carrentalspring.entity.BookACar;
import com.shounoop.carrentalspring.entity.Car;
import com.shounoop.carrentalspring.entity.User;
import com.shounoop.carrentalspring.enums.BookCarStatus;
import com.shounoop.carrentalspring.repository.BookACarRepository;
import com.shounoop.carrentalspring.repository.CarRepository;
import com.shounoop.carrentalspring.repository.UserRepository;
import com.shounoop.carrentalspring.services.StripeService;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CarRepository carRepository;
    private final UserRepository userRepository;
    private final BookACarRepository bookACarRepository;
    private final StripeService stripeService;

    private PasswordEncoder passwordEncoder;
    @Override
    public Optional<User> getCustomerById(Long customerId) {
        return userRepository.findById(customerId);
    }


    @Override
    public void updateUserProfile(User userProfileDto) {
        Optional<User> optionalUser = userRepository.findById(userProfileDto.getId());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setName(userProfileDto.getName());
            user.setEmail(userProfileDto.getEmail());
            // Update other fields as necessary
            userRepository.save(user);
        } else {
            throw new RuntimeException("User not found");
        }
    }

    @Override
    public void changePassword(ChangePasswordDto changePasswordDto) {
        Optional<User> optionalUser = userRepository.findById(changePasswordDto.getUserId());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (passwordEncoder.matches(changePasswordDto.getCurrentPassword(), user.getPassword())) {
                // Encode the new password
                String encodedNewPassword = passwordEncoder.encode(changePasswordDto.getNewPassword());
                user.setPassword(encodedNewPassword);
                userRepository.save(user);
            } else {
                throw new RuntimeException("Current password is incorrect");
            }
        } else {
            throw new RuntimeException("User not found");
        }
    }



    @Override
    public List<CarDto> getAllCars() {
        return carRepository.findAll().stream().map(Car::getCarDto).collect(Collectors.toList());
    }

    @Override
    public boolean bookACar(BookACarDto bookACarDto) {
        Optional<Car> optionalCar = carRepository.findById(bookACarDto.getCarId());
        Optional<User> optionalUser = userRepository.findById(bookACarDto.getUserId());

        if (optionalCar.isPresent() && optionalUser.isPresent()) {
            Car existingCar = optionalCar.get();

            BookACar bookACar = new BookACar();
            bookACar.setUser(optionalUser.get());
            bookACar.setCar(existingCar);
            bookACar.setBookCarStatus(BookCarStatus.PENDING);
            bookACar.setFromDate(bookACarDto.getFromDate());
            bookACar.setToDate(bookACarDto.getToDate());

            long diffInMilliSeconds = bookACar.getToDate().getTime() - bookACar.getFromDate().getTime();
            long days = TimeUnit.MILLISECONDS.toDays(diffInMilliSeconds);

            bookACar.setDays(days);
            bookACar.setPrice(days * existingCar.getPrice());

            bookACarRepository.save(bookACar);
            return true;
        }

        return false;
    }

    @Override
    public String bookAndPayForCar(BookACarDto bookACarDto) throws Exception {
        Optional<Car> optionalCar = carRepository.findById(bookACarDto.getCarId());
        Optional<User> optionalUser = userRepository.findById(bookACarDto.getUserId());

        if (optionalCar.isPresent() && optionalUser.isPresent()) {
            Car existingCar = optionalCar.get();

            BookACar bookACar = new BookACar();
            bookACar.setUser(optionalUser.get());
            bookACar.setCar(existingCar);
            bookACar.setBookCarStatus(BookCarStatus.PENDING);
            bookACar.setFromDate(bookACarDto.getFromDate());
            bookACar.setToDate(bookACarDto.getToDate());

            long diffInMilliSeconds = bookACar.getToDate().getTime() - bookACar.getFromDate().getTime();
            long days = TimeUnit.MILLISECONDS.toDays(diffInMilliSeconds);

            bookACar.setDays(days);
            bookACar.setPrice(days * existingCar.getPrice());

            double amount = bookACar.getPrice();
            String currency = "USD"; // You can fetch this from bookACarDto if needed

            try {
                Session session = stripeService.createCheckoutSession(amount, currency);
                bookACar.setPaymentSessionId(session.getId()); // Store the Session ID
                bookACarRepository.save(bookACar);
                return session.getId(); // Return the Session ID
            } catch (StripeException e) {
                e.printStackTrace();
                throw new Exception("Failed to create payment session", e);
            }
        }

        throw new Exception("Car or User not found");
    }


    @Override
    public void updateBookingStatusToPaid(String paymentIntentId) {
        Optional<BookACar> optionalBookACar = bookACarRepository.findByPaymentSessionId(paymentIntentId);
        if (optionalBookACar.isPresent()) {
            BookACar bookACar = optionalBookACar.get();
            bookACar.setBookCarStatus(BookCarStatus.APPROVED);
            bookACarRepository.save(bookACar);
        }
        /*if (bookACar != null) {
            bookACar.setBookCarStatus(BookCarStatus.PAID);
            bookACarRepository.save(bookACar);
        }*/
    }

    @Override
    public CarDto getCarById(Long id) {
        Optional<Car> optionalCar = carRepository.findById(id);
        return optionalCar.map(Car::getCarDto).orElse(null);
    }

    @Override
    public List<BookACarDto> getBookingsByUserId(Long userId) {
        return bookACarRepository.findAllByUserId(userId).stream()
                .sorted(Comparator.comparing((BookACar booking) -> booking.getBookCarStatus() == BookCarStatus.APPROVED ? 0 : 1))
                .map(BookACar::getBookACarDto)
                .collect(Collectors.toList());
    }

    @Override
    public boolean cancelBooking(Long bookingId) {
        Optional<BookACar> optionalBooking = bookACarRepository.findById(bookingId);

        if (optionalBooking.isPresent()) {
            BookACar booking = optionalBooking.get();
            // Update booking status to CANCELED or remove it
            booking.setBookCarStatus(BookCarStatus.REJECTED);
            bookACarRepository.save(booking);
            return true;
        }

        return false;
    }

}
