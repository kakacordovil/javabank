package org.academiadecodigo.javabank.command;

import org.academiadecodigo.javabank.persistence.model.account.AccountType;
import org.academiadecodigo.javabank.persistence.model.account.Account;

/**
 * The {@link Account} data transfer object
 */
public class AccountDto {

    private Integer id;
    private AccountType type;

    private double balance;

    /**
     * Gets the id of the account dto
     *
     * @return the account dto id
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets the id of the account dto
     *
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Gets the type of the account dto
     *
     * @return the account dto type
     */
    public AccountType getType() {
        return type;
    }

    /**
     * Sets the type of the account dto
     *
     * @param type the type to set
     */
    public void setType(AccountType type) {
        this.type = type;
    }

    /**
     * Gets the balance of the account dto
     *
     * @return the account dto balance
     */
    public double getBalance() {
        return balance;
    }

    /**
     * Sets the balance of the account dto
     *
     * @param balance the balance to set
     */
    public void setBalance(double balance) {
        this.balance = balance;
    }

    /**
     * @see Object#toString()
     */
    @Override
    public String toString() {
        return "AccountDto{" +
                "id=" + id +
                ", type=" + type +
                '}';
    }
}
