package org.academiadecodigo.javabank.services;

import org.academiadecodigo.javabank.persistence.model.Customer;
import org.academiadecodigo.javabank.persistence.model.Recipient;

import java.util.List;
import java.util.Set;

/**
 * Common interface for customer services, provides methods to manage customers
 */
public interface CustomerService {

    /**
     * Gets the customer with the given id
     *
     * @param id the customer id
     * @return the customer
     */
    Customer get(Integer id);

    /**
     * Gets the balance of the customer
     *
     * @param id the customer id
     * @return the balance of the customer with the given id
     */
    double getBalance(Integer id);

    /**
     * Gets a list of the customers
     *
     * @return the customers list
     */
    List<Customer> list();

    /**
     * Gets the set of customer account ids
     *
     * @param id the customer id
     * @return the accounts of the given customer id
     */
    Set<Integer> listCustomerAccountIds(Integer id);

    /**
     * Gets the list of customer recipients
     *
     * @param id the customer id
     * @return the list of recipients of the customer
     */
    List<Recipient> listRecipients(Integer id);

    void deleteCustomer(Integer id);
}
