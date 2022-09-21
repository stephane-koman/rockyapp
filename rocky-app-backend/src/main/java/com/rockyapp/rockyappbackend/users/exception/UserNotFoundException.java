package com.rockyapp.rockyappbackend.users.exception;

import com.rockyapp.rockyappbackend.common.exception.ValidationException;

public class UserNotFoundException extends ValidationException {

    private static final long serialVersionUID = -1256646265380008470L;

    private static final String ERROR_MESSAGE = "Utilisateur introuvable.";

    public UserNotFoundException(){
        super(ERROR_MESSAGE);
    }
}
