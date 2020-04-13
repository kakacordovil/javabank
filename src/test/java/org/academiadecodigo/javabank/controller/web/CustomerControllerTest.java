package org.academiadecodigo.javabank.controller.web;

import org.academiadecodigo.javabank.command.AccountDto;
import org.academiadecodigo.javabank.command.CustomerDto;
import org.academiadecodigo.javabank.command.RecipientDto;
import org.academiadecodigo.javabank.converters.AccountToAccountDto;
import org.academiadecodigo.javabank.converters.CustomerDtoToCustomer;
import org.academiadecodigo.javabank.converters.CustomerToCustomerDto;
import org.academiadecodigo.javabank.persistence.model.Customer;
import org.academiadecodigo.javabank.persistence.model.Recipient;
import org.academiadecodigo.javabank.persistence.model.account.Account;
import org.academiadecodigo.javabank.services.CustomerService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class CustomerControllerTest {

    @Mock
    private CustomerService customerService;

    @Mock
    private CustomerToCustomerDto customerToCustomerDto;

    @Mock
    private CustomerDtoToCustomer customerDtoToCustomer;

    @Mock
    private AccountToAccountDto accountToAccountDto;

    @InjectMocks
    private CustomerController customerController;

    private MockMvc mockMvc;

    @Before
    public void setup() {

        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();
    }

    @Test
    public void testListCustomers() throws Exception {

        List<Customer> customers = new ArrayList<>();
        customers.add(new Customer());
        customers.add(new Customer());

        when(customerService.list()).thenReturn(customers);

        List<CustomerDto> customerDtos = new ArrayList<>();
        customerDtos.add(new CustomerDto());
        customerDtos.add(new CustomerDto());

        when(customerToCustomerDto.convert(customers)).thenReturn(customerDtos);

        mockMvc.perform(get("/customer/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("customer/list"))
                .andExpect(model().attribute("customers", hasSize(2)));

        mockMvc.perform(get("/customer/"))
                .andExpect(status().isOk())
                .andExpect(view().name("customer/list"))
                .andExpect(model().attribute("customers", hasSize(2)));

        mockMvc.perform(get("/customer"))
                .andExpect(status().isOk())
                .andExpect(view().name("customer/list"))
                .andExpect(model().attribute("customers", hasSize(2)));

        verify(customerService, times(3)).list();
        verify(customerToCustomerDto, times(3)).convert(customers);
    }

    @Test
    public void testShowCustomer() throws Exception {

        int fakeId = 999;
        Customer customer = new Customer();
        customer.setId(fakeId);
        customer.setFirstName("Rui");
        customer.setLastName("Ferrao");
        customer.setEmail("mail@gmail.com");
        customer.setPhone("99999914143");

        when(customerService.get(fakeId)).thenReturn(customer);

        CustomerDto customerDto = new CustomerDto();
        customerDto.setId(customer.getId());
        customerDto.setEmail(customer.getEmail());
        customerDto.setFirstName(customer.getFirstName());
        customerDto.setLastName(customer.getLastName());
        customerDto.setPhone(customer.getPhone());

        when(customerToCustomerDto.convert(customer)).thenReturn(customerDto);

        List<AccountDto> accountDtos = new ArrayList<>();
        accountDtos.add(new AccountDto());
        accountDtos.add(new AccountDto());

        when(accountToAccountDto.convert(ArgumentMatchers.<Account>anyList())).thenReturn(accountDtos);

        mockMvc.perform(get("/customer/" + fakeId))
                .andExpect(status().isOk())
                .andExpect(view().name("customer/show"))
                .andExpect(model().attribute("customer", equalTo(customerDto)))
                .andExpect(model().attribute("accounts", equalTo(accountDtos)));

        verify(customerService, times(1)).get(fakeId);
        verify(customerToCustomerDto).convert(customer);
        verify(accountToAccountDto).convert(ArgumentMatchers.<Account>anyList());

    }

    @Test
    public void testAddCustomer() throws Exception {

        mockMvc.perform(get("/customer/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("customer/add-update"))
                .andExpect(model().attribute("customer", instanceOf(CustomerDto.class)));

        verifyZeroInteractions(customerService);
    }

    @Test
    public void testEditCustomer() throws Exception {

        int fakeID = 9999;
        Customer customer = new Customer();
        CustomerDto customerDto = new CustomerDto();
        customer.setId(fakeID);

        when(customerService.get(fakeID)).thenReturn(customer);
        when(customerToCustomerDto.convert(customer)).thenReturn(customerDto);

        mockMvc.perform(get("/customer/" + fakeID + "/edit/"))
                .andExpect(status().isOk())
                .andExpect(view().name("customer/add-update"))
                .andExpect(model().attribute("customer", equalTo(customerDto)));

        verify(customerService, times(1)).get(fakeID);
    }

    @Test
    public void testSaveCustomer() throws Exception {

        Integer fakeID = 9999;
        String firstName = "Rui";
        String lastName = "Ferrão";
        String phone = "999888";
        String email = "mail@gmail.com";

        CustomerDto customerDto = new CustomerDto();

        customerDto.setFirstName(firstName);
        customerDto.setLastName(lastName);
        customerDto.setPhone(phone);
        customerDto.setEmail(email);

        Customer customer = new Customer();
        customer.setId(fakeID);
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setPhone(phone);
        customer.setEmail(email);

        when(customerDtoToCustomer.convert(ArgumentMatchers.any(CustomerDto.class))).thenReturn(customer);
        when(customerService.save(ArgumentMatchers.any(Customer.class))).thenReturn(customer);

        mockMvc.perform(post("/customer")
                //Action parameter to post so spring can use the right method to process this request
                .param("action", "save")
                .param("id", fakeID.toString())
                .param("firstName", firstName)
                .param("lastName", lastName)
                .param("email", email)
                .param("phone", phone))
                // for debugging
                // .andDo(print())
                .andExpect(status().is3xxRedirection());

        //verify properties of bound command object
        ArgumentCaptor<CustomerDto> boundCustomer = ArgumentCaptor.forClass(CustomerDto.class);

        verify(customerDtoToCustomer, times(1)).convert(boundCustomer.capture());
        verify(customerService, times(1)).save(customer);

        assertEquals(fakeID, boundCustomer.getValue().getId());
        assertEquals(firstName, boundCustomer.getValue().getFirstName());
        assertEquals(lastName, boundCustomer.getValue().getLastName());
        assertEquals(email, boundCustomer.getValue().getEmail());
        assertEquals(phone, boundCustomer.getValue().getPhone());

    }

    @Test
    public void testSaveCustomerCancel() throws Exception {
        mockMvc.perform(post("/customer/")
                //Action parameter to post so spring can use the right method to process this request
                .param("action", "cancel"))
                .andDo(print())
                .andExpect(status().is3xxRedirection());
    }

    @Test
    public void testDeleteCustomer() throws Exception {

        int fakeId = 9999;
        Customer customer = new Customer();

        when(customerService.get(fakeId)).thenReturn(customer);

        mockMvc.perform(get("/customer/" + fakeId + "/delete/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/customer"));

        verify(customerService, times(1)).delete(fakeId);
    }

}
