package com.rockyapp.rockyappbackend.roles.exception;

public class RoleNotFoundException extends Exception {

    private static final long serialVersionUID = -1256646265380008470L;

    private static final String ERROR_MESSAGE = "Role introuvable.";

    public RoleNotFoundException(){
        super(ERROR_MESSAGE);
    }
}
