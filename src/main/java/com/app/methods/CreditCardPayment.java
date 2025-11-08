package com.app.methods;

import com.app.core.PaymentMethod;
import com.app.core.Retryble;
import exceptions.InvalidCredentialsException;
import exceptions.PaymentGatewayTimeoutException;


public class CreditCardPayment extends PaymentMethod implements Retryble {

    public CreditCardPayment() {
        super("Credit Card");
    }

    @Override
    public void processPayment(double amount, String... details)
            throws InvalidCredentialsException, PaymentGatewayTimeoutException {
        // details[0] = cardNumber, details[1] = expiry, details[2] = cvv
        if (details.length < 3) {
            throw new InvalidCredentialsException("Card Number, Expiry, and CVV are required.");
        }

        System.out.println("Processing credit card payment for " + amount + "...");
        System.out.println("Contacting bank gateway...");

        // Scenario 3: Simulate a gateway timeout
        try {
            // Simulate a network delay
            Thread.sleep(3000); // 3 seconds
        } catch (InterruptedException e) {
            // Restore the interrupted status
            Thread.currentThread().interrupt();
        }

        // After the "delay", we simulate the timeout failure
        throw new PaymentGatewayTimeoutException("Bank server did not respond in time.");
    }

    @Override
    public boolean retryPayment(String transactionId) {
        System.out.println("Retrying payment for transaction: " + transactionId);
        // Real logic to retry would go here
        return false; // Simulate retry failure
    }
}
