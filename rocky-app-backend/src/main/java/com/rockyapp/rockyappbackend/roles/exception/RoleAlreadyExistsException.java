package com.rockyapp.rockyappbackend.roles.exception;

import com.rockyapp.rockyappbackend.utils.helpers.StringHelper;

public class RoleAlreadyExistsException extends Exception {

    private static final long serialVersionUID = -2524718068633033742L;
    private static final String ERROR_MESSAGE = "Le role % existe déjà.";

    public RoleAlreadyExistsException(String roleName){
        super(StringHelper.replace(ERROR_MESSAGE, roleName));
    }
}
