package org.academiadecodigo.javabank.converters;

import org.academiadecodigo.javabank.command.RecipientDto;
import org.academiadecodigo.javabank.persistence.model.Recipient;
import org.academiadecodigo.javabank.services.RecipientService;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.mockito.Mockito.*;

public class RecipientDtoToRecipientTest {

    private RecipientDtoToRecipient recipientDtoToRecipient;
    private RecipientService recipientService;

    @Before
    public void setup() {
        recipientService = mock(RecipientService.class);

        recipientDtoToRecipient = new RecipientDtoToRecipient();
        recipientDtoToRecipient.setRecipientService(recipientService);
    }

    @Test
    public void testConvert() {

        //setup
        int fakeRecipientId = 9999;
        int fakeAccountNumber = 8888;
        String fakeName = "Fake";
        String fakeEmail = "fake@email.com";
        String fakePhone = "912345678";
        String fakeDescription = "Fake Description";

        Recipient fakeRecipient = spy(Recipient.class);
        fakeRecipient.setId(fakeRecipientId);

        RecipientDto fakeRecipientDto = spy(RecipientDto.class);
        fakeRecipientDto.setId(fakeRecipientId);
        fakeRecipientDto.setAccountNumber(fakeAccountNumber);
        fakeRecipientDto.setName(fakeName);
        fakeRecipientDto.setEmail(fakeEmail);
        fakeRecipientDto.setPhone(fakePhone);
        fakeRecipientDto.setDescription(fakeDescription);

        when(recipientService.get(fakeRecipientId)).thenReturn(fakeRecipient);

        //exercise
        Recipient recipient = recipientDtoToRecipient.convert(fakeRecipientDto);

        //verify
        verify(fakeRecipientDto, times(2)).getId();
        verify(fakeRecipientDto, times(1)).getAccountNumber();
        verify(fakeRecipientDto, times(1)).getName();
        verify(fakeRecipientDto, times(1)).getEmail();
        verify(fakeRecipientDto, times(1)).getPhone();
        verify(fakeRecipientDto, times(1)).getDescription();

        assertTrue(recipient.getId().equals(fakeRecipientDto.getId()));
        assertTrue(recipient.getAccountNumber().equals(fakeRecipientDto.getAccountNumber()));
        assertTrue(recipient.getName().equals(fakeRecipientDto.getName()));
        assertTrue(recipient.getEmail().equals(fakeRecipientDto.getEmail()));
        assertTrue(recipient.getPhone().equals(fakeRecipientDto.getPhone()));
        assertTrue(recipient.getDescription().equals(fakeRecipientDto.getDescription()));
    }
}
