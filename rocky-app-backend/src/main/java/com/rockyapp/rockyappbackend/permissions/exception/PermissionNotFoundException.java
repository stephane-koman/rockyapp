package com.rockyapp.rockyappbackend.permissions.exception;

public class PermissionNotFoundException extends Exception {

    private static final long serialVersionUID = -1256646265380008470L;

    private static final String ERROR_MESSAGE = "Permission introuvable.";

    public PermissionNotFoundException(){
        super(ERROR_MESSAGE);
    }
}
