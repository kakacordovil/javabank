package org.academiadecodigo.javabank.persistence.model.account;

import org.academiadecodigo.javabank.persistence.model.AbstractModel;
import org.academiadecodigo.javabank.persistence.model.Customer;

import javax.persistence.*;

/**
 * A generic account model entity to be used as a base for concrete types of accounts
 */
@Entity
@Table(name = "account")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "account_type")
public abstract class Account extends AbstractModel {

    private double balance = 0;

    @ManyToOne
    private Customer customer;

    /**
     * Gets the account balance
     *
     * @return the account balance
     */
    public double getBalance() {
        return balance;
    }

    /**
     * Gets the account costumer
     *
     * @return the customer
     */
    public Customer getCustomer() {
        return customer;
    }

    /**
     * Sets the account costumer
     *
     * @param customer the customer to set
     */
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    /**
     * Gets the account type
     *
     * @return the account type
     */
    public abstract AccountType getAccountType();

    /**
     * Credits account if possible
     *
     * @param amount the amount to credit
     * @see Account#credit(double)
     */
    public void credit(double amount) {
        if (canCredit(amount)) {
            balance += amount;
        }
    }

    /**
     * Debits the account if possible
     *
     * @param amount the amount to debit
     * @see Account#canDebit(double)
     */
    public void debit(double amount) {
        if (canDebit(amount)) {
            balance -= amount;
        }
    }

    /**
     * Checks if a specific amount can be credited on the account
     *
     * @param amount the amount to check
     * @return {@code true} if the account can be credited
     */
    public boolean canCredit(double amount) {
        return amount > 0;
    }

    /**
     * Checks if a specific amount can be debited from the account
     *
     * @param amount the amount to check
     * @return {@code true} if the account can be debited
     */
    public boolean canDebit(double amount) {
        return amount > 0 && amount <= balance;
    }

    /**
     * Checks if the account can be withdrawn
     *
     * @return {@code true} if withdraw can be done
     */
    public boolean canWithdraw() {
        return true;
    }

    /**
     * @see Object#toString()
     */
    @Override
    public String toString() {
        return "Account{" +
                "balance=" + balance +
                ", customerId=" + (customer != null ? customer.getId() : null) +
                "} " + super.toString();
    }
}
