package com.app.core;

import exceptions.InvalidClassException;
import exceptions.InvalidCredentialsException;
import exceptions.InsufficientBalanceException;
import exceptions.PaymentGatewayTimeoutException;
import exceptions.InvalidAmountException;


public abstract class PaymentMethod {
    protected String paymentType;

    public PaymentMethod(String paymentType) {
        this.paymentType = paymentType;
    }

    
    public abstract void processPayment(double amount, String... details)
            throws InvalidCredentialsException, InsufficientBalanceException, PaymentGatewayTimeoutException;

   
    public void validateAmount(double amount) throws InvalidAmountException {
        if (amount <= 0) {
            throw new InvalidAmountException("Payment amount must be greater than zero. Received: " + amount);
        }
    }

    public String getPaymentType() {
        return paymentType;
    }
}