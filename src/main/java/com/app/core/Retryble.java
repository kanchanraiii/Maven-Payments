package com.app.core;

public interface Retryble {
    boolean retryPayment(String transactionId);
}
