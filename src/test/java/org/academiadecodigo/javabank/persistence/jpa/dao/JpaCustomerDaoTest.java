package org.academiadecodigo.javabank.persistence.jpa.dao;

import org.academiadecodigo.javabank.persistence.dao.jpa.JpaCustomerDao;
import org.academiadecodigo.javabank.persistence.model.Customer;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class JpaCustomerDaoTest {

    private JpaCustomerDao customerDao;
    private EntityManager em;

    @Before
    public void setup() {

        em = mock(EntityManager.class);

        customerDao = new JpaCustomerDao();
        customerDao.setEm(em);

    }

    @Test
    public void testFindAll() {

        // setup
        List<Customer> mockCustomers = new ArrayList<>();
        CriteriaQuery criteriaQuery = mock(CriteriaQuery.class);
        CriteriaBuilder criteriaBuilder = mock(CriteriaBuilder.class);
        TypedQuery typedQuery = mock(TypedQuery.class);
        when(em.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        when(criteriaBuilder.createQuery(Customer.class)).thenReturn(criteriaQuery);
        when(em.createQuery(criteriaQuery)).thenReturn(typedQuery);
        when(em.createQuery(anyString(), any(Class.class))).thenReturn(typedQuery);
        when(em.createQuery(any(CriteriaQuery.class))).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(mockCustomers);

        // exercise
        List<Customer> customers = customerDao.findAll();

        // verify
        verify(typedQuery, times(1)).getResultList();
        assertEquals(mockCustomers, customers);
    }

    @Test
    public void testFindById() {

        // setup
        int fakeId = 9999;
        Customer fakeCustomer = new Customer();
        fakeCustomer.setId(fakeId);
        when(em.find(Customer.class, fakeId)).thenReturn(fakeCustomer);

        // exercise
        Customer customer = customerDao.findById(fakeId);

        // verify
        verify(em, times(1)).find(Customer.class, fakeId);
        assertEquals(fakeCustomer, customer);

    }

    @Test
    public void testSaveOrUpdate() {

        // setup
        Customer fakeCustomer = new Customer();
        when(em.merge(any(Customer.class))).thenReturn(fakeCustomer);

        // exercise
        Customer customer = customerDao.saveOrUpdate(fakeCustomer);

        // verify
        verify(em, times(1)).merge(any(Customer.class));
        assertEquals(fakeCustomer, customer);

    }

    @Test
    public void testDelete() {

        // setup
        int fakeId = 9999;
        Customer fakeCustomer = new Customer();
        fakeCustomer.setId(fakeId);
        when(em.find(Customer.class, fakeId)).thenReturn(fakeCustomer);

        // exercise
        customerDao.delete(fakeId);

        // verify
        verify(em, times(1)).remove(fakeCustomer);

    }

}
