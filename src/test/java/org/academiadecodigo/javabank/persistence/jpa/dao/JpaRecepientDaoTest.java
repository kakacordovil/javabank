package org.academiadecodigo.javabank.persistence.jpa.dao;

import org.academiadecodigo.javabank.persistence.dao.jpa.JpaRecipientDao;
import org.academiadecodigo.javabank.persistence.model.Recipient;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class JpaRecepientDaoTest {

    private JpaRecipientDao recipientDao;
    private EntityManager em;

    @Before
    public void setup() {

        em = mock(EntityManager.class);
        recipientDao = new JpaRecipientDao();
        recipientDao.setEm(em);

    }

    @Test
    public void testFindAll() {

        // setup
        List<Recipient> mockRecipients = new ArrayList<>();
        CriteriaQuery criteriaQuery = mock(CriteriaQuery.class);
        CriteriaBuilder criteriaBuilder = mock(CriteriaBuilder.class);
        TypedQuery typedQuery = mock(TypedQuery.class);
        when(em.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        when(criteriaBuilder.createQuery(Recipient.class)).thenReturn(criteriaQuery);
        when(em.createQuery(criteriaQuery)).thenReturn(typedQuery);
        when(em.createQuery(any(CriteriaQuery.class))).thenReturn(typedQuery);
        when(em.createQuery(anyString(), any(Class.class))).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(mockRecipients);

        // exercise
        List<Recipient> recipients = recipientDao.findAll();

        // verify
        verify(typedQuery, times(1)).getResultList();
        assertEquals(mockRecipients, recipients);
    }

    @Test
    public void testFindById() {

        // setup
        int fakeId = 9999;
        Recipient fakeRecipient = new Recipient();
        fakeRecipient.setId(fakeId);
        when(em.find(Recipient.class, fakeId)).thenReturn(fakeRecipient);

        // exercise
        Recipient recipient = recipientDao.findById(fakeId);

        // verify
        verify(em, times(1)).find(Recipient.class, fakeId);
        assertEquals(fakeRecipient, recipient);

    }

    @Test
    public void testSaveOrUpdate() {

        // setup
        Recipient fakeRecipient = new Recipient();
        when(em.merge(any(Recipient.class))).thenReturn(fakeRecipient);

        // exercise
        Recipient recipient = recipientDao.saveOrUpdate(fakeRecipient);

        // verify
        verify(em, times(1)).merge(any(Recipient.class));
        assertEquals(fakeRecipient, recipient);

    }

    @Test
    public void testDelete() {

        // setup
        int fakeId = 9999;
        Recipient fakeRecipient = new Recipient();
        fakeRecipient.setId(fakeId);
        when(em.find(Recipient.class, fakeId)).thenReturn(fakeRecipient);

        // exercise
        recipientDao.delete(fakeId);

        // verify
        verify(em, times(1)).remove(fakeRecipient);

    }
}
