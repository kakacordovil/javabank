package org.academiadecodigo.javabank.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.academiadecodigo.javabank.command.AccountDto;
import org.academiadecodigo.javabank.converters.AccountDtoToAccount;
import org.academiadecodigo.javabank.converters.AccountToAccountDto;
import org.academiadecodigo.javabank.exceptions.AccountNotFoundException;
import org.academiadecodigo.javabank.exceptions.CustomerNotFoundException;
import org.academiadecodigo.javabank.exceptions.TransactionInvalidException;
import org.academiadecodigo.javabank.persistence.model.Customer;
import org.academiadecodigo.javabank.persistence.model.account.Account;
import org.academiadecodigo.javabank.persistence.model.account.AccountType;
import org.academiadecodigo.javabank.persistence.model.account.CheckingAccount;
import org.academiadecodigo.javabank.persistence.model.account.SavingsAccount;
import org.academiadecodigo.javabank.services.AccountService;
import org.academiadecodigo.javabank.services.CustomerService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class RestAccountControllerTest {

    @Mock
    private CustomerService customerService;

    @Mock
    private AccountDtoToAccount accountDtoToAccount;

    @Mock
    private AccountToAccountDto accountToAccountDto;

    @Mock
    private AccountService accountService;

    @InjectMocks
    private RestAccountController restAccountController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;


    @Before
    public void setup() {

        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(restAccountController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testListCustomerAccounts() throws Exception {

        int fakeCustomerId = 999;
        Customer customer = new Customer();
        customer.setId(fakeCustomerId);

        int fakeAccountId = 888;
        int balance = 8888;
        AccountType accountType = AccountType.CHECKING;

        Account checkingAccount = new CheckingAccount();
        checkingAccount.setCustomer(customer);
        checkingAccount.setBalance(balance);
        checkingAccount.setId(fakeAccountId);
        customer.addAccount(checkingAccount);

        AccountDto accountDto = new AccountDto();
        accountDto.setId(fakeAccountId);
        accountDto.setBalance(String.valueOf(balance));
        accountDto.setType(accountType);


        when(customerService.get(fakeCustomerId)).thenReturn(customer);
        when(accountToAccountDto.convert(ArgumentMatchers.any(Account.class))).thenReturn(accountDto);

        mockMvc.perform(get("/api/customer/{cid}/account", fakeCustomerId))
                .andExpect(jsonPath("$[0].id").value(fakeAccountId))
                .andExpect(jsonPath("$[0].type").value(accountType.toString()))
                .andExpect(jsonPath("$[0].balance").value(balance))
                .andExpect(status().isOk());

        verify(customerService, times(1)).get(fakeCustomerId);
        verify(accountToAccountDto, times(1)).convert(checkingAccount);

    }

    @Test
    public void testListAccountsInvalidCustomer() throws Exception {

        int invalidId = 888;

        when(customerService.get(invalidId)).thenReturn(null);

        mockMvc.perform(get("/api/customer/{cid}/account", invalidId))
                .andExpect(status().isNotFound());

        verify(customerService, times(1)).get(invalidId);

    }

    @Test
    public void testShowCustomerAccount() throws Exception {

        int fakeCustomerId = 999;
        Customer customer = new Customer();
        customer.setId(fakeCustomerId);

        int fakeAccountId = 888;
        int balance = 8888;
        AccountType accountType = AccountType.CHECKING;

        Account checkingAccount = new CheckingAccount();
        checkingAccount.setCustomer(customer);
        checkingAccount.setBalance(balance);
        checkingAccount.setId(fakeAccountId);
        customer.addAccount(checkingAccount);

        AccountDto accountDto = new AccountDto();
        accountDto.setId(fakeAccountId);
        accountDto.setBalance(String.valueOf(balance));
        accountDto.setType(accountType);


        when(accountService.get(fakeAccountId)).thenReturn(checkingAccount);
        when(accountToAccountDto.convert(checkingAccount)).thenReturn(accountDto);

        mockMvc.perform(get("/api/customer/{cid}/account/{aid}", fakeCustomerId, fakeAccountId))
                .andExpect(jsonPath("$.id").value(fakeAccountId))
                .andExpect(jsonPath("$.type").value(accountType.toString()))
                .andExpect(jsonPath("$.balance").value(balance))
                .andExpect(status().isOk());

        verify(accountService, times(1)).get(fakeAccountId);
        verify(accountToAccountDto, times(1)).convert(checkingAccount);
    }

    @Test
    public void testShowInvalidAccount() throws Exception {

        int fakeCustomerId = 999;
        int invalidAccountId = 777;

        when(accountService.get(invalidAccountId)).thenReturn(null);

        mockMvc.perform(get("/api/customer/{cid}/account/{aid}", fakeCustomerId, invalidAccountId))
                .andExpect(status().isNotFound());

        verify(accountService, times(1)).get(invalidAccountId);
    }

    @Test
    public void testShowAccountWithInvalidCustomer() throws Exception {

        int fakeAccountId = 888;
        int invalidCustomerId = 777;

        Account checkingAccount = new CheckingAccount();
        checkingAccount.setId(fakeAccountId);


        when(accountService.get(fakeAccountId)).thenReturn(checkingAccount);

        mockMvc.perform(get("/api/customer/{cid}/account/{aid}", invalidCustomerId, fakeAccountId))
                .andExpect(status().isNotFound());

        verify(accountService, times(1)).get(fakeAccountId);
    }

    @Test
    public void testShowAccountWithMismatchingCustomerId() throws Exception {

        int fakeCustomerId = 999;
        int fakeAccountId = 888;
        int invalidCustomerId = 777;

        Customer customer = new Customer();
        customer.setId(fakeCustomerId);

        Account checkingAccount = new CheckingAccount();
        checkingAccount.setId(fakeAccountId);
        checkingAccount.setCustomer(customer);


        when(accountService.get(fakeAccountId)).thenReturn(checkingAccount);

        mockMvc.perform(get("/api/customer/{cid}/account/{aid}", invalidCustomerId, fakeAccountId))
                .andExpect(status().isNotFound());

        verify(accountService, times(1)).get(fakeAccountId);
    }

    @Test
    public void testAddAccount() throws Exception {

        int fakeCustomerId = 999;
        Customer customer = new Customer();
        customer.setId(fakeCustomerId);

        int fakeAccountId = 888;
        int balance = 8888;
        AccountType accountType = AccountType.CHECKING;

        Account account = new CheckingAccount();
        account.setCustomer(customer);
        account.setBalance(balance);
        account.setId(fakeAccountId);
        customer.addAccount(account);

        String initialAmount = "1111";
        AccountDto accountDto = new AccountDto();
        accountDto.setId(null);
        accountDto.setBalance(initialAmount);
        accountDto.setType(accountType);


        when(accountDtoToAccount.convert(ArgumentMatchers.any(AccountDto.class))).thenReturn(account);
        when(customerService.addAccount(fakeCustomerId, account)).thenReturn(account);

        mockMvc.perform(post("/api/customer/{cid}/account", fakeCustomerId)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsBytes(accountDto)))
                .andExpect(status().isCreated())
                .andExpect(header().string("location", containsString("http://localhost/api/customer/" + fakeCustomerId + "/account/" + fakeAccountId)));

        //verify properties of bound command object
        ArgumentCaptor<AccountDto> boundAccount = ArgumentCaptor.forClass(AccountDto.class);

        verify(accountDtoToAccount, times(1)).convert(boundAccount.capture());
        verify(customerService, times(1)).addAccount(fakeCustomerId, account);

        assertEquals(null, boundAccount.getValue().getId());
        assertEquals(initialAmount, boundAccount.getValue().getBalance());
        assertEquals(accountType, boundAccount.getValue().getType());

    }

    @Test
    public void testAddAccountWithBadRequest() throws Exception {

        int fakeCustomerId = 999;

        mockMvc.perform(post("/api/customer/{cid}/account", fakeCustomerId))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testAddAccountWithInvalidCustomer() throws Exception {

        int invalidCustomerId = 888;
        AccountType accountType = AccountType.CHECKING;
        String initialAmount = "1111";

        Account account = new CheckingAccount();

        AccountDto accountDto = new AccountDto();
        accountDto.setId(null);
        accountDto.setBalance(initialAmount);
        accountDto.setType(accountType);


        when(accountDtoToAccount.convert(ArgumentMatchers.any(AccountDto.class))).thenReturn(account);
        doThrow(new CustomerNotFoundException()).when(customerService).addAccount(ArgumentMatchers.any(Integer.class), ArgumentMatchers.any(Account.class));

        mockMvc.perform(post("/api/customer/{cid}/account", invalidCustomerId)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsBytes(accountDto)))
                .andExpect(status().isNotFound());

        verify(accountDtoToAccount, times(1)).convert(ArgumentMatchers.any(AccountDto.class));
    }

    @Test
    public void testAddSavingAccountWithoutMinimumBalance() throws Exception {

        int fakeCustomerId = 999;
        Customer customer = new Customer();
        customer.setId(fakeCustomerId);

        AccountType accountType = AccountType.SAVINGS;
        Account account = new SavingsAccount();

        String invalidInitialAmount = "1";
        AccountDto accountDto = new AccountDto();
        accountDto.setId(null);
        accountDto.setBalance(invalidInitialAmount);
        accountDto.setType(accountType);


        when(accountDtoToAccount.convert(ArgumentMatchers.any(AccountDto.class))).thenReturn(account);
        when(customerService.addAccount(fakeCustomerId, account)).thenReturn(account);
        doThrow(new TransactionInvalidException()).when(customerService).addAccount(ArgumentMatchers.any(Integer.class), ArgumentMatchers.any(Account.class));

        mockMvc.perform(post("/api/customer/{cid}/account", fakeCustomerId)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsBytes(accountDto)))
                .andExpect(status().isBadRequest());

        verify(accountDtoToAccount, times(1)).convert(ArgumentMatchers.any(AccountDto.class));
        verify(customerService, times(1)).addAccount(ArgumentMatchers.any(Integer.class), ArgumentMatchers.any(Account.class));
    }

    @Test
    public void testCloseAccount() throws Exception {

        int fakeCustomerId = 999;
        int fakeAccountId = 888;

        mockMvc.perform(get("/api/customer/{cid}/account/{aid}/close", fakeCustomerId, fakeAccountId))
                .andExpect(status().isOk());

        verify(customerService, times(1)).closeAccount(fakeCustomerId, fakeAccountId);
    }

    @Test
    public void testCloseAccountWithInvalidCustomer() throws Exception {

        int fakeAccountId = 888;
        int invalidCustomerId = 777;

        doThrow(new CustomerNotFoundException()).when(customerService).closeAccount(ArgumentMatchers.any(Integer.class), ArgumentMatchers.any(Integer.class));

        mockMvc.perform(get("/api/customer/{cid}/account/{aid}/close", invalidCustomerId, fakeAccountId))
                .andExpect(status().isNotFound());

        verify(customerService, times(1)).closeAccount(invalidCustomerId, fakeAccountId);
    }

    @Test
    public void testCloseInvalidAccount() throws Exception {

        int fakeCustomerId = 999;
        int invalidAccountId = 777;

        doThrow(new AccountNotFoundException()).when(customerService).closeAccount(ArgumentMatchers.any(Integer.class), ArgumentMatchers.any(Integer.class));

        mockMvc.perform(get("/api/customer/{cid}/account/{aid}/close", fakeCustomerId, invalidAccountId))
                .andExpect(status().isNotFound());

        verify(customerService, times(1)).closeAccount(fakeCustomerId, invalidAccountId);
    }

    @Test
    public void testCloseAccountWithNonZeroBalance() throws Exception {

        int fakeCustomerId = 999;
        int fakeAccountId = 888;

        doThrow(new TransactionInvalidException()).when(customerService).closeAccount(ArgumentMatchers.any(Integer.class), ArgumentMatchers.any(Integer.class));

        mockMvc.perform(get("/api/customer/{cid}/account/{aid}/close", fakeCustomerId, fakeAccountId))
                .andExpect(status().isBadRequest());

        verify(customerService, times(1)).closeAccount(fakeCustomerId, fakeAccountId);
    }

}
