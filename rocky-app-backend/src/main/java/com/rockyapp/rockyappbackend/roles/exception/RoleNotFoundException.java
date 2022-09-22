package com.rockyapp.rockyappbackend.roles.exception;

import com.rockyapp.rockyappbackend.common.exception.ValidationException;

public class RoleNotFoundException extends ValidationException {

    private static final long serialVersionUID = -1623151985808793327L;
    private static final String ERROR_MESSAGE = "Role introuvable.";

    public RoleNotFoundException(){
        super(ERROR_MESSAGE);
    }
}
