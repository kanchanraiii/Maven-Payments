package com.app.main;

import com.app.core.PaymentMethod;
import exceptions.*;
import com.app.methods.*;
import com.app.model.Beneficiary;
import com.app.service.BeneficiaryManager;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Main application class, rewritten for interactive user input.
 * This version is confirmed to work with the case-insensitive BeneficiaryManager.
 */
public class SmartPaymentGateway {

    // Re-usable services
    private final BeneficiaryManager beneficiaryManager;
    private final Scanner scanner;

    // Pre-instantiated payment methods for the user to choose
    private final WalletPayment wallet;
    private final UPIPayment upi;
    private final CreditCardPayment creditCard;
    private final NetBankingPayment netBanking;

    public SmartPaymentGateway() {
        this.scanner = new Scanner(System.in);
        this.beneficiaryManager = new BeneficiaryManager();
        
        // Initialize payment methods with some starting data
        this.wallet = new WalletPayment(1000.0); // Wallet starts with 1000
        this.upi = new UPIPayment("aashish@upi");
        this.creditCard = new CreditCardPayment();
        this.netBanking = new NetBankingPayment("MyBank");
        
        // Add a default beneficiary so the user can pay someone
        setupInitialData();
    }

    /**
     * Sets up initial beneficiaries for demo purposes.
     * This method MUST print "Beneficiary added" for the app to work.
     */
    private void setupInitialData() {
        System.out.println("--- Setting up initial beneficiaries ---");
        try {
            beneficiaryManager.addBeneficiary(new Beneficiary("BENE-101", "Aashish", "aashish@upi"));
            beneficiaryManager.addBeneficiary(new Beneficiary("BENE-102", "Gopal", "gopal@upi"));
        } catch (IllegalArgumentException e) {
            System.out.println("Error setting up initial data: " + e.getMessage());
        }
        System.out.println("----------------------------------------\n");
    }

    /**
     * The main application loop.
     * Keeps running until the user decides to exit.
     */
    public void runGateway() {
        boolean keepRunning = true;

        while (keepRunning) {
            int choice = showMainMenu();
            
            switch (choice) {
                case 1:
                    handlePayToBeneficiary();
                    break;
                case 2:
                    handleAddNewBeneficiary();
                    break;
                case 3:
                    keepRunning = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
            
            if (choice != 3) {
                keepRunning = askToContinue();
            }
        }
        closeGateway();
    }

    /**
     * Displays the main menu and gets the user's choice.
     */
    private int showMainMenu() {
        System.out.println("\n===== Smart Payment Gateway Main Menu =====");
        System.out.println("1. Pay to Beneficiary");
        System.out.println("2. Add New Beneficiary");
        System.out.println("3. Exit");
        return getIntInput("Enter your choice: ");
    }

    /**
     * Handles the entire flow for paying a beneficiary.
     * This method catches all errors gracefully so the main loop doesn't crash.
     */
    private void handlePayToBeneficiary() {
        try {
            // 1. Find Beneficiary
            String beneId = getStringInput("Enter Beneficiary Account ID: ");
            Beneficiary beneficiary = beneficiaryManager.findBeneficiary(beneId);
            System.out.println("Found Beneficiary: " + beneficiary.getName() + " (" + beneficiary.getAccountId() + ")");

            // 2. Get Amount
            double amount = getDoubleInput("Enter amount to pay: ");

            // 3. Choose Payment Method
            int methodChoice = showPaymentMethodMenu();
            PaymentMethod selectedMethod = null;
            String[] details = new String[3]; // Array to hold credentials

            switch (methodChoice) {
                case 1:
                    selectedMethod = upi;
                    details[0] = getStringInput("Enter UPI PIN: ");
                    break;
                case 2:
                    selectedMethod = wallet;
                    // No extra details needed for wallet
                    break;
                case 3:
                    selectedMethod = creditCard;
                    details[0] = getStringInput("Enter Card Number (XXXX-XXXX-XXXX-XXXX): ");
                    details[1] = getStringInput("Enter Expiry (MM/YY): ");
                    details[2] = getStringInput("Enter CVV: ");
                    break;
                case 4:
                    selectedMethod = netBanking;
                    details[0] = getStringInput("Enter NetBanking Username: ");
                    details[1] = getStringInput("Enter NetBanking Password: ");
                    break;
                default:
                    System.out.println("Invalid payment method. Returning to main menu.");
                    return;
            }

            // 4. Process the payment
            pay(selectedMethod, amount, details);

        } catch (BeneficiaryNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (TransactionFailedException e) {
            System.out.println("Main: Transaction failed. Cause: " + e.getMessage());
        } catch (Exception e) {
            // General catch-all for any other unexpected error (like InputMismatch)
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
    }

    /**
     * Displays the menu for choosing a payment method.
     */
    private int showPaymentMethodMenu() {
        System.out.println("\n--- Choose Payment Method ---");
        System.out.println("1. UPI (" + upi.getPaymentType() + ")");
        System.out.println("2. Wallet (" + wallet.getPaymentType() + ")");
        System.out.println("3. Credit Card (" + creditCard.getPaymentType() + ")");
        System.out.println("4. NetBanking (" + netBanking.getPaymentType() + ")");
        return getIntInput("Enter your choice: ");
    }

    /**
     * Handles the flow for adding a new beneficiary.
     */
    private void handleAddNewBeneficiary() {
        try {
            String id = getStringInput("Enter new Beneficiary Account ID: ");
            String name = getStringInput("Enter Beneficiary Name: ");
            String upiId = getStringInput("Enter Beneficiary UPI ID (optional, press Enter to skip): ");
            
            Beneficiary newBene = new Beneficiary(id, name, upiId);
            beneficiaryManager.addBeneficiary(newBene);
        } catch (IllegalArgumentException e) {
            // Catches errors from Beneficiary constructor (null name/id)
            System.out.println("Error adding beneficiary: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
    }

    /**
     * A single method to process any payment, demonstrating polymorphism.
     * This is the same method from the previous version.
     */
    public void pay(PaymentMethod method, double amount, String... details) throws TransactionFailedException {
        try {
            // 1. Validate amount (Unchecked Exception)
            method.validateAmount(amount);

            // 2. Process payment (Polymorphic call, throws Checked Exceptions)
            method.processPayment(amount, details);

            System.out.println("\nPAYMENT SUCCESS");
            System.out.println("Type: " + method.getPaymentType() + " | Amount: " + amount);

        } catch (InvalidAmountException e) {
            System.out.println("PAYMENT FAILED");
            System.out.println("Error: " + e.getMessage());
            throw new TransactionFailedException("Transaction failed due to invalid input.", e);

        } catch (InsufficientBalanceException e) {
            System.out.println("PAYMENT FAILED");
            System.out.println("Error: " + e.getMessage());
            System.out.println("Please recharge or choose another payment method.");
            throw new TransactionFailedException("Transaction failed due to insufficient funds.", e);

        } catch (InvalidCredentialsException e) {
            System.out.println("PAYMENT FAILED");
            System.out.println("Error: " + e.getMessage());
            System.out.println("LOG: Invalid credentials attempt for " + method.getPaymentType());
            throw new TransactionFailedException("Transaction failed due to invalid credentials.", e);

        } catch (PaymentGatewayTimeoutException e) {
            System.out.println("PAYMENT FAILED");
            System.out.println("Error: " + e.getMessage());
            throw new TransactionFailedException("Transaction failed due to a gateway timeout.", e);
        }
    }

    /**
     * Asks the user if they want to perform another transaction.
     */
    private boolean askToContinue() {
        String input = getStringInput("\nDo you want to perform another transaction? (yes/no): ");
        return input.trim().equalsIgnoreCase("yes");
    }

    /**
     * Cleans up resources before exiting.
     */
    private void closeGateway() {
        System.out.println("\nThank you for using the Smart Payment Gateway. Exiting.");
        scanner.close();
    }

    // --- ROBUST INPUT HELPER METHODS ---

    /**
     * Safely gets a String from the user.
     */
    private String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    /**
     * Safely gets an integer from the user, retrying until valid input is given.
     */
    private int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                int value = Integer.parseInt(scanner.nextLine());
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a whole number.");
            }
        }
    }

    private double getDoubleInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                double value = Double.parseDouble(scanner.nextLine());
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number (e.g., 500 or 250.75).");
            }
        }
    }

    // --- Main method to run the application ---
    public static void main(String[] args) {
        SmartPaymentGateway gateway = new SmartPaymentGateway();
        gateway.runGateway(); // Starts the interactive loop
    }
}

