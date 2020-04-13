package org.academiadecodigo.javabank.persistence.model.account;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CheckingAccountTest {

    private CheckingAccount account;

    @Before
    public void setup() {
        account = new CheckingAccount();
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

        double initialBalance = account.getBalance();
        double credit = 200;
        double debit = 100;

        // check initial account balance
        account.credit(credit);
        assertEquals(credit + initialBalance, account.getBalance(), 0);

        // debit from the account
        account.debit(debit);

        // check if value has been updated
        assertEquals(credit + initialBalance - debit, account.getBalance(), 0);

    }

    @Test
    public void testDebitFail() {

        double initialBalance = account.getBalance();
        double credit = 100;

        // check initial account balance
        account.credit(credit);
        assertEquals(credit + initialBalance, account.getBalance(), 0);

        // debit from the account
        account.debit(credit + initialBalance + 100);

        // check if value has not been updated
        assertEquals(credit + initialBalance, account.getBalance(), 0);
    }

    @Test
    public void testDebitNegativeValue() {

        double balance = account.getBalance();
        double debit = -100;

        account.debit(debit);
        assertEquals(balance, account.getBalance(), 0);

    }

}
