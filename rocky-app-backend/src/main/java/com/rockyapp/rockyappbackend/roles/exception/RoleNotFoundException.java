package com.rockyapp.rockyappbackend.roles.exception;

public class RoleNotFoundException extends Exception {

    private static final long serialVersionUID = -1623151985808793327L;
    private static final String ERROR_MESSAGE = "Role introuvable.";


    public RoleNotFoundException(){
        super(ERROR_MESSAGE);
    }
}
