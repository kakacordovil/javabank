package org.academiadecodigo.javabank.services;

import org.academiadecodigo.javabank.persistence.dao.RecipientDao;
import org.academiadecodigo.javabank.persistence.model.Recipient;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RecipientServiceImplTest {

    private RecipientDao recipientDao;
    private RecipientServiceImpl recipientService;

    @Before
    public void setup() {

        recipientDao = mock(RecipientDao.class);

        recipientService = new RecipientServiceImpl();
        recipientService.setRecipientDao(recipientDao);
    }

    @Test
    public void testGet() {

        // setup
        int fakeId = 9999;
        Recipient fakeRecipient = mock(Recipient.class);
        when(recipientDao.findById(fakeId)).thenReturn(fakeRecipient);

        // exercise
        Recipient recipient = recipientService.get(fakeId);

        // verify
        assertEquals(fakeRecipient, recipient);

    }
}
