package org.academiadecodigo.javabank.services;

import org.academiadecodigo.javabank.exceptions.AccountNotFoundException;
import org.academiadecodigo.javabank.exceptions.CustomerNotFoundException;
import org.academiadecodigo.javabank.exceptions.JavaBankException;
import org.academiadecodigo.javabank.exceptions.TransactionInvalidException;
import org.academiadecodigo.javabank.persistence.dao.AccountDao;
import org.academiadecodigo.javabank.persistence.dao.CustomerDao;
import org.academiadecodigo.javabank.persistence.model.Customer;
import org.academiadecodigo.javabank.persistence.model.account.Account;
import org.academiadecodigo.javabank.persistence.model.account.CheckingAccount;
import org.academiadecodigo.javabank.persistence.model.account.SavingsAccount;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class AccountServiceImplTest {

    private static final double DOUBLE_PRECISION = 0.1;
    private AccountDao accountDao;
    private CustomerDao customerDao;
    private AccountServiceImpl accountService;

    @Before
    public void setup() {

        accountDao = mock(AccountDao.class);
        customerDao = mock(CustomerDao.class);

        accountService = new AccountServiceImpl();
        accountService.setAccountDao(accountDao);
        accountService.setCustomerDao(customerDao);
    }

    @Test
    public void testGet() {

        //setup
        int fakeId = 9999;
        Account fakeAccount = mock(Account.class);
        when(accountDao.findById(fakeId)).thenReturn(fakeAccount);
        when(fakeAccount.getId()).thenReturn(fakeId);

        //exercise
        Account returnAcc = accountService.get(fakeId);

        //verify
        verify(accountDao, times(1)).findById(fakeId);
        assertTrue(returnAcc.getId() == fakeId);
    }

    @Test
    public void testDeposit() throws JavaBankException {

        //setup
        int fakeCustomerId = 9999;
        int fakeAccountId = 8888;
        double fakeAmount = 1000.00;

        Account fakeAccount = spy(CheckingAccount.class);
        Customer fakeCustomer = new Customer();
        fakeCustomer.setId(fakeCustomerId);
        fakeCustomer.addAccount(fakeAccount);
        fakeAccount.setId(fakeAccountId);

        when(customerDao.findById(fakeCustomerId)).thenReturn(fakeCustomer);
        when(accountDao.findById(fakeAccountId)).thenReturn(fakeAccount);
        when(fakeAccount.getCustomer()).thenReturn(fakeCustomer);

        //exercise
        accountService.deposit(fakeAccountId, fakeCustomerId, fakeAmount);

        //verify
        assertTrue(fakeAccount.getBalance() == fakeAmount);
        verify(customerDao, times(1)).findById(fakeCustomerId);
        verify(accountDao, times(1)).findById(fakeAccountId);
        verify(customerDao, times(1)).saveOrUpdate(fakeCustomer);
        verify(fakeAccount, times(1)).credit(fakeAmount);
    }

    @Test(expected = CustomerNotFoundException.class)
    public void testDepositInvalidCustomer() throws JavaBankException {

        //setup
        int fakeCustomerId = 9999;
        int fakeAccountId = 8888;
        double fakeAmount = 1000.00;
        Account fakeAccount = mock(Account.class);

        when(customerDao.findById(anyInt())).thenReturn(null);
        when(accountDao.findById(anyInt())).thenReturn(fakeAccount);

        //exercise
        accountService.deposit(fakeCustomerId, fakeAccountId, fakeAmount);
    }

    @Test(expected = AccountNotFoundException.class)
    public void testDepositInvalidAccount() throws JavaBankException {

        //setup
        int fakeCustomerId = 9999;
        int fakeAccountId = 8888;
        double fakeAmount = 1000.00;
        Customer fakeCustomer = mock(Customer.class);

        when(customerDao.findById(anyInt())).thenReturn(fakeCustomer);
        when(accountDao.findById(anyInt())).thenReturn(null);

        //exercise
        accountService.deposit(fakeCustomerId, fakeAccountId, fakeAmount);
    }

    @Test(expected = CustomerNotFoundException.class)
    public void testDepositInvalidAccountOwner() throws JavaBankException {

        // setup
        double fakeAmount = 100;
        int fakeCustomerThatIsDepositing = 9898;

        int fakeCustomerId = 9999;
        int fakeAccountId = 9999;
        Customer fakeCustomer = new Customer();
        Account fakeAccount = new SavingsAccount();
        fakeCustomer.setId(fakeCustomerId);

        fakeAccount.setCustomer(fakeCustomer);
        fakeCustomer.addAccount(fakeAccount);

        when(customerDao.findById(fakeCustomerThatIsDepositing)).thenReturn(fakeCustomer);
        when(accountDao.findById(fakeAccountId)).thenReturn(fakeAccount);

        // exercise
        accountService.deposit(fakeCustomerThatIsDepositing, fakeAccountId, fakeAmount);
    }

    @Test(expected = TransactionInvalidException.class)
    public void testDepositInvalidAmount() throws JavaBankException {

        // setup
        double fakeAmount = -10;

        int fakeCustomerId = 9999;
        int fakeAccountId = 9999;
        Customer fakeCustomer = new Customer();
        fakeCustomer.setId(fakeCustomerId);
        Account fakeAccount = new SavingsAccount();
        fakeAccount.setId(fakeAccountId);

        fakeAccount.setCustomer(fakeCustomer);
        fakeCustomer.addAccount(fakeAccount);

        when(customerDao.findById(fakeCustomerId)).thenReturn(fakeCustomer);
        when(accountDao.findById(fakeAccountId)).thenReturn(fakeAccount);

        // exercise
        accountService.deposit(fakeCustomerId, fakeAccountId, fakeAmount);
    }


    @Test
    public void testWithdraw() throws JavaBankException {

        // setup
        double fakeAmountToWithdraw = 100;
        double fakeAccountBalance = 9999;

        int fakeCustomerId = 9999;
        int fakeAccountId = 9999;
        Customer fakeCustomer = new Customer();
        fakeCustomer.setId(fakeCustomerId);
        Account fakeAccount = new CheckingAccount();

        fakeAccount.setId(fakeAccountId);
        fakeAccount.credit(fakeAccountBalance);
        fakeAccount.setCustomer(fakeCustomer);
        fakeCustomer.addAccount(fakeAccount);

        when(customerDao.findById(fakeCustomerId)).thenReturn(fakeCustomer);
        when(accountDao.findById(fakeAccountId)).thenReturn(fakeAccount);

        // exercise
        accountService.withdraw(fakeCustomerId, fakeAccountId, fakeAmountToWithdraw);

        //verify
        assertEquals(accountService.get(fakeAccountId).getBalance(), fakeAccountBalance - fakeAmountToWithdraw, DOUBLE_PRECISION);
    }

    @Test(expected = CustomerNotFoundException.class)
    public void testWithdrawInvalidCustomer() throws JavaBankException {

        // setup
        double fakeAmount = 100;
        int fakeCustomerId = 9999;
        int fakeAccountId = 9999;

        when(customerDao.findById(fakeCustomerId)).thenReturn(null);

        // exercise
        accountService.withdraw(fakeCustomerId, fakeAccountId, fakeAmount);
    }

    @Test(expected = AccountNotFoundException.class)
    public void testWithdrawInvalidAccount() throws JavaBankException {

        // setup
        double fakeAmount = 100;
        int fakeCustomerId = 9999;
        int fakeAccountId = 9999;
        Customer fakeCustomer = new Customer();

        when(customerDao.findById(fakeCustomerId)).thenReturn(fakeCustomer);
        when(accountDao.findById(fakeAccountId)).thenReturn(null);

        // exercise
        accountService.withdraw(fakeCustomerId, fakeAccountId, fakeAmount);
    }

    @Test(expected = AccountNotFoundException.class)
    public void testWithdrawInvalidAccountOwner() throws JavaBankException {

        // setup
        double fakeAmount = 100;
        int fakeCustomerThatIsDepositing = 9898;

        int fakeCustomerId = 9999;
        int fakeAccountId = 9999;
        Customer fakeCustomer = new Customer();
        Account fakeAccount = new CheckingAccount();
        fakeCustomer.setId(fakeCustomerId);

        fakeAccount.setCustomer(fakeCustomer);
        fakeCustomer.addAccount(fakeAccount);

        when(customerDao.findById(fakeCustomerThatIsDepositing)).thenReturn(fakeCustomer);
        when(accountDao.findById(fakeAccountId)).thenReturn(fakeAccount);

        // exercise
        accountService.withdraw(fakeAccountId, fakeCustomerThatIsDepositing, fakeAmount);
    }

    @Test(expected = TransactionInvalidException.class)
    public void testWithdrawInvalidAmount() throws JavaBankException {

        // setup
        double fakeAmount = -10;

        int fakeCustomerId = 9999;
        int fakeAccountId = 9999;
        Customer fakeCustomer = new Customer();
        fakeCustomer.setId(fakeCustomerId);
        Account fakeAccount = new CheckingAccount();
        fakeAccount.setId(fakeAccountId);

        fakeAccount.setCustomer(fakeCustomer);
        fakeCustomer.addAccount(fakeAccount);

        when(customerDao.findById(fakeCustomerId)).thenReturn(fakeCustomer);
        when(accountDao.findById(fakeAccountId)).thenReturn(fakeAccount);

        // exercise
        accountService.withdraw(fakeCustomerId, fakeAccountId, fakeAmount);
    }

    @Test(expected = TransactionInvalidException.class)
    public void testWithdrawInvalidAccountType() throws JavaBankException {

        // setup
        double fakeAmount = 100;

        int fakeCustomerId = 9999;
        int fakeAccountId = 9999;
        Customer fakeCustomer = new Customer();
        Account fakeAccount = new SavingsAccount();

        when(customerDao.findById(fakeCustomerId)).thenReturn(fakeCustomer);
        when(accountDao.findById(fakeAccountId)).thenReturn(fakeAccount);

        // exercise
        accountService.withdraw(fakeCustomerId, fakeAccountId, fakeAmount);
    }

}
