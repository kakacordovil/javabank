package org.academiadecodigo.javabank.services;

import org.academiadecodigo.javabank.persistence.dao.RecipientDao;
import org.academiadecodigo.javabank.persistence.model.Recipient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * A {@link RecipientService} implementation
 */
@Service
public class RecipientServiceImpl implements RecipientService {

    private RecipientDao recipientDao;

    /**
     * Sets the recipient data access object
     *
     * @param recipientDao the recipient DAO to set
     */
    @Autowired
    public void setRecipientDao(RecipientDao recipientDao) {
        this.recipientDao = recipientDao;
    }

    /**
     * @see RecipientService#get(Integer)
     */
    @Override
    public Recipient get(Integer id) {
        return recipientDao.findById(id);
    }
}
