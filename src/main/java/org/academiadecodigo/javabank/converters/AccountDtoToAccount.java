package org.academiadecodigo.javabank.converters;

import org.academiadecodigo.javabank.command.AccountDto;
import org.academiadecodigo.javabank.factories.AccountFactory;
import org.academiadecodigo.javabank.persistence.model.account.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * A {@link Converter} implementation, responsible for {@link AccountDto} to {@link Account} type conversion
 */
@Component
public class AccountDtoToAccount implements Converter<AccountDto, Account> {

    private AccountFactory accountFactory;

    /**
     * Sets the account factory
     *
     * @param accountFactory the account factory to set
     */
    @Autowired
    public void setAccountFactory(AccountFactory accountFactory) {
        this.accountFactory = accountFactory;
    }

    /**
     * Converts the account DTO into a account model object
     *
     * @param accountDto the account DTO
     * @return the account
     */
    @Override
    public Account convert(AccountDto accountDto) {

        Account account = null;

        account = accountFactory.createAccount(accountDto.getType());
        account.credit(accountDto.getBalance() != null ? Double.parseDouble(accountDto.getBalance()) : 0);

        return account;
    }
}

