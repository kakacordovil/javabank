package org.academiadecodigo.javabank.converters;

import org.academiadecodigo.javabank.command.AccountDto;
import org.academiadecodigo.javabank.persistence.model.account.Account;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * A {@link Converter} implementation, responsible for {@link Account} to {@link AccountDto} type conversion
 */
@Component
public class AccountToAccountDto extends AbstractConverter<Account, AccountDto> {

    /**
     * Converts the account model object into a account dto object
     *
     * @param account the account
     * @return the account dto
     */
    @Override
    public AccountDto convert(Account account) {

        AccountDto accountDto = new AccountDto();

        accountDto.setId(account.getId());
        accountDto.setType(account.getAccountType());
        accountDto.setBalance(account.getBalance());

        return accountDto;

    }
}
