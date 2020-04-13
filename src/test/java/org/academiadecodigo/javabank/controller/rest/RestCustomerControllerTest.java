package org.academiadecodigo.javabank.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.academiadecodigo.javabank.command.CustomerDto;
import org.academiadecodigo.javabank.converters.CustomerDtoToCustomer;
import org.academiadecodigo.javabank.converters.CustomerToCustomerDto;
import org.academiadecodigo.javabank.exceptions.AssociationExistsException;
import org.academiadecodigo.javabank.exceptions.CustomerNotFoundException;
import org.academiadecodigo.javabank.persistence.model.Customer;
import org.academiadecodigo.javabank.services.CustomerService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class RestCustomerControllerTest {

    @Mock
    private CustomerService customerService;

    @Mock
    private CustomerToCustomerDto customerToCustomerDto;

    @Mock
    private CustomerDtoToCustomer customerDtoToCustomer;


    @InjectMocks
    private RestCustomerController restCustomerController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Before
    public void setup() {

        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(restCustomerController).build();
        objectMapper = new ObjectMapper();

    }

    @Test
    public void testListCustomers() throws Exception {

        Integer fakeID = 999;
        String firstName = "Rui";
        String lastName = "Ferrão";
        String phone = "777888999";
        String email = "mail@gmail.com";

        Customer customer = new Customer();
        customer.setId(fakeID);
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setPhone(phone);
        customer.setEmail(email);

        CustomerDto customerDto = new CustomerDto();
        customerDto.setId(fakeID);
        customerDto.setFirstName(firstName);
        customerDto.setLastName(lastName);
        customerDto.setPhone(phone);
        customerDto.setEmail(email);

        List<Customer> customers = new ArrayList<>();
        customers.add(customer);


        when(customerService.list()).thenReturn(customers);
        when(customerToCustomerDto.convert(customer)).thenReturn(customerDto);

        mockMvc.perform(get("/api/customer/"))
                .andExpect(jsonPath("$[0].id").value(fakeID))
                .andExpect(jsonPath("$[0].firstName").value(firstName))
                .andExpect(jsonPath("$[0].lastName").value(lastName))
                .andExpect(jsonPath("$[0].email").value(email))
                .andExpect(jsonPath("$[0].phone").value(phone))
                .andExpect(status().isOk());

        verify(customerService, times(1)).list();
        verify(customerToCustomerDto, times(1)).convert(customer);
    }

    @Test
    public void testShowCustomer() throws Exception {

        Integer fakeID = 999;
        String firstName = "Rui";
        String lastName = "Ferrão";
        String phone = "777888999";
        String email = "mail@gmail.com";

        Customer customer = new Customer();
        customer.setId(fakeID);
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setPhone(phone);
        customer.setEmail(email);

        CustomerDto customerDto = new CustomerDto();
        customerDto.setId(fakeID);
        customerDto.setFirstName(firstName);
        customerDto.setLastName(lastName);
        customerDto.setPhone(phone);
        customerDto.setEmail(email);


        when(customerService.get(fakeID)).thenReturn(customer);
        when(customerToCustomerDto.convert(customer)).thenReturn(customerDto);

        mockMvc.perform(get("/api/customer/{id}", customer.getId()))
                .andExpect(jsonPath("$.id").value(fakeID))
                .andExpect(jsonPath("$.firstName").value(firstName))
                .andExpect(jsonPath("$.lastName").value(lastName))
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.phone").value(phone))
                .andExpect(status().isOk());

        verify(customerService, times(1)).get(fakeID);
        verify(customerToCustomerDto, times(1)).convert(customer);
    }

    @Test
    public void testShowInvalidCustomer() throws Exception {

        int invalidId = 888;
        int fakeId = 999;
        Customer customer = new Customer();
        customer.setId(fakeId);


        when(customerService.get(invalidId)).thenReturn(null);

        mockMvc.perform(get("/api/customer/{id}", invalidId))
                .andExpect(status().isNotFound());

        verify(customerService, times(1)).get(invalidId);
    }

    @Test
    public void testAddCustomer() throws Exception {

        int fakeId = 999;
        String firstName = "Rui";
        String lastName = "Ferrão";
        String phone = "999888999";
        String email = "mail@gmail.com";

        CustomerDto customerDto = new CustomerDto();
        customerDto.setFirstName(firstName);
        customerDto.setLastName(lastName);
        customerDto.setPhone(phone);
        customerDto.setEmail(email);

        Customer customer = new Customer();
        customer.setId(fakeId);
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setPhone(phone);
        customer.setEmail(email);


        when(customerDtoToCustomer.convert(ArgumentMatchers.any(CustomerDto.class))).thenReturn(customer);
        when(customerService.save(customer)).thenReturn(customer);

        mockMvc.perform(post("/api/customer/")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsBytes(customerDto)))
                .andExpect(status().isCreated())
                .andExpect(header().string("location", containsString("http://localhost/api/customer/" + customer.getId())));

        //verify properties of bound command object
        ArgumentCaptor<CustomerDto> boundCustomer = ArgumentCaptor.forClass(CustomerDto.class);

        verify(customerDtoToCustomer, times(1)).convert(boundCustomer.capture());
        verify(customerService, times(1)).save(customer);

        assertEquals(null, boundCustomer.getValue().getId());
        assertEquals(firstName, boundCustomer.getValue().getFirstName());
        assertEquals(lastName, boundCustomer.getValue().getLastName());
        assertEquals(email, boundCustomer.getValue().getEmail());
        assertEquals(phone, boundCustomer.getValue().getPhone());
    }

    @Test
    public void testAddCustomerWithBadRequest() throws Exception {

        mockMvc.perform(post("/api/customer/"))
                .andExpect(status().isBadRequest());
    }


    @Test
    public void testEditCustomer() throws Exception {

        int fakeId = 999;
        String firstName = "Rui";
        String lastName = "Ferrão";
        String phone = "999888999";
        String email = "mail@gmail.com";

        CustomerDto customerDto = new CustomerDto();
        customerDto.setId(fakeId);
        customerDto.setFirstName(firstName);
        customerDto.setLastName(lastName);
        customerDto.setPhone(phone);
        customerDto.setEmail(email);

        Customer customer = new Customer();
        customer.setId(fakeId);
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setPhone(phone);
        customer.setEmail(email);


        when(customerDtoToCustomer.convert(ArgumentMatchers.any(CustomerDto.class))).thenReturn(customer);
        when(customerService.save(customer)).thenReturn(customer);
        when(customerService.get(fakeId)).thenReturn(customer);

        mockMvc.perform(put("/api/customer/{id}", fakeId)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsBytes(customerDto)))
                .andExpect(status().isOk());

        ArgumentCaptor<CustomerDto> boundCustomer = ArgumentCaptor.forClass(CustomerDto.class);

        verify(customerDtoToCustomer, times(1)).convert(boundCustomer.capture());
        verify(customerService, times(1)).save(customer);

        assertEquals((Integer) fakeId, boundCustomer.getValue().getId());
        assertEquals(firstName, boundCustomer.getValue().getFirstName());
        assertEquals(lastName, boundCustomer.getValue().getLastName());
        assertEquals(email, boundCustomer.getValue().getEmail());
        assertEquals(phone, boundCustomer.getValue().getPhone());
    }

    @Test
    public void testEditCustomerWithBadRequest() throws Exception {

        int invalidId = 888;
        int fakeId = 999;
        String firstName = "Rui";
        String lastName = "Ferrão";
        String phone = "999888999";
        String email = "mail@gmail.com";

        CustomerDto customerDto = new CustomerDto();
        customerDto.setId(fakeId);
        customerDto.setFirstName(firstName);
        customerDto.setLastName(lastName);
        customerDto.setPhone(phone);
        customerDto.setEmail(email);


        mockMvc.perform(put("/api/customer/{id}", invalidId)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsBytes(customerDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testEditInvalidCustomer() throws Exception {

        int invalidId = 999;
        String firstName = "Rui";
        String lastName = "Ferrão";
        String phone = "999888999";
        String email = "mail@gmail.com";

        CustomerDto customerDto = new CustomerDto();
        customerDto.setId(invalidId);
        customerDto.setFirstName(firstName);
        customerDto.setLastName(lastName);
        customerDto.setPhone(phone);
        customerDto.setEmail(email);

        Customer customer = new Customer();
        customer.setId(invalidId);
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setPhone(phone);
        customer.setEmail(email);


        when(customerDtoToCustomer.convert(ArgumentMatchers.any(CustomerDto.class))).thenReturn(customer);
        when(customerService.save(customer)).thenReturn(customer);
        when(customerService.get(invalidId)).thenReturn(null);

        mockMvc.perform(put("/api/customer/{id}", invalidId)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsBytes(customerDto)))
                .andExpect(status().isNotFound());
    }


    @Test
    public void testDeleteCustomer() throws Exception {

        int fakeId = 999;

        mockMvc.perform(delete("/api/customer/{id}", fakeId))
                .andExpect(status().isNoContent());

        verify(customerService, times(1)).delete(fakeId);
    }

    @Test
    public void testDeleteInvalidCustomer() throws Exception {

        int invalidId = 888;

        doThrow(new CustomerNotFoundException()).when(customerService).delete(ArgumentMatchers.any(Integer.class));

        mockMvc.perform(delete("/api/customer/{id}", invalidId))
                .andExpect(status().isNotFound());

        verify(customerService, times(1)).delete(invalidId);
    }

    @Test
    public void testDeleteCustomerWithOpenAccount() throws Exception {

        int fakeId = 999;

        doThrow(new AssociationExistsException()).when(customerService).delete(ArgumentMatchers.any(Integer.class));

        mockMvc.perform(delete("/api/customer/{id}", fakeId))
                .andExpect(status().isBadRequest());

        verify(customerService, times(1)).delete(fakeId);
    }
}
