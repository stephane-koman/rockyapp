package com.rockyapp.rockyappbackend.users.exception;

import com.rockyapp.rockyappbackend.utils.helpers.StringHelper;

public class EmailAlreadyExistsException extends Exception {

    private static final long serialVersionUID = -1939072464060565201L;
    private static final String ERROR_MESSAGE = "L'email % existe déjà.";

    public EmailAlreadyExistsException(String email){
        super(StringHelper.replace(ERROR_MESSAGE, email));
    }
}
