package com.rockyapp.rockyappbackend.permissions.exception;

import com.rockyapp.rockyappbackend.common.exception.ValidationException;

public class PermissionNotFoundException extends ValidationException {

    private static final long serialVersionUID = -1027178301899361626L;
    private static final String ERROR_MESSAGE = "Permission introuvable.";

    public PermissionNotFoundException(){
        super(ERROR_MESSAGE);
    }
}
