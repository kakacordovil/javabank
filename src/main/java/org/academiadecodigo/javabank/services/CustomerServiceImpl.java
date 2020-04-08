package org.academiadecodigo.javabank.services;

import org.academiadecodigo.javabank.persistence.dao.AccountDao;
import org.academiadecodigo.javabank.persistence.dao.CustomerDao;
import org.academiadecodigo.javabank.persistence.dao.RecipientDao;
import org.academiadecodigo.javabank.persistence.model.AbstractModel;
import org.academiadecodigo.javabank.persistence.model.Customer;
import org.academiadecodigo.javabank.persistence.model.Recipient;
import org.academiadecodigo.javabank.persistence.model.account.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.academiadecodigo.javabank.errors.ErrorMessage.*;

/**
 * An {@link CustomerService} implementation
 */
@Service
public class CustomerServiceImpl implements CustomerService {

    private CustomerDao customerDao;
    private RecipientDao recipientDao;
    private AccountDao accountDao;

    /**
     * Sets the customer data access object
     *
     * @param customerDao the account DAO to set
     */
    @Autowired
    public void setCustomerDao(CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    @Autowired
    public void setRecipientDao(RecipientDao recipientDao) {
        this.recipientDao = recipientDao;
    }

    @Autowired
    public void setAccountDao(AccountDao accountDao) {
        this.accountDao = accountDao;
    }


    /**
     * @see CustomerService#get(Integer)
     */
    @Override
    public Customer get(Integer id) {
        return customerDao.findById(id);
    }

    /**
     * @see CustomerService#getBalance(Integer)
     */
    @Override
    public double getBalance(Integer id) {

        Customer customer = Optional.ofNullable(customerDao.findById(id))
                .orElseThrow(() -> new IllegalArgumentException("Customer does not exist"));

        return customer.getAccounts().stream()
                .mapToDouble(Account::getBalance)
                .sum();
    }

    /**
     * @see CustomerService#list()
     */
    @Override
    public List<Customer> list() {
        return customerDao.findAll();
    }

    /**
     * @see CustomerService#listCustomerAccountIds(Integer)
     */
    @Override
    public Set<Integer> listCustomerAccountIds(Integer id) {

        Customer customer = Optional.ofNullable(customerDao.findById(id))
                .orElseThrow(() -> new IllegalArgumentException("Customer does not exist"));

        return customer.getAccounts().stream()
                .map(AbstractModel::getId)
                .collect(Collectors.toSet());
    }

    /**
     * @see CustomerService#listRecipients(Integer)
     */
    @Transactional(readOnly = true)
    @Override
    public List<Recipient> listRecipients(Integer id) {

        // check then act logic requires transaction,
        // event if read only

        Customer customer = Optional.ofNullable(customerDao.findById(id))
                .orElseThrow(() -> new IllegalArgumentException("Customer does not exist"));

        return new ArrayList<>(customer.getRecipients());
    }

    @Transactional
    @Override
    public void deleteCustomer(Integer id) {
               customerDao.delete(id);
    }

    @Transactional
    @Override
    public Customer create(Customer customer) {
        System.out.println("KAKA-CREATE-CallingSaveOrUpdate");

        return customerDao.saveOrUpdate(customer);
    }

    @Transactional
    @Override
    public void delete(Integer id) {
        customerDao.delete(id);
    }

    @Override
    public void addRecipient(Integer id, Recipient recipient) {
        Customer customer = Optional.ofNullable(customerDao.findById(id))
                .orElseThrow(() -> new IllegalArgumentException(CUSTOMER_NOT_FOUND));

        if (accountDao.findById(recipient.getAccountNumber()) == null ||
                getAccountIds(customer).contains(recipient.getAccountNumber())) {
            throw new IllegalArgumentException("Invalid account number");
        }

        customer.addRecipient(recipient);
        customerDao.saveOrUpdate(customer);
    }

    @Override
    public void removeRecipient(Integer id, Integer recipientId) {
        Customer customer = Optional.ofNullable(customerDao.findById(id))
                .orElseThrow(() -> new IllegalArgumentException(CUSTOMER_NOT_FOUND));


        Recipient recipient = Optional.ofNullable(recipientDao.findById(recipientId))
                .orElseThrow(() -> new IllegalArgumentException(RECIPIENT_NOT_FOUND));

        if (!recipient.getCustomer().getId().equals(id)) {
            throw new IllegalArgumentException(CUSTOMER_RECIPIENT_NOT_FOUND);
        }

        customer.removeRecipient(recipient);
        customerDao.saveOrUpdate(customer);
    }

    @Transactional
    @Override
    public Customer save(Customer customer) {
        System.out.println("KAKA-SAVE-CallingSaveOrUpdate");

        return customerDao.saveOrUpdate(customer);
    }

    @Transactional
    @Override
    public void update(Customer customer) {
 System.out.println("KAKA-UPDATE-CallingSaveOrUpdate");
        customerDao.saveOrUpdate(customer);
    }

    private Set<Integer> getAccountIds(Customer customer) {
        List<Account> accounts = customer.getAccounts();

        return accounts.stream()
                .map(AbstractModel::getId)
                .collect(Collectors.toSet());
    }
}
