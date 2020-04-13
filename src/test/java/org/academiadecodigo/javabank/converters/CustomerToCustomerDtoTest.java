package org.academiadecodigo.javabank.converters;

import org.academiadecodigo.javabank.command.CustomerDto;
import org.academiadecodigo.javabank.persistence.model.Customer;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.mockito.Mockito.*;

public class CustomerToCustomerDtoTest {

    private CustomerToCustomerDto customerToCustomerDto;

    @Before
    public void setup() {
        customerToCustomerDto = new CustomerToCustomerDto();
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
        fakeCustomer.setFirstName(fakeFirstName);
        fakeCustomer.setLastName(fakeLastName);
        fakeCustomer.setEmail(fakeEmail);
        fakeCustomer.setPhone(fakePhone);

        //exercise
        CustomerDto customerDto = customerToCustomerDto.convert(fakeCustomer);

        //verify
        verify(fakeCustomer, times(1)).getId();
        verify(fakeCustomer, times(1)).getFirstName();
        verify(fakeCustomer, times(1)).getLastName();
        verify(fakeCustomer, times(1)).getEmail();
        verify(fakeCustomer, times(1)).getPhone();

        assertTrue(customerDto.getId() == fakeCustomerId);
        assertTrue(customerDto.getFirstName().equals(fakeFirstName));
        assertTrue(customerDto.getLastName().equals(fakeLastName));
        assertTrue(customerDto.getEmail().equals(fakeEmail));
        assertTrue(customerDto.getPhone().equals(fakePhone));
    }
}
