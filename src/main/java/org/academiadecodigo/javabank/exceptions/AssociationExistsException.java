package org.academiadecodigo.javabank.exceptions;

import org.academiadecodigo.javabank.errors.ErrorMessage;

/**
 * Thrown to indicate that an association still exists
 */
public class AssociationExistsException extends JavaBankException {

    /**
     * @see JavaBankException#JavaBankException(String)
     */
    public AssociationExistsException() {
        super(ErrorMessage.ASSOCIATION_EXISTS);
    }
}
