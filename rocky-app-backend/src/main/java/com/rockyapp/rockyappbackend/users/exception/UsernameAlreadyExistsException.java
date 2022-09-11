package com.rockyapp.rockyappbackend.users.exception;

import com.rockyapp.rockyappbackend.utils.helpers.StringHelper;

public class UsernameAlreadyExistsException extends Exception {

    private static final long serialVersionUID = -1939072464060565201L;
    private static final String ERROR_MESSAGE = "Le username % existe déjà.";

    public UsernameAlreadyExistsException(String username){
        super(StringHelper.replace(ERROR_MESSAGE, username));
    }
}
