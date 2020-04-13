package org.academiadecodigo.javabank.controller.web;

import org.academiadecodigo.javabank.command.AccountDto;
import org.academiadecodigo.javabank.command.TransferDto;
import org.academiadecodigo.javabank.controller.web.AccountController;
import org.academiadecodigo.javabank.converters.AccountDtoToAccount;
import org.academiadecodigo.javabank.converters.TransferDtoToTransfer;
import org.academiadecodigo.javabank.domain.Transfer;
import org.academiadecodigo.javabank.persistence.model.Customer;
import org.academiadecodigo.javabank.persistence.model.account.AccountType;
import org.academiadecodigo.javabank.persistence.model.account.CheckingAccount;
import org.academiadecodigo.javabank.services.AccountService;
import org.academiadecodigo.javabank.services.CustomerService;
import org.academiadecodigo.javabank.services.TransferService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class AccountControllerTest {

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private AccountController accountController;

    private MockMvc mockMvc;

    @Mock
    private AccountDtoToAccount accountDtoToAccount;

    @Mock
    private TransferDtoToTransfer transferDtoToTransfer;

    @Mock
    private TransferService transferService;

    @Mock
    private AccountService accountService;

    @Before
    public void setup() {

        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(accountController).build();
    }

    @Test
    public void testCloseAccount() throws Exception {

        //setup
        int fakeCustomerId = 9999;
        int fakeAccountId = 8888;


        //exercise
        mockMvc.perform(get("/customer/" + fakeCustomerId + "/account/" + fakeAccountId + "/close"))

                //verify
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/customer/" + fakeCustomerId))
                .andExpect(flash().attribute("lastAction", "Closed account " + fakeAccountId));

        verify(customerService, times(1)).closeAccount(fakeCustomerId, fakeAccountId);
    }

    @Test
    public void testTransferToAccount() throws Exception {

        //setup
        Integer fakeCustomerId = 9999;
        Double fakeAmount = 1000.00;
        Integer fakeSrcId = 8888;
        Integer fakeDestId = 7777;

        Transfer fakeTransfer = new Transfer();
        fakeTransfer.setSrcId(fakeSrcId);
        fakeTransfer.setDstId(fakeDestId);
        fakeTransfer.setAmount(fakeAmount);

        when(transferDtoToTransfer.convert(any(TransferDto.class))).thenReturn(fakeTransfer);

        //exercise
        mockMvc.perform(post("/customer/" + fakeCustomerId + "/transfer")
                .param("srcId", fakeSrcId.toString())
                .param("dstId", fakeDestId.toString())
                .param("amount", fakeAmount.toString()))

                //verify
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/customer/" + fakeCustomerId))
                .andExpect(flash().attribute("lastAction", "Account # " + fakeSrcId + " transfered " + fakeAmount + " to account #" + fakeDestId));

        verify(transferService, times(1)).transfer(fakeTransfer, fakeCustomerId);

        assertEquals(fakeTransfer.getAmount(), fakeAmount);
        assertEquals(fakeTransfer.getSrcId(), fakeSrcId);
        assertEquals(fakeTransfer.getDstId(), fakeDestId);
    }

    @Test
    public void testDeposit() throws Exception {

        //setup
        int fakeCustomerId = 9998;
        Integer fakeDestId = 8888;
        Double fakeAmount = 1000.00;


        //exercise
        mockMvc.perform(post("/customer/" + fakeCustomerId + "/deposit")
                .param("id", fakeDestId.toString())
                .param("amount", fakeAmount.toString()))

                //verify
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/customer/" + fakeCustomerId))
                .andExpect(flash().attribute("lastAction", equalTo("Deposited " + fakeAmount + " into account # " + fakeDestId)));

        verify(accountService, times(1)).deposit(fakeDestId, fakeCustomerId, fakeAmount);
    }

    @Test
    public void testWithdraw() throws Exception {

        //setup
        int fakeCustomerId = 9998;
        Integer fakeDestId = 8888;
        Double fakeAmount = 1000.00;


        //exercise
        mockMvc.perform(post("/customer/" + fakeCustomerId + "/withdraw")
                .param("id", fakeDestId.toString())
                .param("amount", fakeAmount.toString()))

                //verify
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/customer/" + fakeCustomerId))
                .andExpect(flash().attribute("lastAction", equalTo("Withdrew " + fakeAmount + " from account # " + fakeDestId)));

        verify(accountService, times(1)).withdraw(fakeDestId, fakeCustomerId, fakeAmount);
    }

    @Test
    public void testAddAccount() throws Exception {

        //setup
        Integer fakeCustomerId = 9999;
        Double fakeInitialAmount = 1000.0;
        AccountType fakeAccountType = AccountType.CHECKING;

        Customer fakeCustomer = new Customer();

        fakeCustomer.setId(fakeCustomerId);

        CheckingAccount fakeAccount = new CheckingAccount();

        fakeAccount.setCustomer(fakeCustomer);
        fakeAccount.credit(fakeInitialAmount);

        when(customerService.get(fakeCustomerId)).thenReturn(fakeCustomer);
        when(accountDtoToAccount.convert(ArgumentMatchers.any(AccountDto.class))).thenReturn(fakeAccount);

        //exercise
        mockMvc.perform(post("/customer/" + fakeCustomerId + "/account")
                .param("customerId", fakeCustomerId.toString())
                .param("balance", fakeInitialAmount.toString())
                .param("type", fakeAccountType.toString()))

                //verify
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/customer/" + fakeCustomerId))
                .andExpect(flash().attribute("lastAction", equalTo("Created " + fakeAccount.getAccountType() + " account.")));

        ArgumentCaptor<AccountDto> boundAccount = ArgumentCaptor.forClass(AccountDto.class);
        verify(accountDtoToAccount, times(1)).convert(boundAccount.capture());
        verify(customerService, times(1)).addAccount(fakeCustomerId, fakeAccount);

        assertEquals(fakeInitialAmount.toString(), boundAccount.getValue().getBalance());
        assertEquals(fakeAccountType, boundAccount.getValue().getType());
    }

}
