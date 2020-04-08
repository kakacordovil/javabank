package org.academiadecodigo.javabank.services;

import org.academiadecodigo.javabank.persistence.model.Recipient;

/**
 * Common interface for recipient services, provides a method to get the recipient
 */
public interface RecipientService {

    /**
     * Gets the recipient
     *
     * @param id the recipient id
     * @return the recipient
     */
    Recipient get(Integer id);
}
