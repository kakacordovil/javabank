package org.academiadecodigo.javabank.persistence.jpa.dao;

import org.academiadecodigo.javabank.persistence.dao.jpa.JpaCustomerDao;
import org.academiadecodigo.javabank.persistence.jpa.JpaIntegrationTestHelper;
import org.academiadecodigo.javabank.persistence.model.Customer;
import org.academiadecodigo.javabank.persistence.model.account.Account;
import org.academiadecodigo.javabank.persistence.model.account.CheckingAccount;
import org.academiadecodigo.javabank.persistence.model.account.SavingsAccount;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.Query;
import java.util.List;

import static org.junit.Assert.*;

public class JpaCustomerDaoIntegrationTest extends JpaIntegrationTestHelper {

    private final static Integer INVALID_ID = 9999;
    private final static double DOUBLE_DELTA = 0.1;

    private JpaCustomerDao customerDao;

    @Before
    public void setup() {
        customerDao = new JpaCustomerDao();
        customerDao.setEm(em);
    }

    @Test
    public void testFindById() {

        // setup
        int id = 1;

        // exercise
        Customer customer = customerDao.findById(id);

        // verify
        assertNotNull("Customer is null", customer);
        assertEquals("Customer id is wrong", id, customer.getId().intValue());
        assertEquals("Customer name is wrong", "Rui", customer.getFirstName());

    }

    @Test()
    public void testFindByIdInvalid() {

        // exercise
        Customer customer = customerDao.findById(INVALID_ID);

        // verify
        assertNull("invalid customer should not be found", customer);

    }

    @Test
    public void testFindAll() {

        // exercise
        List<Customer> customers = customerDao.findAll();

        // verify
        assertNotNull("customers are null", customers);
        assertEquals("Number of customer is wrong", 4, customers.size());

    }

    @Test
    public void testFindAllFail() {

        // setup
        em.getTransaction().begin();
        Query query = em.createQuery("delete from Account ");
        query.executeUpdate();
        query = em.createQuery("delete from Recipient ");
        query.executeUpdate();
        query = em.createQuery("delete from Customer");
        query.executeUpdate();
        em.getTransaction().commit();

        // exercise
        List<Customer> customers = customerDao.findAll();

        // verify
        assertNotNull("Customers are null", customers);
        assertEquals("Number of customers is wrong", 0, customers.size());

    }

    @Test
    public void testAddCustomerNoAccounts() {

        // setup
        String name = "New Customer name";
        Customer newCustomer = new Customer();
        newCustomer.setFirstName(name);

        // exercise
        em.getTransaction().begin();
        Customer addedCustomer = customerDao.saveOrUpdate(newCustomer);
        em.getTransaction().commit();

        // verify
        assertNotNull("customer not added", addedCustomer);
        Customer customer = em.find(Customer.class, addedCustomer.getId());
        assertNotNull("Customer not found", customer);

    }

    @Test
    public void testAddCustomerWithAccounts() {

        // setup
        double caBalance = 100;
        double saBalance = 101;
        Account ca = new CheckingAccount();
        Account sa = new SavingsAccount();
        ca.credit(caBalance);
        sa.credit(saBalance);

        Customer newCustomer = new Customer();
        newCustomer.addAccount(ca);
        newCustomer.addAccount(sa);

        // exercise
        em.getTransaction().begin();
        Customer addedCustomer = customerDao.saveOrUpdate(newCustomer);
        em.getTransaction().commit();

        // verify
        assertNotNull("customer not added", addedCustomer);
        Customer customer = em.find(Customer.class, addedCustomer.getId());
        assertNotNull("customer not found", addedCustomer);
        assertNotNull("customer accounts not found", customer.getAccounts());
        assertEquals("customer number of accounts wrong", newCustomer.getAccounts().size(), customer.getAccounts().size());
        assertEquals("first account balance is wrong", caBalance, customer.getAccounts().get(0).getBalance(), DOUBLE_DELTA);
        assertEquals("second account balance is wrong", saBalance, customer.getAccounts().get(1).getBalance(), DOUBLE_DELTA);

    }

    @Test
    public void testUpdateCustomer() {

        // setup
        int id = 1;
        String name = "updated customer";
        Customer customer = em.find(Customer.class, id);
        customer.setFirstName(name);

        // exercise
        em.getTransaction().begin();
        customerDao.saveOrUpdate(customer);
        em.getTransaction().commit();

        // verify
        customer = em.find(Customer.class, id);
        assertEquals("customer name is wrong", name, customer.getFirstName());

    }

    @Test
    public void testUpdateCustomerWithAccounts() {

        // setup
        int id = 1;
        String name = "updated customer";
        Customer existingCustomer = em.find(Customer.class, id);
        existingCustomer.setFirstName(name);
        existingCustomer.getAccounts().get(0).canCredit(100);

        // exercise
        em.getTransaction().begin();
        customerDao.saveOrUpdate(existingCustomer);
        em.getTransaction().commit();

        // verify
        Customer customer = em.find(Customer.class, id);
        assertEquals("customer name is wrong", name, customer.getFirstName());
        assertEquals("number of accounts is wrong", 2, customer.getAccounts().size());
        assertEquals("account balance is wrong", 100, customer.getAccounts().get(0).getBalance(), DOUBLE_DELTA);

    }

    @Test
    public void testUpdatedCustomerOrphanAccountDelete() {

        // setup
        int id = 1;
        String name = "updated customer";
        Customer existingCustomer = em.find(Customer.class, id);
        existingCustomer.setFirstName(name);
        existingCustomer.removeAccount(existingCustomer.getAccounts().get(1));

        // exercise
        em.getTransaction().begin();
        customerDao.saveOrUpdate(existingCustomer);
        em.getTransaction().commit();

        // verify
        Customer customer = em.find(Customer.class, id);
        assertEquals("customer name is wrong", name, customer.getFirstName());
        assertEquals("number of accounts is wrong", 1, customer.getAccounts().size());
        assertEquals("account balance is wrong", 100, customer.getAccounts().get(0).getBalance(), DOUBLE_DELTA);

    }

    @Test
    public void testDeleteCustomer() {

        // setup
        int id = 1;

        // exercise
        em.getTransaction().begin();
        customerDao.delete(id);
        em.getTransaction().commit();

        // verify
        Customer customer = em.find(Customer.class, id);
        assertNull("should be null", customer);
    }

    @Test
    public void testDeleteCustomerNoAccounts() {

        // setup
        int id = 4;

        // exercise
        em.getTransaction().begin();
        customerDao.delete(id);
        em.getTransaction().commit();

        // verify
        Customer customer = em.find(Customer.class, id);
        assertNull("should be null", customer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteInvalid() {

        // exercise
        em.getTransaction().begin();
        customerDao.delete(INVALID_ID);
        em.getTransaction().commit();
    }
}
