package com.app.methods;

import com.app.core.PaymentMethod;
import exceptions.InvalidCredentialsException;

public class NetBankingPayment extends PaymentMethod {
    private String bankName;

    public NetBankingPayment(String bankName) {
        super("NetBanking");
        this.bankName = bankName;
    }

    @Override
    public void processPayment(double amount, String... details) throws InvalidCredentialsException {
        // details[0] = username, details[1] = password
        if (details.length < 2 || details[0] == null || details[1] == null) {
            throw new InvalidCredentialsException("Username and Password are required.");
        }
        String username = details[0];
        String password = details[1];

        // Simulate credential check
        if (!username.equals("admin") || !password.equals("password123")) {
            throw new InvalidCredentialsException("Invalid NetBanking username or password.");
        }

        System.out.println("NetBanking payment of " + amount + " from " + this.bankName + " successful.");
    }
}