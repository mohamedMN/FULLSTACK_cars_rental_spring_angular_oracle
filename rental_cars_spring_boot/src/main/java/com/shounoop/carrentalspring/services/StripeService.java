package com.shounoop.carrentalspring.services;

import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.stereotype.Service;

@Service
public class StripeService {

    public Session createCheckoutSession(double amount, String currency) throws StripeException {
        SessionCreateParams params = SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setPriceData(
                                        SessionCreateParams.LineItem.PriceData.builder()
                                                .setCurrency(currency)
                                                .setProductData(
                                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                .setName("Car Booking")
                                                                .build()
                                                )
                                                .setUnitAmount((long) (amount * 100)) // Stripe accepts amounts in cents
                                                .build()
                                )
                                .setQuantity(1L)
                                .build()
                )
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:4200/customer/my-bookings") // Update to your success URL
                .setCancelUrl("http://localhost:4200/customer/dashboard")   // Update to your cancel URL
                .build();

        return Session.create(params);
    }
}