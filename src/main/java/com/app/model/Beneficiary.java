package com.app.model;

public class Beneficiary {
    private String accountId; 
    private String name;
    private String upiId;

    public Beneficiary(String accountId, String name, String upiId) {
        // Negative testing: Prevent null or empty data
        if (accountId == null || accountId.trim().isEmpty()) {
            throw new IllegalArgumentException("Account ID cannot be null or empty.");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty.");
        }
        this.accountId = accountId;
        this.name = name;
        this.upiId = upiId;
    }

    // --- Getters (Encapsulation) ---
    public String getAccountId() {
        return accountId;
    }

    public String getName() {
        return name;
    }

    public String getUpiId() {
        return upiId;
    }

    @Override
    public String toString() {
        return "Beneficiary[Name: " + name + ", AccountID: " + accountId + "]";
    }
}
