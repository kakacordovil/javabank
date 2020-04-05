package org.academiadecodigo.javabank.persistence.model.account;

import javax.persistence.Entity;

/**
 * A savings account model entity which requires a minimum balance
 * and can only be used for transferring money, not for debiting
 * @see Account
 * @see AccountType#SAVINGS
 */
@Entity
public class SavingsAccount extends Account {

    public static final double MIN_BALANCE = 100;

    /**
     * @see Account#getAccountType()
     */
    @Override
    public AccountType getAccountType() {
        return AccountType.SAVINGS;
    }

    /**
     * @see Account#canDebit(double)
     */
    @Override
    public boolean canDebit(double amount) {
        return super.canDebit(amount) && (getBalance() - amount) >= MIN_BALANCE;
    }

    /**
     * @see Account#canWithdraw()
     */
    @Override
    public boolean canWithdraw() {
      return false;
    }
}
