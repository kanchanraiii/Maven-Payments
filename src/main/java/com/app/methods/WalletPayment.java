package com.app.methods;

import com.app.core.PaymentMethod;
import com.app.core.Refundable;
import exceptions.InsufficientBalanceException;


public class WalletPayment extends PaymentMethod implements Refundable {
    private double balance;

    public WalletPayment(double initialBalance) {
        super("Wallet");
        this.balance = initialBalance;
    }

    @Override
    public void processPayment(double amount, String... details) throws InsufficientBalanceException {
        // Scenario 2: Insufficient Funds
        if (amount > this.balance) {
            throw new InsufficientBalanceException(
                    "Insufficient funds in wallet. Available: " + this.balance + ", Required: " + amount);
        }
        this.balance -= amount;
        System.out.println("Wallet payment successful. New balance: " + this.balance);
    }

    @Override
    public void processRefund(String transactionId) {
        System.out.println("Processing wallet refund for transaction: " + transactionId);
        // Logic to add money back to balance would go here
    }
}
