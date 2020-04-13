package org.academiadecodigo.javabank.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.academiadecodigo.javabank.command.RecipientDto;
import org.academiadecodigo.javabank.converters.RecipientDtoToRecipient;
import org.academiadecodigo.javabank.converters.RecipientToRecipientDto;
import org.academiadecodigo.javabank.exceptions.AccountNotFoundException;
import org.academiadecodigo.javabank.exceptions.CustomerNotFoundException;
import org.academiadecodigo.javabank.exceptions.RecipientNotFoundException;
import org.academiadecodigo.javabank.persistence.model.Customer;
import org.academiadecodigo.javabank.persistence.model.Recipient;
import org.academiadecodigo.javabank.services.CustomerService;
import org.academiadecodigo.javabank.services.RecipientService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class RestRecipientControllerTest {

    @Mock
    private CustomerService customerService;

    @Mock
    private RecipientToRecipientDto recipientToRecipientDto;

    @Mock
    private RecipientDtoToRecipient recipientDtoToRecipient;

    @Mock
    private RecipientService recipientService;


    @InjectMocks
    private RestRecipientController restRecipientController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Before
    public void setup() {

        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(restRecipientController).build();
        objectMapper = new ObjectMapper();

    }

    @Test
    public void testListRecipients() throws Exception {

        int fakeCustomerId = 999;

        int fakeRecipientId = 888;
        String fakeRecipientName = "Fake Recipient";
        String fakeRecipientEmail = "fake@gmail.com";
        String fakeRecipientPhone = "912345678";
        int fakeRecipientAccountNumber = 777;
        String fakeRecipientDescription = "Fake";

        Recipient fakeRecipient = new Recipient();
        List<Recipient> recipients = new ArrayList<>();
        recipients.add(fakeRecipient);

        RecipientDto fakeRecipientDto = new RecipientDto();
        fakeRecipientDto.setId(fakeRecipientId);
        fakeRecipientDto.setAccountNumber(fakeRecipientAccountNumber);
        fakeRecipientDto.setName(fakeRecipientName);
        fakeRecipientDto.setEmail(fakeRecipientEmail);
        fakeRecipientDto.setPhone(fakeRecipientPhone);
        fakeRecipientDto.setDescription(fakeRecipientDescription);


        when(customerService.listRecipients(fakeCustomerId)).thenReturn(recipients);
        when(recipientToRecipientDto.convert(ArgumentMatchers.any(Recipient.class))).thenReturn(fakeRecipientDto);

        mockMvc.perform(get("/api/customer/{cid}/recipient", fakeCustomerId))
                .andExpect(jsonPath("$[0].id").value(fakeRecipientId))
                .andExpect(jsonPath("$[0].accountNumber").value(fakeRecipientAccountNumber))
                .andExpect(jsonPath("$[0].name").value(fakeRecipientName))
                .andExpect(jsonPath("$[0].email").value(fakeRecipientEmail))
                .andExpect(jsonPath("$[0].phone").value(fakeRecipientPhone))
                .andExpect(jsonPath("$[0].description").value(fakeRecipientDescription))
                .andExpect(status().isOk());

        verify(customerService, times(1)).listRecipients(fakeCustomerId);
        verify(recipientToRecipientDto, times(1)).convert(ArgumentMatchers.any(Recipient.class));
    }

    @Test
    public void testListRecipientsFromInvalidCustomer() throws Exception {

        int invalidCustomerId = 777;

        doThrow(new CustomerNotFoundException()).when(customerService).listRecipients(invalidCustomerId);

        mockMvc.perform(get("/api/customer/{cid}/recipient", invalidCustomerId))
                .andExpect(status().isNotFound());

        verify(customerService, times(1)).listRecipients(invalidCustomerId);
    }

    @Test
    public void testShowRecipient() throws Exception {

        int fakeCustomerId = 999;

        int fakeRecipientId = 888;
        String fakeRecipientName = "Fake Recipient";
        String fakeRecipientEmail = "fake@gmail.com";
        String fakeRecipientPhone = "912345678";
        int fakeRecipientAccountNumber = 777;
        String fakeRecipientDescription = "Fake";

        Recipient fakeRecipient = new Recipient();

        RecipientDto fakeRecipientDto = new RecipientDto();
        fakeRecipientDto.setId(fakeRecipientId);
        fakeRecipientDto.setAccountNumber(fakeRecipientAccountNumber);
        fakeRecipientDto.setName(fakeRecipientName);
        fakeRecipientDto.setEmail(fakeRecipientEmail);
        fakeRecipientDto.setPhone(fakeRecipientPhone);
        fakeRecipientDto.setDescription(fakeRecipientDescription);


        when(recipientService.get(fakeRecipientId)).thenReturn(fakeRecipient);
        when(recipientToRecipientDto.convert(ArgumentMatchers.any(Recipient.class))).thenReturn(fakeRecipientDto);

        mockMvc.perform(get("/api/customer/{cid}/recipient/{rid}", fakeCustomerId, fakeRecipientId))
                .andExpect(jsonPath("$.id").value(fakeRecipientId))
                .andExpect(jsonPath("$.accountNumber").value(fakeRecipientAccountNumber))
                .andExpect(jsonPath("$.name").value(fakeRecipientName))
                .andExpect(jsonPath("$.email").value(fakeRecipientEmail))
                .andExpect(jsonPath("$.phone").value(fakeRecipientPhone))
                .andExpect(jsonPath("$.description").value(fakeRecipientDescription))
                .andExpect(status().isOk());

        verify(recipientService, times(1)).get(fakeRecipientId);
        verify(recipientToRecipientDto, times(1)).convert(ArgumentMatchers.any(Recipient.class));
    }

    @Test
    public void testShowInvalidRecipient() throws Exception {

        int fakeCustomerId = 999;
        int invalidRecipientId = 888;

        when(recipientService.get(invalidRecipientId)).thenReturn(null);

        mockMvc.perform(get("/api/customer/{cid}/recipient/{rid}", fakeCustomerId, invalidRecipientId))
                .andExpect(status().isNotFound());

        verify(recipientService, times(1)).get(invalidRecipientId);
    }

    @Test
    public void testAddRecipient() throws Exception {

        int fakeCustomerId = 999;
        int fakeRecipientId = 888;
        String fakeRecipientName = "Fake Recipient";
        String fakeRecipientEmail = "fake@gmail.com";
        String fakeRecipientPhone = "912345678";
        int fakeRecipientAccountNumber = 666;
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
        recipientDto.setAccountNumber(fakeRecipientAccountNumber);
        recipientDto.setName(fakeRecipientName);
        recipientDto.setEmail(fakeRecipientEmail);
        recipientDto.setPhone(fakeRecipientPhone);
        recipientDto.setDescription(fakeRecipientDescription);


        when(recipientDtoToRecipient.convert(ArgumentMatchers.any(RecipientDto.class))).thenReturn(fakeRecipient);
        when(customerService.addRecipient(fakeCustomerId, fakeRecipient)).thenReturn(fakeRecipient);

        mockMvc.perform(post("/api/customer/{cid}/recipient", fakeCustomerId)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsBytes(recipientDto)))
                .andExpect(status().isCreated())
                .andExpect(header().string("location", containsString("http://localhost/api/customer/" + fakeCustomerId + "/recipient/" + fakeRecipientId)));

        ArgumentCaptor<RecipientDto> boundRecipient = ArgumentCaptor.forClass(RecipientDto.class);

        verify(recipientDtoToRecipient, times(1)).convert(boundRecipient.capture());
        verify(customerService, times(1)).addRecipient(fakeCustomerId, fakeRecipient);

        assertEquals(null, boundRecipient.getValue().getId());
        assertEquals(fakeRecipientName, boundRecipient.getValue().getName());
        assertEquals(fakeRecipientEmail, boundRecipient.getValue().getEmail());
        assertEquals(fakeRecipientPhone, boundRecipient.getValue().getPhone());
        assertEquals(fakeRecipientDescription, boundRecipient.getValue().getDescription());

    }

    @Test
    public void testAddRecipientWithBadRequest() throws Exception {

        int fakeCustomerId = 999;

        mockMvc.perform(post("/api/customer/{cid}/recipient", fakeCustomerId))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testAddRecipientToInvalidCustomer() throws Exception {

        int invalidCustomerId = 999;
        Recipient fakeRecipient = new Recipient();
        RecipientDto recipientDto = new RecipientDto();


        when(recipientDtoToRecipient.convert(ArgumentMatchers.any(RecipientDto.class))).thenReturn(fakeRecipient);
        doThrow(new CustomerNotFoundException()).when(customerService).addRecipient(invalidCustomerId, fakeRecipient);

        mockMvc.perform(post("/api/customer/{cid}/recipient", invalidCustomerId)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsBytes(recipientDto)))
                .andExpect(status().isNotFound());

        verify(recipientDtoToRecipient, times(1)).convert(ArgumentMatchers.any(RecipientDto.class));
        verify(customerService, times(1)).addRecipient(invalidCustomerId, fakeRecipient);
    }

    @Test
    public void testAddRecipientToInvalidAccount() throws Exception {

        int fakeCustomerId = 999;
        Recipient fakeRecipient = new Recipient();
        RecipientDto recipientDto = new RecipientDto();


        when(recipientDtoToRecipient.convert(ArgumentMatchers.any(RecipientDto.class))).thenReturn(fakeRecipient);
        doThrow(new AccountNotFoundException()).when(customerService).addRecipient(fakeCustomerId, fakeRecipient);

        mockMvc.perform(post("/api/customer/{cid}/recipient", fakeCustomerId)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsBytes(recipientDto)))
                .andExpect(status().isNotFound());

        verify(recipientDtoToRecipient, times(1)).convert(ArgumentMatchers.any(RecipientDto.class));
        verify(customerService, times(1)).addRecipient(fakeCustomerId, fakeRecipient);
    }

    @Test
    public void testEditRecipient() throws Exception {

        int fakeCustomerId = 999;
        int fakeRecipientId = 888;
        String fakeRecipientName = "Fake Recipient";
        String fakeRecipientEmail = "fake@gmail.com";
        String fakeRecipientPhone = "912345678";
        int fakeRecipientAccountNumber = 666;
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
        recipientDto.setAccountNumber(fakeRecipientAccountNumber);
        recipientDto.setName(fakeRecipientName);
        recipientDto.setEmail(fakeRecipientEmail);
        recipientDto.setPhone(fakeRecipientPhone);
        recipientDto.setDescription(fakeRecipientDescription);


        when(recipientDtoToRecipient.convert(ArgumentMatchers.any(RecipientDto.class))).thenReturn(fakeRecipient);
        when(customerService.addRecipient(fakeCustomerId, fakeRecipient)).thenReturn(fakeRecipient);

        mockMvc.perform(put("/api/customer/{cid}/recipient/{rid}", fakeCustomerId, fakeRecipientId)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsBytes(recipientDto)))
                .andExpect(status().isOk());

        ArgumentCaptor<RecipientDto> boundRecipient = ArgumentCaptor.forClass(RecipientDto.class);

        verify(recipientDtoToRecipient, times(1)).convert(boundRecipient.capture());
        verify(customerService, times(1)).addRecipient(fakeCustomerId, fakeRecipient);

        assertEquals((Integer) fakeRecipientId, boundRecipient.getValue().getId());
        assertEquals(fakeRecipientName, boundRecipient.getValue().getName());
        assertEquals(fakeRecipientEmail, boundRecipient.getValue().getEmail());
        assertEquals(fakeRecipientPhone, boundRecipient.getValue().getPhone());
        assertEquals(fakeRecipientDescription, boundRecipient.getValue().getDescription());
    }

    @Test
    public void testEditRecipientWithBadRequest() throws Exception {

        int fakeCustomerId = 999;
        int fakeRecipientId = 888;

        mockMvc.perform(put("/api/customer/{cid}/recipient/{rid}", fakeCustomerId, fakeRecipientId))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testEditRecipientWithMismatchingRecipientId() throws Exception {

        int fakeCustomerId = 999;
        int fakeRecipientId = 888;
        int invalidRecipientId = 777;

        RecipientDto recipientDto = new RecipientDto();
        recipientDto.setId(invalidRecipientId);


        mockMvc.perform(put("/api/customer/{cid}/recipient/{rid}", fakeCustomerId, fakeRecipientId)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsBytes(recipientDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testEditRecipientFromInvalidCustomer() throws Exception {

        int invalidCustomerId = 999;
        int fakeRecipientId = 888;
        Recipient recipient = new Recipient();
        RecipientDto recipientDto = new RecipientDto();


        when(recipientDtoToRecipient.convert(ArgumentMatchers.any(RecipientDto.class))).thenReturn(recipient);
        doThrow(new CustomerNotFoundException()).when(customerService).addRecipient(invalidCustomerId, recipient);

        mockMvc.perform(put("/api/customer/{cid}/recipient/{rid}", invalidCustomerId, fakeRecipientId)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsBytes(recipientDto)))
                .andExpect(status().isNotFound());

        verify(recipientDtoToRecipient, times(1)).convert(ArgumentMatchers.any(RecipientDto.class));
        verify(customerService, times(1)).addRecipient(invalidCustomerId, recipient);
    }

    @Test
    public void testEditRecipientWithInvalidAccount() throws Exception {

        int fakeCustomerId = 999;
        int fakeRecipientId = 888;
        Recipient recipient = new Recipient();
        RecipientDto recipientDto = new RecipientDto();


        when(recipientDtoToRecipient.convert(ArgumentMatchers.any(RecipientDto.class))).thenReturn(recipient);
        doThrow(new AccountNotFoundException()).when(customerService).addRecipient(fakeCustomerId, recipient);

        mockMvc.perform(put("/api/customer/{cid}/recipient/{rid}", fakeCustomerId, fakeRecipientId)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsBytes(recipientDto)))
                .andExpect(status().isNotFound());

        verify(recipientDtoToRecipient, times(1)).convert(ArgumentMatchers.any(RecipientDto.class));
        verify(customerService, times(1)).addRecipient(fakeCustomerId, recipient);
    }

    @Test
    public void testDeleteRecipient() throws Exception {

        int fakeCustomerId = 999;
        int fakeRecipientId = 888;

        mockMvc.perform(delete("/api/customer/{cid}/recipient/{rid}", fakeCustomerId, fakeRecipientId))
                .andExpect(status().isNoContent());

        verify(customerService, times(1)).removeRecipient(fakeCustomerId, fakeRecipientId);
    }

    @Test
    public void testDeleteRecipientFromInvalidCustomer() throws Exception {

        int invalidCustomerId = 999;
        int fakeRecipientId = 888;

        doThrow(new CustomerNotFoundException()).when(customerService).removeRecipient(invalidCustomerId, fakeRecipientId);

        mockMvc.perform(delete("/api/customer/{cid}/recipient/{rid}", invalidCustomerId, fakeRecipientId))
                .andExpect(status().isNotFound());

        verify(customerService, times(1)).removeRecipient(invalidCustomerId, fakeRecipientId);
    }

    @Test
    public void testDeleteInvalidRecipient() throws Exception {

        int fakeCustomerId = 999;
        int invalidRecipientId = 888;

        doThrow(new RecipientNotFoundException()).when(customerService).removeRecipient(fakeCustomerId, invalidRecipientId);

        mockMvc.perform(delete("/api/customer/{cid}/recipient/{rid}", fakeCustomerId, invalidRecipientId))
                .andExpect(status().isNotFound());

        verify(customerService, times(1)).removeRecipient(fakeCustomerId, invalidRecipientId);
    }

    @Test
    public void testDeleteRecipientFromInvalidAccount() throws Exception {

        int fakeCustomerId = 999;
        int fakeRecipientId = 888;

        doThrow(new AccountNotFoundException()).when(customerService).removeRecipient(fakeCustomerId, fakeRecipientId);

        mockMvc.perform(delete("/api/customer/{cid}/recipient/{rid}", fakeCustomerId, fakeRecipientId))
                .andExpect(status().isNotFound());

        verify(customerService, times(1)).removeRecipient(fakeCustomerId, fakeRecipientId);
    }
}
