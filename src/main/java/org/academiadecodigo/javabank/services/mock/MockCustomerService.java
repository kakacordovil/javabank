package org.academiadecodigo.javabank.services.mock;

import org.academiadecodigo.javabank.exceptions.AssociationExistsException;
import org.academiadecodigo.javabank.exceptions.CustomerNotFoundException;
import org.academiadecodigo.javabank.exceptions.RecipientNotFoundException;
import org.academiadecodigo.javabank.persistence.model.AbstractModel;
import org.academiadecodigo.javabank.persistence.model.Customer;
import org.academiadecodigo.javabank.persistence.model.Recipient;
import org.academiadecodigo.javabank.persistence.model.account.Account;
import org.academiadecodigo.javabank.services.AccountService;
import org.academiadecodigo.javabank.services.CustomerService;

import java.util.*;
import java.util.stream.Collectors;

/**
 * A mock {@link CustomerService} implementation
 */
public class MockCustomerService extends AbstractMockService<Customer> implements CustomerService {

    private AccountService accountService;

    /**
     * Sets the customer service
     *
     * @param accountService the customer service to set
     */
    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * @see CustomerService#get(Integer)
     */
    @Override
    public Customer get(Integer id) {
        return modelMap.get(id);
    }

    /**
     * @see CustomerService#getBalance(Integer)
     */
    @Override
    public double getBalance(Integer id) throws CustomerNotFoundException {

        Customer customer = Optional.ofNullable(modelMap.get(id))
                .orElseThrow(CustomerNotFoundException::new);

        return customer.getAccounts().stream()
                .mapToDouble(Account::getBalance)
                .sum();
    }

    /**
     * @see CustomerService#save(Customer)
     */
    @Override
    public Customer save(Customer customer) {

        if (customer.getId() == null) {
            customer.setId(getNextId());
        }

        modelMap.put(customer.getId(), customer);
        return customer;
    }

    /**
     * @see CustomerService#delete(Integer)
     */
    @Override
    public void delete(Integer id) throws AssociationExistsException {

        Customer customer = get(id);

        if (!customer.getAccounts().isEmpty()) {
            throw new AssociationExistsException();
        }

        modelMap.remove(id);
    }

    /**
     * @see CustomerService#list()
     */
    @Override
    public List<Customer> list() {
        return new ArrayList<>(modelMap.values());
    }

    /**
     * @see CustomerService#listRecipients(Integer)
     */
    @Override
    public List<Recipient> listRecipients(Integer id) throws CustomerNotFoundException {

        return Optional.ofNullable(modelMap.get(id))
                .orElseThrow(CustomerNotFoundException::new)
                .getRecipients();
    }

    /**
     * @see CustomerService#addRecipient(Integer, Recipient)
     */
    @Override
    public Recipient addRecipient(Integer id, Recipient recipient) {

        Customer customer = modelMap.get(id);

        if (accountService.get(recipient.getAccountNumber()) == null ||
                getAccountIds(customer).contains(recipient.getAccountNumber())) {
            return null;
        }

        if (recipient.getId() == null) {
            recipient.setId(getNextId());
        }

        customer.addRecipient(recipient);

        return recipient;
    }

    /**
     * @see CustomerService#removeRecipient(Integer, Integer)
     */
    @Override
    public void removeRecipient(Integer id, Integer recipientId)
            throws CustomerNotFoundException, RecipientNotFoundException {

        Customer customer = Optional.ofNullable(modelMap.get(id))
                .orElseThrow(CustomerNotFoundException::new);

        Recipient recipient = null;

        for (Recipient rcpt : customer.getRecipients()) {
            if (rcpt.getId().equals(recipientId)) {
                recipient = rcpt;
            }
        }

        if (recipient == null) {
            throw new RecipientNotFoundException();
        }

        customer.removeRecipient(recipient);
    }

    /**
     * @see CustomerService#addAccount(Integer, Account)
     */
    @Override
    public Account addAccount(Integer id, Account account) {
        Customer customer = get(id);
        customer.addAccount(account);
        return account;
    }

    /**
     * @see CustomerService#closeAccount(Integer, Integer)
     */
    @Override
    public void closeAccount(Integer cid, Integer accountId) {
        Customer customer = modelMap.get(cid);

        customer.getAccounts().stream()
                .filter(account -> account.getId().equals(accountId))
                .findFirst()
                .ifPresent(customer::removeAccount);
    }

    private Set<Integer> getAccountIds(Customer customer) {
        List<Account> accounts = customer.getAccounts();

        return accounts.stream()
                .map(AbstractModel::getId)
                .collect(Collectors.toSet());
    }
}
