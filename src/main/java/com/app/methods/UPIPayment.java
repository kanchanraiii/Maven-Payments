package com.app.methods;

import com.app.core.PaymentMethod;
import exceptions.InvalidCredentialsException;

public class UPIPayment extends PaymentMethod {
    private String upiId;

    public UPIPayment(String upiId) {
        super("UPI");
        this.upiId = upiId;
    }

    @Override
    public void processPayment(double amount, String... details) throws InvalidCredentialsException {
        // We expect the UPI PIN to be in details[0]
        if (details.length == 0 || details[0] == null) {
            throw new InvalidCredentialsException("UPI PIN is required.");
        }

        String pin = details[0];

        // Scenario 4: Invalid Credentials
        if (!pin.equals("1234")) { // Simulating the correct PIN
            throw new InvalidCredentialsException("Invalid UPI PIN entered.");
        }

        System.out.println("UPI payment of " + amount + " from " + this.upiId + " successful.");
    }
}
