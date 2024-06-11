package com.shounoop.carrentalspring.controller;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.shounoop.carrentalspring.entity.BookACar;
import com.shounoop.carrentalspring.enums.BookCarStatus;
import com.shounoop.carrentalspring.repository.BookACarRepository;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.net.Webhook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StripeWebhookController {

    @Autowired
    private BookACarRepository bookACarRepository;

    @Value("${stripe.webhook.secret}")
    private String webhookSecret;

    @Value("${stripe.webhook.security.enabled}")
    private boolean isWebhookSecurityEnabled;

    @PostMapping("/stripe/webhook")
    public String handleStripeWebhook(@RequestBody String payload, @RequestHeader("Stripe-Signature") String sigHeader) {
        if (isWebhookSecurityEnabled) {
            try {
                // Verify the webhook signature
                Event event = Webhook.constructEvent(payload, sigHeader, webhookSecret);

                // Extract the event type
                String eventType = event.getType();

                // Handle the event
                handleEvent(eventType, payload);
            } catch (SignatureVerificationException e) {
                // Invalid signature
                return "Invalid signature";
            }
        } else {
            // Parse the incoming webhook payload without signature verification
            JsonParser parser = new JsonParser();
            JsonObject jsonPayload = parser.parse(payload).getAsJsonObject();

            // Extract the event type
            String eventType = jsonPayload.get("type").getAsString();

            // Handle the event
            handleEvent(eventType, payload);
        }

        return "Received webhook event";
    }

    private void handleEvent(String eventType, String payload) {
        JsonParser parser = new JsonParser();
        JsonObject jsonPayload = parser.parse(payload).getAsJsonObject();

        switch (eventType) {
            case "checkout.session.completed":
                // Extract the session ID from the event data
                String sessionId = jsonPayload.getAsJsonObject("data")
                        .getAsJsonObject("object").get("id").getAsString();
                String sessionIdpi = jsonPayload.getAsJsonObject("data")
                        .getAsJsonObject("object").get("payment_intent").getAsString();

                // Now you can access the session ID
                System.out.println("Checkout Session ID: " + sessionId);

                // Update the booking status to "APPROVED" based on the session ID
                bookACarRepository.findByPaymentSessionId(sessionId).ifPresent(bookACar -> {
                    bookACar.setBookCarStatus(BookCarStatus.APPROVED);
                    bookACar.setPaymentSessionId(sessionIdpi);
                    bookACarRepository.save(bookACar);
                });

                break;
            case "charge.refunded":
                // Extract the session ID from the event data
                String sessionIdrfnd = jsonPayload.getAsJsonObject("data")
                        .getAsJsonObject("object").get("payment_intent").getAsString();

                // Now you can access the session ID
                System.out.println("refunded Session ID: " + sessionIdrfnd);

                // Update the booking status to "APPROVED" based on the session ID
                bookACarRepository.findByPaymentSessionId(sessionIdrfnd).ifPresent(bookACar -> {
                    bookACar.setBookCarStatus(BookCarStatus.REJECTED);
                    bookACarRepository.save(bookACar);
                });

                break;
            case "charge.refund.updated":
                // Extract the session ID from the event data
                String sessionIdrfndu = jsonPayload.getAsJsonObject("data")
                        .getAsJsonObject("object").get("payment_intent").getAsString();

                // Now you can access the session ID
                System.out.println("refund.updated Session ID: " + sessionIdrfndu);

                // Update the booking status to "APPROVED" based on the session ID
                bookACarRepository.findByPaymentSessionId(sessionIdrfndu).ifPresent(bookACar -> {
                    bookACar.setBookCarStatus(BookCarStatus.REJECTED);
                    bookACarRepository.save(bookACar);
                });

                break;
            case "payment_intent.created":
                // Extract the payment intent ID from the event data
                String paymentIntentId = jsonPayload.getAsJsonObject("data")
                        .getAsJsonObject("object").get("id").getAsString();

                // Now you can access payment intent ID
                System.out.println("Payment Intent ID: " + paymentIntentId);

                break;
            case "payment_intent.succeeded":
                // Extract the succeeded payment intent ID from the event data
                String succeededPaymentIntentId = jsonPayload.getAsJsonObject("data")
                        .getAsJsonObject("object").get("id").getAsString();

                // Now you can access succeeded payment intent ID
                System.out.println("Succeeded Payment Intent ID: " + succeededPaymentIntentId);

                break;
            default:
                // Handle unknown event types
                System.out.println("Unhandled event type: " + eventType);
        }
    }
}
