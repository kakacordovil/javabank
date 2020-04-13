package org.academiadecodigo.javabank.converters;

import org.academiadecodigo.javabank.command.RecipientDto;
import org.academiadecodigo.javabank.persistence.model.Recipient;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.mockito.Mockito.*;

public class RecipientToRecipientDtoTest {

    private RecipientToRecipientDto recipientToRecipientDto;

    @Before
    public void setup() {
        recipientToRecipientDto = new RecipientToRecipientDto();
    }

    @Test
    public void testConvert() {

        //setup
        int fakeRecipientId = 9999;
        int fakeAccountNumber = 8888;
        String fakeName = "Fake Name";
        String fakeEmail = "fake@email.com";
        String fakePhone = "912345678";
        String fakeDescription = "Fake Description";

        Recipient fakeRecipient = spy(Recipient.class);
        fakeRecipient.setId(fakeRecipientId);
        fakeRecipient.setAccountNumber(fakeAccountNumber);
        fakeRecipient.setName(fakeName);
        fakeRecipient.setEmail(fakeEmail);
        fakeRecipient.setPhone(fakePhone);
        fakeRecipient.setDescription(fakeDescription);

        //exercise
        RecipientDto recipientDto = recipientToRecipientDto.convert(fakeRecipient);

        //verify
        verify(fakeRecipient, times(1)).getId();
        verify(fakeRecipient, times(1)).getAccountNumber();
        verify(fakeRecipient, times(1)).getName();
        verify(fakeRecipient, times(1)).getEmail();
        verify(fakeRecipient, times(1)).getPhone();
        verify(fakeRecipient, times(1)).getDescription();

        assertTrue(recipientDto.getId() == fakeRecipientId);
        assertTrue(recipientDto.getAccountNumber().equals(fakeAccountNumber));
        assertTrue(recipientDto.getName().equals(fakeName));
        assertTrue(recipientDto.getEmail().equals(fakeEmail));
        assertTrue(recipientDto.getPhone().equals(fakePhone));
        assertTrue(recipientDto.getDescription().equals(fakeDescription));
    }
}
