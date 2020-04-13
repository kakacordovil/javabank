package org.academiadecodigo.javabank.persistence.jpa.dao;

import org.academiadecodigo.javabank.persistence.dao.jpa.JpaRecipientDao;
import org.academiadecodigo.javabank.persistence.jpa.JpaIntegrationTestHelper;
import org.academiadecodigo.javabank.persistence.model.Recipient;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.Query;
import java.util.List;

import static org.junit.Assert.*;

public class JpaRecipientDaoIntegrationTest extends JpaIntegrationTestHelper {

    private final static Integer INVALID_ID = 9999;
    private final static double DOUBLE_DELTA = 0.1;

    private JpaRecipientDao recipientDao;

    @Before
    public void setup() {

        recipientDao = new JpaRecipientDao();
        recipientDao.setEm(em);
    }

    @Test
    public void testFindById() {

        // setup
        int id = 2;

        // exercise
        Recipient recipient = recipientDao.findById(id);

        // verify
        assertNotNull("Recipient is null", recipient);
        assertEquals("Recipient id is wrong", id, recipient.getId().intValue());
        assertEquals("Recipient name is wrong", "Bruno Ferreira", recipient.getName());
        assertEquals("Recipient email is wrong", "bruno@gmail.com", recipient.getEmail());
        assertEquals("Recipient description is wrong", "My colelague Bruno from A/C", recipient.getDescription());
        assertEquals("Recipient phone is wrong", "777888", recipient.getPhone());

    }

    @Test()
    public void testFindByIdInvalid() {

        // exercise
        Recipient recipient = recipientDao.findById(INVALID_ID);

        // verify
        assertNull("invalid Recipient should not be found", recipient);

    }

    @Test
    public void testFindAll() {

        // exercise
        List<Recipient> recipients = recipientDao.findAll();

        // verify
        assertNotNull("Recipients are null", recipients);
        assertEquals("Number of Recipient is wrong", 2, recipients.size());

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
        List<Recipient> recipients = recipientDao.findAll();

        // verify
        assertNotNull("Recipients are null", recipients);
        assertEquals("Number of Recipients is wrong", 0, recipients.size());

    }


    @Test
    public void testAddRecipient() {

        // setup
        Recipient newRecipient = new Recipient();

        // exercise
        em.getTransaction().begin();
        Recipient addedRecipient = recipientDao.saveOrUpdate(newRecipient);
        em.getTransaction().commit();

        // verify
        assertNotNull("Recipient not added", addedRecipient);
        Recipient recipient = em.find(Recipient.class, addedRecipient.getId());
        assertNotNull("Recipient not found", recipient);

    }

    @Test
    public void testUpdateRecipient() {

        // setup
        int id = 1;
        Recipient recipient = em.find(Recipient.class, id);
        recipient.setAccountNumber(100);

        // exercise
        em.getTransaction().begin();
        recipientDao.saveOrUpdate(recipient);
        em.getTransaction().commit();

        // verify
        recipient = em.find(Recipient.class, id);
        assertEquals("Recipient account number is wrong", 100, recipient.getAccountNumber(), DOUBLE_DELTA);

    }

    @Test
    public void testDeleteOrphanRecipient() {

        // setup
        int id = 2;

        // exercise
        em.getTransaction().begin();
        recipientDao.delete(id);
        em.getTransaction().commit();

        // verify
        Recipient recipient = em.find(Recipient.class, id);
        assertNull("Recipient is not null", recipient);

    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteInvalid() {

        // exercise
        em.getTransaction().begin();
        recipientDao.delete(INVALID_ID);
        em.getTransaction().commit();
    }
}
