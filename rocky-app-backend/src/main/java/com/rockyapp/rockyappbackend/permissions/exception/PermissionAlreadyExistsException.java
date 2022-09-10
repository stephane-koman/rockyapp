package com.rockyapp.rockyappbackend.permissions.exception;

import com.rockyapp.rockyappbackend.utils.helpers.StringHelper;

public class PermissionAlreadyExistsException extends Exception {

    private static final long serialVersionUID = -1818472920850279520L;

    private static final String ERROR_MESSAGE = "La permission % existe déjà.";

    public PermissionAlreadyExistsException(String roleName){
        super(StringHelper.replace(ERROR_MESSAGE, roleName));
    }
}
