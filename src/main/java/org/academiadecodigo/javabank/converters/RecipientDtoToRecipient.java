package org.academiadecodigo.javabank.converters;

import org.academiadecodigo.javabank.command.RecipientDto;
import org.academiadecodigo.javabank.persistence.model.Recipient;
import org.academiadecodigo.javabank.services.RecipientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * A {@link Converter} implementation, responsible for {@link RecipientDto} to {@link Recipient} type conversion
 */
@Component
public class RecipientDtoToRecipient implements Converter<RecipientDto, Recipient> {

    private RecipientService recipientService;

    /**
     * Sets the recipient service
     *
     * @param recipientService the recipient service to set
     */
    @Autowired
    public void setRecipientService(RecipientService recipientService) {
        this.recipientService = recipientService;
    }

    /**
     * Converts the recipient DTO into a recipient model object
     * @param recipientDto the recipient DTO
     * @return the recipient
     */
    @Override
    public Recipient convert(RecipientDto recipientDto) {

        Recipient recipient = (recipientDto.getId() != null ? recipientService.get(recipientDto.getId()) : new Recipient());

        recipient.setAccountNumber(recipientDto.getAccountNumber());
        recipient.setName(recipientDto.getName());
        recipient.setEmail(recipientDto.getEmail());
        recipient.setPhone(recipientDto.getPhone());
        recipient.setDescription(recipientDto.getDescription());

        return recipient;
    }
}
