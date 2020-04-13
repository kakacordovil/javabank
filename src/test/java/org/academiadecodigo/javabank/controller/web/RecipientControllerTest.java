package org.academiadecodigo.javabank.controller.web;

import org.academiadecodigo.javabank.command.AccountDto;
import org.academiadecodigo.javabank.command.CustomerDto;
import org.academiadecodigo.javabank.command.RecipientDto;
import org.academiadecodigo.javabank.command.TransferDto;
import org.academiadecodigo.javabank.controller.web.RecipientController;
import org.academiadecodigo.javabank.converters.*;
import org.academiadecodigo.javabank.domain.Transfer;
import org.academiadecodigo.javabank.persistence.model.Customer;
import org.academiadecodigo.javabank.persistence.model.Recipient;
import org.academiadecodigo.javabank.persistence.model.account.Account;
import org.academiadecodigo.javabank.services.CustomerService;
import org.academiadecodigo.javabank.services.RecipientService;
import org.academiadecodigo.javabank.services.TransferService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class RecipientControllerTest {

    @Mock
    private RecipientService recipientService;

    @Mock
    private CustomerService customerService;

    @Mock
    private TransferService transferService;

    @Mock
    private RecipientToRecipientDto recipientToRecipientDto;

    @Mock
    private RecipientDtoToRecipient recipientDtoToRecipient;

    @Mock
    private CustomerToCustomerDto customerToCustomerDto;

    @Mock
    private TransferDtoToTransfer transferDtoToTransfer;

    @Mock
    private AccountToAccountDto accountToAccountDto;

    @InjectMocks
    private RecipientController recipientController;

    private MockMvc mockMvc;

    @Before
    public void setup() {

        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(recipientController).build();
    }

    @Test
    public void testEditRecipient() throws Exception {

        //setup
        int fakeCustomerId = 9999;

        int fakeRecipientId = 8888;
        String fakeRecipientName = "Fake Recipient";
        String fakeRecipientEmail = "fake@gmail.com";
        String fakeRecipientPhone = "912345678";
        int fakeRecipientAccountNumber = 6666;
        String fakeRecipientDescription = "Fake";

        Customer fakeCustomer = new Customer();

        fakeCustomer.setId(fakeCustomerId);
        fakeCustomer.setFirstName("Rui");
        fakeCustomer.setLastName("Ferrao");
        fakeCustomer.setEmail("mail@gmail.com");
        fakeCustomer.setPhone("99999914143");

        Recipient fakeRecipient = new Recipient();

        RecipientDto fakeRecipientDto = new RecipientDto();

        fakeRecipientDto.setId(fakeRecipientId);
        fakeRecipientDto.setAccountNumber(fakeRecipientAccountNumber);
        fakeRecipientDto.setName(fakeRecipientName);
        fakeRecipientDto.setEmail(fakeRecipientEmail);
        fakeRecipientDto.setPhone(fakeRecipientPhone);
        fakeRecipientDto.setDescription(fakeRecipientDescription);

        CustomerDto customerDto = new CustomerDto();

        when(customerService.get(fakeCustomerId)).thenReturn(fakeCustomer);
        when(recipientService.get(fakeRecipientId)).thenReturn(fakeRecipient);
        when(recipientToRecipientDto.convert(fakeRecipient)).thenReturn(fakeRecipientDto);
        when(customerToCustomerDto.convert(fakeCustomer)).thenReturn(customerDto);

        //exercise
        mockMvc.perform(get("/customer/" + fakeCustomerId + "/recipient/" + fakeRecipientId + "/edit/"))

                //verify
                .andExpect(status().isOk())
                .andExpect(view().name("recipient/add-update"))
                .andExpect(model().attribute("customer", equalTo(customerDto)))
                .andExpect(model().attribute("recipient", equalTo(fakeRecipientDto)));

        verify(customerService, times(1)).get(fakeCustomerId);
        verify(recipientService, times(1)).get(fakeRecipientId);
        verify(recipientToRecipientDto, times(1)).convert(fakeRecipient);
    }

    @Test
    public void testSaveRecipient() throws Exception {

        //setup
        Integer fakeCustomerId = 9999;
        Integer fakeRecipientId = 8888;
        String fakeRecipientName = "Fake Recipient";
        String fakeRecipientEmail = "fake@gmail.com";
        String fakeRecipientPhone = "912345678";
        Integer fakeRecipientAccountNumber = 6666;
        String fakeRecipientDescription = "Fake";

        Customer fakeCustomer = new Customer();

        Recipient fakeRecipient = new Recipient();

        fakeRecipient.setId(fakeRecipientId);
        fakeRecipient.setName(fakeRecipientName);
        fakeRecipient.setEmail(fakeRecipientEmail);
        fakeRecipient.setPhone(fakeRecipientPhone);
        fakeRecipient.setAccountNumber(fakeRecipientAccountNumber);
        fakeRecipient.setDescription(fakeRecipientDescription);
        fakeRecipient.setCustomer(fakeCustomer);

        RecipientDto recipientDto = new RecipientDto();

        recipientDto.setId(fakeRecipientId);
        recipientDto.setAccountNumber(fakeRecipientAccountNumber);
        recipientDto.setName(fakeRecipientName);
        recipientDto.setEmail(fakeRecipientEmail);
        recipientDto.setPhone(fakeRecipientPhone);
        recipientDto.setDescription(fakeRecipientDescription);

        CustomerDto fakeCustomerDto = new CustomerDto();
        fakeCustomerDto.setId(fakeCustomerId);

        when(recipientDtoToRecipient.convert(ArgumentMatchers.any(RecipientDto.class))).thenReturn(fakeRecipient);
        when(customerToCustomerDto.convert(ArgumentMatchers.any(Customer.class))).thenReturn(fakeCustomerDto);

        //exercise
        mockMvc.perform(post("/customer/" + fakeCustomerId + "/recipient")
                .param("action", "save")
                .param("id", fakeRecipientId.toString())
                .param("accountNumber", fakeRecipientAccountNumber.toString())
                .param("name", fakeRecipientName)
                .param("email", fakeRecipientEmail)
                .param("phone", fakeRecipientPhone)
                .param("description", fakeRecipientDescription))

                //verify
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/customer/" + fakeCustomerId + "/recipient"))
                .andExpect(flash().attribute("lastAction", equalTo("Saved " + fakeRecipientName)));

        ArgumentCaptor<RecipientDto> boundRecipient = ArgumentCaptor.forClass(RecipientDto.class);
        verify(recipientDtoToRecipient, times(1)).convert(boundRecipient.capture());
        verify(customerService, times(1)).addRecipient(fakeCustomerId, fakeRecipient);

        assertEquals(fakeRecipientId, boundRecipient.getValue().getId());
        assertEquals(fakeRecipientName, boundRecipient.getValue().getName());
        assertEquals(fakeRecipientEmail, boundRecipient.getValue().getEmail());
        assertEquals(fakeRecipientPhone, boundRecipient.getValue().getPhone());
        assertEquals(fakeRecipientAccountNumber, boundRecipient.getValue().getAccountNumber());
        assertEquals(fakeRecipientDescription, boundRecipient.getValue().getDescription());
        assertEquals(fakeCustomer, fakeRecipient.getCustomer());
    }

    @Test
    public void testListRecipients() throws Exception {

        //setup
        int fakeCustomerId = 9999;
        Customer fakeCustomer = new Customer();
        List<Recipient> fakeRecipientList = new ArrayList<>();
        fakeRecipientList.add(new Recipient());
        fakeRecipientList.add(new Recipient());

        CustomerDto customerDto = new CustomerDto();

        List<AccountDto> accountDtos = new ArrayList<>();
        accountDtos.add(new AccountDto());
        accountDtos.add(new AccountDto());

        List<RecipientDto> recipientDtos = new ArrayList<>();
        recipientDtos.add(new RecipientDto());
        recipientDtos.add(new RecipientDto());

        when(customerService.get(fakeCustomerId)).thenReturn(fakeCustomer);
        when(customerService.listRecipients(fakeCustomerId)).thenReturn(fakeRecipientList);
        when(customerToCustomerDto.convert(fakeCustomer)).thenReturn(customerDto);
        when(accountToAccountDto.convert(ArgumentMatchers.<Account>anyList())).thenReturn(accountDtos);
        when(recipientToRecipientDto.convert(fakeRecipientList)).thenReturn(recipientDtos);

        //exercise
        mockMvc.perform(get("/customer/" + fakeCustomerId + "/recipient"))

                //verify
                .andExpect(status().isOk())
                .andExpect(model().attribute("customer", equalTo(customerDto)))
                .andExpect(model().attribute("accounts", equalTo(accountDtos)))
                .andExpect(model().attribute("recipients", equalTo(recipientDtos)))
                .andExpect(view().name("recipient/list"));

        verify(customerService, times(1)).get(fakeCustomerId);
        verify(customerService, times(1)).listRecipients(fakeCustomerId);
    }

    @Test
    public void testDeleteRecipient() throws Exception {

        int fakeCustomerId = 9998;
        int fakeRecipientId = 9999;
        Recipient recipient = new Recipient();

        when(recipientService.get(fakeRecipientId)).thenReturn(recipient);

        mockMvc.perform(get("/customer/" + fakeCustomerId + "/recipient/" + fakeRecipientId + "/delete/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/customer/" + fakeCustomerId + "/recipient"));

        verify(customerService, times(1)).removeRecipient(fakeCustomerId, fakeRecipientId);
    }

    @Test
    public void testAddRecipient() throws Exception {

        //setup
        int fakeCustomerId = 9999;
        Customer fakeCustomer = new Customer();
        fakeCustomer.setId(fakeCustomerId);
        fakeCustomer.setFirstName("Rui");
        fakeCustomer.setLastName("Ferrao");
        fakeCustomer.setEmail("mail@gmail.com");
        fakeCustomer.setPhone("912345678");

        when(customerService.get(fakeCustomerId)).thenReturn(fakeCustomer);
        //exercise
        mockMvc.perform(get("/customer/" + fakeCustomerId + "/recipient/add"))

                //verify
                .andExpect(status().isOk());
        //.andExpect(view().name("recipient/add-update"))
        //.andExpect(model().attribute("customer", equalTo(fakeCustomer)));

        verify(customerService, times(1)).get(fakeCustomerId);
    }

    @Test
    public void testSaveRecipientCancel() throws Exception {
        int fakeCustomerId = 9999;

        mockMvc.perform(post("/customer/" + fakeCustomerId + "/recipient")
                //added action parameter to post so spring can use the right method to process this request
                .param("action", "cancel"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    public void testTransfer() throws Exception {

        //setup
        int fakeCustomerId = 9999;
        Customer fakeCustomer = new Customer();
        List<Recipient> fakeRecipientList = new ArrayList<>();
        fakeRecipientList.add(new Recipient());
        fakeRecipientList.add(new Recipient());

        CustomerDto customerDto = new CustomerDto();

        List<RecipientDto> recipientDtos = new ArrayList<>();
        recipientDtos.add(new RecipientDto());
        recipientDtos.add(new RecipientDto());

        when(customerService.get(fakeCustomerId)).thenReturn(fakeCustomer);
        when(customerService.listRecipients(fakeCustomerId)).thenReturn(fakeRecipientList);
        when(customerToCustomerDto.convert(fakeCustomer)).thenReturn(customerDto);
        when(recipientToRecipientDto.convert(fakeRecipientList)).thenReturn(recipientDtos);

        //exercise
        mockMvc.perform(get("/customer/" + fakeCustomerId + "/recipient/transfer"))

                //verify
                .andExpect(status().isOk())
                .andExpect(model().attribute("customer", equalTo(customerDto)))
                .andExpect(model().attribute("recipients", equalTo(recipientDtos)))
                .andExpect(view().name("recipient/transfer"));

        verify(customerService, times(1)).get(fakeCustomerId);
        verify(customerService, times(1)).listRecipients(fakeCustomerId);

    }

    @Test
    public void testDoTransfer() throws Exception {

        //setup
        Integer fakeCustomerId = 9999;
        Double fakeAmount = 100.0;
        Integer fakeDestId = 1;
        Integer fakeSrcId = 2;

        Transfer fakeTransfer = new Transfer();

        fakeTransfer.setAmount(fakeAmount);
        fakeTransfer.setDstId(fakeDestId);
        fakeTransfer.setSrcId(fakeSrcId);

        when(transferDtoToTransfer.convert(any(TransferDto.class))).thenReturn(fakeTransfer);

        //exercise
        mockMvc.perform(post("/customer/" + fakeCustomerId + "/recipient/transfer")
                .param("dstId", fakeDestId.toString())
                .param("srcId", fakeSrcId.toString())
                .param("amount", fakeAmount.toString()))

                //verify
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/customer/" + fakeCustomerId))
                .andExpect(flash().attribute("lastAction", equalTo("Transfered " + fakeTransfer.getAmount() + " to account #" + fakeTransfer.getDstId())));

        ArgumentCaptor<TransferDto> boundTransfer = ArgumentCaptor.forClass(TransferDto.class);
        verify(transferDtoToTransfer, times(1)).convert(boundTransfer.capture());
        verify(transferService, times(1)).transfer(fakeTransfer, fakeCustomerId);


        assertEquals(fakeAmount.toString(), boundTransfer.getValue().getAmount());
        assertEquals(fakeDestId, boundTransfer.getValue().getDstId());
        assertEquals(fakeSrcId, boundTransfer.getValue().getSrcId());
    }

}











