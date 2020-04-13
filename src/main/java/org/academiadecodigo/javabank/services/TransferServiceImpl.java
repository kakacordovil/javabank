package org.academiadecodigo.javabank.services;

import org.academiadecodigo.javabank.domain.Transfer;
import org.academiadecodigo.javabank.exceptions.AccountNotFoundException;
import org.academiadecodigo.javabank.exceptions.CustomerNotFoundException;
import org.academiadecodigo.javabank.exceptions.TransactionInvalidException;
import org.academiadecodigo.javabank.persistence.dao.AccountDao;
import org.academiadecodigo.javabank.persistence.dao.CustomerDao;
import org.academiadecodigo.javabank.persistence.model.Customer;
import org.academiadecodigo.javabank.persistence.model.Recipient;
import org.academiadecodigo.javabank.persistence.model.account.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * A {@link TransferService} implementation
 */
@Service
public class TransferServiceImpl implements TransferService {

    private CustomerDao customerDao;
    private AccountDao accountDao;

    /**
     * Sets the customer data access object
     *
     * @param customerDao the customer dao to set
     */
    @Autowired
    public void setCustomerDao(CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    /**
     * Sets the account data access object
     *
     * @param accountDao the account dao to set
     */
    @Autowired
    public void setAccountDao(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    /**
     * @see TransferService#transfer(Transfer)
     */
    @Transactional
    @Override
    public void transfer(Transfer transfer) throws AccountNotFoundException, TransactionInvalidException {

        Account srcAccount = accountDao.findById(transfer.getSrcId());
        Account dstAccount = accountDao.findById(transfer.getDstId());

        accountTransfer(srcAccount, dstAccount, transfer.getAmount());
    }

    /**
     * @see TransferService#transfer(Transfer, Integer)
     */
    @Transactional
    @Override
    public void transfer(Transfer transfer, Integer customerId)
            throws CustomerNotFoundException, AccountNotFoundException, TransactionInvalidException {

        Customer customer = Optional.ofNullable(customerDao.findById(customerId))
                .orElseThrow(CustomerNotFoundException::new);

        Account srcAccount = Optional.ofNullable(accountDao.findById(transfer.getSrcId()))
                .orElseThrow(AccountNotFoundException::new);
        Account dstAccount = Optional.ofNullable(accountDao.findById(transfer.getDstId()))
                .orElseThrow(AccountNotFoundException::new);

        if (!customer.getAccounts().contains(srcAccount)) {
            throw new AccountNotFoundException();
        }

        // make sure destination account is a part of the recipient list
        verifyRecipientId(customer, dstAccount);

        accountTransfer(srcAccount, dstAccount, transfer.getAmount());
    }

    private void accountTransfer(Account srcAccount, Account dstAccount, Double amount)
            throws AccountNotFoundException, TransactionInvalidException {

        // make sure transaction can be performed
        verifyTransferAccountInformation(srcAccount, dstAccount, amount);

        srcAccount.debit(amount);
        dstAccount.credit(amount);

        accountDao.saveOrUpdate(srcAccount);
        accountDao.saveOrUpdate(dstAccount);
    }

    private void verifyTransferAccountInformation(Account srcAccount, Account dstAccount, double amount)
            throws AccountNotFoundException, TransactionInvalidException {

        Optional.ofNullable(srcAccount)
                .orElseThrow(AccountNotFoundException::new);

        Optional.ofNullable(dstAccount)
                .orElseThrow(AccountNotFoundException::new);

        if (!srcAccount.canDebit(amount) || !dstAccount.canCredit(amount)) {
            throw new TransactionInvalidException();
        }

    }

    private void verifyRecipientId(Customer customer, Account dstAccount) throws AccountNotFoundException {

        List<Integer> recipientAccountIds = listRecipientAccountIds(customer);

        if (!customer.getAccounts().contains(dstAccount) &&
                !recipientAccountIds.contains(dstAccount.getId())) {
            throw new AccountNotFoundException();
        }
    }

    private List<Integer> listRecipientAccountIds(Customer customer) {

        return customer.getRecipients().stream()
                .map(Recipient::getAccountNumber)
                .collect(Collectors.toList());
    }
}
