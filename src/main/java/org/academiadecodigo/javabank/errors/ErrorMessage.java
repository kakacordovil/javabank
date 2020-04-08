package org.academiadecodigo.javabank.errors;

/**
 * Error messages to be used throughout the application
 */
public class ErrorMessage {

    public static final String CUSTOMER_NOT_FOUND = "Customer does not exist";
    public static final String CUSTOMER_RECIPIENT_NOT_FOUND = "Recipient not owned by customer";
    public static final String CUSTOMER_ACCOUNT_NOT_FOUND = "Account not owned by customer";

    public static final String ACCOUNT_CLOSE_AMOUNT_INVALID = "Account balance needs to be zero";
    public static final String ACCOUNT_NOT_FOUND = "Account does not exist";

    public static final String RECIPIENT_NOT_FOUND = "Recipient does not exist";
}
