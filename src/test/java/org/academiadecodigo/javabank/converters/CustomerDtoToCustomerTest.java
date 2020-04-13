package org.academiadecodigo.javabank.converters;

import org.academiadecodigo.javabank.command.CustomerDto;
import org.academiadecodigo.javabank.persistence.model.Customer;
import org.academiadecodigo.javabank.services.CustomerService;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.mockito.Mockito.*;

public class CustomerDtoToCustomerTest {

    private CustomerDtoToCustomer customerDtoToCustomer;
    private CustomerService customerService;

    @Before
    public void setup() {
        customerService = mock(CustomerService.class);

        customerDtoToCustomer = new CustomerDtoToCustomer();
        customerDtoToCustomer.setCustomerService(customerService);
    }

    @Test
    public void testConvert() {

        //setup
        int fakeCustomerId = 9999;
        String fakeFirstName = "Fakey";
        String fakeLastName = "Fake";
        String fakeEmail = "fake@email.com";
        String fakePhone = "912345678";

        Customer fakeCustomer = spy(Customer.class);
        fakeCustomer.setId(fakeCustomerId);

        CustomerDto fakeCustomerDto = new CustomerDto();
        fakeCustomerDto.setId(fakeCustomerId);
        fakeCustomerDto.setFirstName(fakeFirstName);
        fakeCustomerDto.setLastName(fakeLastName);
        fakeCustomerDto.setEmail(fakeEmail);
        fakeCustomerDto.setPhone(fakePhone);

        when(customerService.get(fakeCustomerId)).thenReturn(fakeCustomer);

        //exercise
        Customer customer = customerDtoToCustomer.convert(fakeCustomerDto);

        //verify
        verify(customerService, times(1)).get(fakeCustomerId);
        verify(fakeCustomer, times(1)).setFirstName(fakeFirstName);
        verify(fakeCustomer, times(1)).setLastName(fakeLastName);
        verify(fakeCustomer, times(1)).setEmail(fakeEmail);
        verify(fakeCustomer, times(1)).setPhone(fakePhone);

        assertTrue(customer.getId() == fakeCustomerId);
        assertTrue(customer.getFirstName().equals(fakeFirstName));
        assertTrue(customer.getLastName().equals(fakeLastName));
        assertTrue(customer.getEmail().equals(fakeEmail));
        assertTrue(customer.getPhone().equals(fakePhone));
    }
}
