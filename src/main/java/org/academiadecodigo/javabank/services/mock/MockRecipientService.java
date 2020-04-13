package org.academiadecodigo.javabank.services.mock;

import org.academiadecodigo.javabank.persistence.model.Recipient;
import org.academiadecodigo.javabank.services.RecipientService;

/**
 * A mock {@link RecipientService} implementation
 */
public class MockRecipientService extends AbstractMockService<Recipient> implements RecipientService {

    /**
     * @see RecipientService#get(Integer)
     */
    @Override
    public Recipient get(Integer id) {

        return modelMap.get(id);
    }
}
