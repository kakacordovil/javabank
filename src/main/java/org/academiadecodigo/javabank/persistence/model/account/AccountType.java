package org.academiadecodigo.javabank.persistence.model.account;

import java.util.Arrays;
import java.util.List;

/**
 * The possible {@link Account} types
 */
public enum AccountType {

    /**
     * @see CheckingAccount
     */
    CHECKING,

    /**
     * @see SavingsAccount
     */
    SAVINGS;

    /**
     * Lists the account types
     *
     * @return the list of account types
     */
    public static List<AccountType> list() {
        return Arrays.asList(AccountType.values());
    }
}
