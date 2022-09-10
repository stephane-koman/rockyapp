package com.rockyapp.rockyappbackend.users.exception;

public class UserNotFoundException extends Exception {

    private static final long serialVersionUID = -1256646265380008470L;

    private static final String ERROR_MESSAGE = "Utilisateur introuvable.";

    public UserNotFoundException(){
        super(ERROR_MESSAGE);
    }
}
