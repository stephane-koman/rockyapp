package com.rockyapp.rockyappbackend.users.exception;

import com.rockyapp.rockyappbackend.common.exception.ValidationException;
import com.rockyapp.rockyappbackend.utils.helpers.StringHelper;

public class EmailAlreadyExistsException extends ValidationException {

    private static final long serialVersionUID = -1939072464060565201L;
    private static final String ERROR_CODE = "EMAIL_ALREADY_EXISTS_EXCEPTION";
    private static final String ERROR_MESSAGE = "L''email [{0}] existe déjà.";

    public EmailAlreadyExistsException(String email){
        super(ERROR_CODE, StringHelper.formatMessage(ERROR_MESSAGE, email));
    }
}
