package org.academiadecodigo.javabank.converters;

import org.academiadecodigo.javabank.command.RecipientDto;
import org.academiadecodigo.javabank.persistence.model.Recipient;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * A {@link Converter} implementation, responsible for {@link Recipient} to {@link RecipientDto} type conversion
 */
@Component
public class RecipientToRecipientDto extends AbstractConverter<Recipient, RecipientDto> {

    /**
     * Converts the recipient model object into a recipient DTO
     * @param recipient the recipient
     * @return the recipient DTO
     */
    @Override
    public RecipientDto convert(Recipient recipient) {

        RecipientDto recipientDto = new RecipientDto();
        recipientDto.setId(recipient.getId());
        recipientDto.setAccountNumber(recipient.getAccountNumber());
        recipientDto.setName(recipient.getName());
        recipientDto.setEmail(recipient.getEmail());
        recipientDto.setPhone(recipient.getPhone());
        recipientDto.setDescription(recipient.getDescription());

        return recipientDto;
    }
}
