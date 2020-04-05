package org.academiadecodigo.javabank.persistence.model.account;

import javax.persistence.Entity;

/**
 * A checking account with no restrictions
 * @see Account
 * @see AccountType#CHECKING
 */
@Entity
public class CheckingAccount extends Account {

    /**
     * @see Account#getAccountType()
     */
    @Override
    public AccountType getAccountType() {
        return AccountType.CHECKING;
    }
}
