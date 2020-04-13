package org.academiadecodigo.javabank.persistence.model.account;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SavingsAccountTest {

    private SavingsAccount account;

    @Before
    public void setup() {
        account = new SavingsAccount();
    }

    @Test
    public void testInitialBalance() {

        // check initial account balance
        assertEquals(0, account.getBalance(), 0.1);

    }

    @Test
    public void testCredit() {

        int credit = 100;
        double balance = account.getBalance();

        // credit the account
        account.credit(credit);

        // check if the balance has been updated
        assertEquals(balance + credit, account.getBalance(), 0.1);
    }

    @Test
    public void testCreditNegativeValue() {

        double credit = -100;
        double balance = account.getBalance();

        // credit the account with a negative value
        account.credit(credit);

        // check if the balance has remained the same
        assertEquals(balance, account.getBalance(), 0);

    }

    @Test
    public void testDebit() {

        double credit = SavingsAccount.MIN_BALANCE + 100;
        double debit = 100;

        // check initial account balance
        account.credit(credit);
        assertEquals(credit, account.getBalance(), 0);

        double balance = account.getBalance();

        // debit from the account
        account.debit(debit);

        // check if value has been updated
        assertEquals(SavingsAccount.MIN_BALANCE, account.getBalance(), 0);

    }


    @Test
    public void testDebitInsufficientFunds() {

        double credit = SavingsAccount.MIN_BALANCE + 100;

        // check initial account balance
        account.credit(credit);
        assertEquals(credit, account.getBalance(), 0);

        double maxDebit = account.getBalance() - SavingsAccount.MIN_BALANCE;

        // debit more than what is allowed
        account.debit(maxDebit + 10);

        // confirm that the account is still limited by its MIN_BALANCE
        assertEquals(credit, account.getBalance(), 0);
    }

    @Test
    public void testDebitNegativeValue() {

        double credit = SavingsAccount.MIN_BALANCE + 100;
        double debit = -100;

        // check initial account balance
        account.credit(credit);
        assertEquals(credit, account.getBalance(), 0);

        double balance = account.getBalance();

        account.debit(debit);
        assertEquals(balance, account.getBalance(), 0);

    }

}
