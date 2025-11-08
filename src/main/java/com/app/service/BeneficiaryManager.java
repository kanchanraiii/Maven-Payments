package com.app.service;

import com.app.model.Beneficiary;
import exceptions.BeneficiaryNotFoundException;

/**
 * Manages an array of Beneficiary objects.
 * (CORRECTED VERSION: Now trims input in findBeneficiary)
 */
public class BeneficiaryManager {
    // Using a fixed-size array as requested
    private Beneficiary[] beneficiaries;
    private int beneficiaryCount;
    private static final int MAX_BENEFICIARIES = 10;

    public BeneficiaryManager() {
        this.beneficiaries = new Beneficiary[MAX_BENEFICIARIES];
        this.beneficiaryCount = 0;
    }

    /**
     * Adds a new beneficiary to the array.
     */
    public void addBeneficiary(Beneficiary beneficiary) {
        // Negative Testing: Null pointer check
        if (beneficiary == null) {
            System.out.println("Error: Cannot add a null beneficiary.");
            return; // Fail gracefully
        }
        if (beneficiaryCount >= MAX_BENEFICIARIES) {
            System.out.println("Error: Beneficiary list is full.");
            return;
        }

        // Check if beneficiary already exists
        for (int i = 0; i < beneficiaryCount; i++) {
            if (beneficiaries[i].getAccountId().equals(beneficiary.getAccountId())) {
                System.out.println("Error: Beneficiary with ID " + beneficiary.getAccountId() + " already exists.");
                return;
            }
        }

        this.beneficiaries[beneficiaryCount] = beneficiary;
        beneficiaryCount++;
        System.out.println("Beneficiary added: " + beneficiary.getName());
    }

    /**
     * Finds a beneficiary by their account ID.
     */
    public Beneficiary findBeneficiary(String accountId) throws BeneficiaryNotFoundException {
        // Negative Testing: Null or empty input check
        if (accountId == null || accountId.trim().isEmpty()) {
            throw new BeneficiaryNotFoundException("Account ID cannot be null or empty.");
        }

        // ***** THIS IS THE FIX *****
        // Trim whitespace from the input to make the search more flexible.
        String trimmedAccountId = accountId.trim();

        for (int i = 0; i < beneficiaryCount; i++) {
            // Compare against the trimmed ID
            if (this.beneficiaries[i].getAccountId().equals(trimmedAccountId)) {
                return this.beneficiaries[i];
            }
        }

        // Scenario 5: Beneficiary Not Found
        throw new BeneficiaryNotFoundException("No beneficiary found with Account ID: " + trimmedAccountId);
    }
}

