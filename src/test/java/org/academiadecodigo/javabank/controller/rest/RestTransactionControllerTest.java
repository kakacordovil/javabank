package org.academiadecodigo.javabank.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.academiadecodigo.javabank.command.AccountTransactionDto;
import org.academiadecodigo.javabank.command.TransferDto;
import org.academiadecodigo.javabank.converters.TransferDtoToTransfer;
import org.academiadecodigo.javabank.domain.Transfer;
import org.academiadecodigo.javabank.exceptions.AccountNotFoundException;
import org.academiadecodigo.javabank.exceptions.CustomerNotFoundException;
import org.academiadecodigo.javabank.exceptions.TransactionInvalidException;
import org.academiadecodigo.javabank.services.AccountService;
import org.academiadecodigo.javabank.services.TransferService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RestTransactionControllerTest {

    @Mock
    private TransferService transferService;

    @Mock
    private TransferDtoToTransfer transferDtoToTransfer;

    @Mock
    private AccountService accountService;

    @InjectMocks
    private RestTransactionController restTransactionController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;


    @Before
    public void setup() {

        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(restTransactionController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testTransfer() throws Exception {

        int fakeCustomerId = 999;
        int fakeSrcId = 66;
        int fakeDstId = 77;
        double amount = 9999;

        TransferDto transferDto = new TransferDto();
        transferDto.setSrcId(fakeSrcId);
        transferDto.setDstId(fakeDstId);
        transferDto.setAmount(Double.toString(amount));

        Transfer transfer = new Transfer();
        transfer.setSrcId(fakeSrcId);
        transfer.setDstId(fakeDstId);
        transfer.setAmount(amount);


        when(transferDtoToTransfer.convert(ArgumentMatchers.any(TransferDto.class))).thenReturn(transfer);

        mockMvc.perform(put("/api/customer/{cid}/transfer", fakeCustomerId)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsBytes(transferDto)))
                .andExpect(status().isOk());

        ArgumentCaptor<TransferDto> boundTransfer = ArgumentCaptor.forClass(TransferDto.class);

        verify(transferDtoToTransfer, times(1)).convert(boundTransfer.capture());
        verify(transferService, times(1)).transfer(transfer, fakeCustomerId);

        assertEquals((Integer) fakeSrcId, boundTransfer.getValue().getSrcId());
        assertEquals((Integer) fakeDstId, boundTransfer.getValue().getDstId());
        assertEquals(Double.toString(amount), boundTransfer.getValue().getAmount());
    }

    @Test
    public void testTransferWithBadRequest() throws Exception {

        int fakeCustomerId = 999;

        mockMvc.perform(put("/api/customer/{cid}/transfer", fakeCustomerId))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testTransferToInvalidCustomer() throws Exception {

        int invalidCustomerId = 888;
        TransferDto transferDto = new TransferDto();
        Transfer transfer = new Transfer();


        when(transferDtoToTransfer.convert(ArgumentMatchers.any(TransferDto.class))).thenReturn(transfer);
        doThrow(new CustomerNotFoundException()).when(transferService).transfer(transfer, invalidCustomerId);

        mockMvc.perform(put("/api/customer/{cid}/transfer", invalidCustomerId)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsBytes(transferDto)))
                .andExpect(status().isNotFound());

        verify(transferDtoToTransfer, times(1)).convert(ArgumentMatchers.any(TransferDto.class));
        verify(transferService, times(1)).transfer(transfer, invalidCustomerId);
    }

    @Test
    public void testTransferToInvalidAccount() throws Exception {

        int fakeCustomerId = 999;
        TransferDto transferDto = new TransferDto();
        Transfer transfer = new Transfer();


        when(transferDtoToTransfer.convert(ArgumentMatchers.any(TransferDto.class))).thenReturn(transfer);
        doThrow(new AccountNotFoundException()).when(transferService).transfer(transfer, fakeCustomerId);

        mockMvc.perform(put("/api/customer/{cid}/transfer", fakeCustomerId)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsBytes(transferDto)))
                .andExpect(status().isNotFound());

        verify(transferDtoToTransfer, times(1)).convert(ArgumentMatchers.any(TransferDto.class));
        verify(transferService, times(1)).transfer(transfer, fakeCustomerId);
    }

    @Test
    public void testTransferInvalidTransaction() throws Exception {

        int fakeCustomerId = 999;
        TransferDto transferDto = new TransferDto();
        Transfer transfer = new Transfer();


        when(transferDtoToTransfer.convert(ArgumentMatchers.any(TransferDto.class))).thenReturn(transfer);
        doThrow(new TransactionInvalidException()).when(transferService).transfer(transfer, fakeCustomerId);

        mockMvc.perform(put("/api/customer/{cid}/transfer", fakeCustomerId)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsBytes(transferDto)))
                .andExpect(status().isBadRequest());

        verify(transferDtoToTransfer, times(1)).convert(ArgumentMatchers.any(TransferDto.class));
        verify(transferService, times(1)).transfer(transfer, fakeCustomerId);
    }

    @Test
    public void testDeposit() throws Exception {

        int fakeCustomerId = 999;
        int fakeAccountId = 888;
        String amount = "777";

        AccountTransactionDto accountTransactionDto = new AccountTransactionDto();
        accountTransactionDto.setId(fakeAccountId);
        accountTransactionDto.setAmount(amount);


        mockMvc.perform(put("/api/customer/{cid}/deposit", fakeCustomerId)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsBytes(accountTransactionDto)))
                .andExpect(status().isOk());

        verify(accountService, times(1)).deposit(fakeAccountId, fakeCustomerId, Double.parseDouble(accountTransactionDto.getAmount()));
    }

    @Test
    public void testDepositWithBadRequest() throws Exception {

        int fakeCustomerId = 999;

        mockMvc.perform(put("/api/customer/{cid}/deposit", fakeCustomerId))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testDepositToInvalidCustomer() throws Exception {

        int invalidCustomerId = 777;
        int fakeAccountId = 888;
        String amount = "777";

        AccountTransactionDto accountTransactionDto = new AccountTransactionDto();
        accountTransactionDto.setId(fakeAccountId);
        accountTransactionDto.setAmount(amount);


        doThrow(new CustomerNotFoundException()).when(accountService).deposit(accountTransactionDto.getId(), invalidCustomerId, Double.parseDouble(accountTransactionDto.getAmount()));

        mockMvc.perform(put("/api/customer/{cid}/deposit", invalidCustomerId)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsBytes(accountTransactionDto)))
                .andExpect(status().isNotFound());

        verify(accountService, times(1)).deposit(accountTransactionDto.getId(), invalidCustomerId, Double.parseDouble(accountTransactionDto.getAmount()));
    }


    @Test
    public void testDepositInvalidAccount() throws Exception {

        int fakeCustomerId = 999;
        int invalidAccountId = 888;
        String amount = "777";

        AccountTransactionDto accountTransactionDto = new AccountTransactionDto();
        accountTransactionDto.setId(invalidAccountId);
        accountTransactionDto.setAmount(amount);


        doThrow(new AccountNotFoundException()).when(accountService).deposit(accountTransactionDto.getId(), fakeCustomerId, Double.parseDouble(accountTransactionDto.getAmount()));

        mockMvc.perform(put("/api/customer/{cid}/deposit", fakeCustomerId)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsBytes(accountTransactionDto)))
                .andExpect(status().isNotFound());

        verify(accountService, times(1)).deposit(accountTransactionDto.getId(), fakeCustomerId, Double.parseDouble(accountTransactionDto.getAmount()));
    }

    @Test
    public void testDepositInvalidTransaction() throws Exception {

        int fakeCustomerId = 999;
        int fakeAccountId = 888;
        String amount = "777";

        AccountTransactionDto accountTransactionDto = new AccountTransactionDto();
        accountTransactionDto.setId(fakeAccountId);
        accountTransactionDto.setAmount(amount);


        doThrow(new TransactionInvalidException()).when(accountService).deposit(accountTransactionDto.getId(), fakeCustomerId, Double.parseDouble(accountTransactionDto.getAmount()));

        mockMvc.perform(put("/api/customer/{cid}/deposit", fakeCustomerId)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsBytes(accountTransactionDto)))
                .andExpect(status().isBadRequest());

        verify(accountService, times(1)).deposit(accountTransactionDto.getId(), fakeCustomerId, Double.parseDouble(accountTransactionDto.getAmount()));
    }

    @Test
    public void testWithdraw() throws Exception {

        int fakeCustomerId = 999;
        int fakeAccountId = 888;
        String amount = "777";

        AccountTransactionDto accountTransactionDto = new AccountTransactionDto();
        accountTransactionDto.setId(fakeAccountId);
        accountTransactionDto.setAmount(amount);


        mockMvc.perform(put("/api/customer/{cid}/withdraw", fakeCustomerId)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsBytes(accountTransactionDto)))
                .andExpect(status().isOk());

        verify(accountService, times(1)).withdraw(fakeAccountId, fakeCustomerId, Double.parseDouble(accountTransactionDto.getAmount()));

    }

    @Test
    public void testWithdrawWithBadRequest() throws Exception {

        int fakeCustomerId = 999;

        mockMvc.perform(put("/api/customer/{cid}/withdraw", fakeCustomerId))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testWithdrawToInvalidCustomer() throws Exception {

        int invalidCustomerId = 777;
        int fakeAccountId = 888;
        String amount = "777";

        AccountTransactionDto accountTransactionDto = new AccountTransactionDto();
        accountTransactionDto.setId(fakeAccountId);
        accountTransactionDto.setAmount(amount);


        doThrow(new CustomerNotFoundException()).when(accountService).withdraw(accountTransactionDto.getId(), invalidCustomerId, Double.parseDouble(accountTransactionDto.getAmount()));

        mockMvc.perform(put("/api/customer/{cid}/withdraw", invalidCustomerId)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsBytes(accountTransactionDto)))
                .andExpect(status().isNotFound());

        verify(accountService, times(1)).withdraw(accountTransactionDto.getId(), invalidCustomerId, Double.parseDouble(accountTransactionDto.getAmount()));
    }


    @Test
    public void testWithdrawInvalidAccount() throws Exception {

        int fakeCustomerId = 999;
        int invalidAccountId = 888;
        String amount = "777";

        AccountTransactionDto accountTransactionDto = new AccountTransactionDto();
        accountTransactionDto.setId(invalidAccountId);
        accountTransactionDto.setAmount(amount);


        doThrow(new AccountNotFoundException()).when(accountService).withdraw(accountTransactionDto.getId(), fakeCustomerId, Double.parseDouble(accountTransactionDto.getAmount()));

        mockMvc.perform(put("/api/customer/{cid}/withdraw", fakeCustomerId)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsBytes(accountTransactionDto)))
                .andExpect(status().isNotFound());

        verify(accountService, times(1)).withdraw(accountTransactionDto.getId(), fakeCustomerId, Double.parseDouble(accountTransactionDto.getAmount()));
    }

    @Test
    public void testWithdrawInvalidTransaction() throws Exception {

        int fakeCustomerId = 999;
        int fakeAccountId = 888;
        String amount = "777";

        AccountTransactionDto accountTransactionDto = new AccountTransactionDto();
        accountTransactionDto.setId(fakeAccountId);
        accountTransactionDto.setAmount(amount);


        doThrow(new TransactionInvalidException()).when(accountService).withdraw(accountTransactionDto.getId(), fakeCustomerId, Double.parseDouble(accountTransactionDto.getAmount()));

        mockMvc.perform(put("/api/customer/{cid}/withdraw", fakeCustomerId)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsBytes(accountTransactionDto)))
                .andExpect(status().isBadRequest());

        verify(accountService, times(1)).withdraw(accountTransactionDto.getId(), fakeCustomerId, Double.parseDouble(accountTransactionDto.getAmount()));
    }

}
