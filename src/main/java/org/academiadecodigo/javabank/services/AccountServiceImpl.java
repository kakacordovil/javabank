package org.academiadecodigo.javabank.services;

import org.academiadecodigo.javabank.exceptions.AccountNotFoundException;
import org.academiadecodigo.javabank.exceptions.CustomerNotFoundException;
import org.academiadecodigo.javabank.exceptions.TransactionInvalidException;
import org.academiadecodigo.javabank.persistence.dao.AccountDao;
import org.academiadecodigo.javabank.persistence.dao.CustomerDao;
import org.academiadecodigo.javabank.persistence.model.Customer;
import org.academiadecodigo.javabank.persistence.model.account.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * An {@link AccountService} implementation
 */
@Service
public class AccountServiceImpl implements AccountService {

    private AccountDao accountDao;
    private CustomerDao customerDao;

    /**
     * Sets the account data access object
     *
     * @param accountDao the account DAO to set
     */
    @Autowired
    public void setAccountDao(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    /**
     * Sets the customer data access object
     *
     * @param customerDao the customer DAO to set
     */
    @Autowired
    public void setCustomerDao(CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    /**
     * @see AccountService#get(Integer)
     */
    @Override
    public Account get(Integer id) {
        return accountDao.findById(id);
    }

    /**
     * @see AccountService#deposit(Integer, Integer, double)
     */
    @Transactional
    @Override
    public void deposit(Integer id, Integer customerId, double amount)
            throws AccountNotFoundException, CustomerNotFoundException, TransactionInvalidException {

        Customer customer = Optional.ofNullable(customerDao.findById(customerId))
                .orElseThrow(CustomerNotFoundException::new);

        Account account = Optional.ofNullable(accountDao.findById(id))
                .orElseThrow(AccountNotFoundException::new);

        if (!account.getCustomer().getId().equals(customerId)) {
            throw new AccountNotFoundException();
        }

        if (!account.canCredit(amount)) {
            throw new TransactionInvalidException();
        }

        for (Account a : customer.getAccounts()) {
            if (a.getId().equals(id)) {
                a.credit(amount);
            }
        }

        customerDao.saveOrUpdate(customer);
    }

    /**
     * @see AccountService#withdraw(Integer, Integer, double)
     */
    @Transactional
    @Override
    public void withdraw(Integer id, Integer customerId, double amount)
            throws AccountNotFoundException, CustomerNotFoundException, TransactionInvalidException {

        Customer customer = Optional.ofNullable(customerDao.findById(customerId))
                .orElseThrow(CustomerNotFoundException::new);

        Account account = Optional.ofNullable(accountDao.findById(id))
                .orElseThrow(AccountNotFoundException::new);

        // in UI the user cannot click on Withdraw so this is here for safety because the user can bypass
        // the UI limitation easily
        if (!account.canWithdraw()) {
            throw new TransactionInvalidException();
        }

        if (!account.getCustomer().getId().equals(customerId)) {
            throw new AccountNotFoundException();
        }

        // make sure transaction can be performed
        if (!account.canDebit(amount)) {
            throw new TransactionInvalidException();
        }

        for (Account a : customer.getAccounts()) {
            if (a.getId().equals(id)) {
                a.debit(amount);
            }
        }

        customerDao.saveOrUpdate(customer);
    }
}
