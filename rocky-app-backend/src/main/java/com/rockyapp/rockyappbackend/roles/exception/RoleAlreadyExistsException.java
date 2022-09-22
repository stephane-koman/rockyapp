package com.rockyapp.rockyappbackend.roles.exception;

import com.rockyapp.rockyappbackend.common.exception.ValidationException;
import com.rockyapp.rockyappbackend.utils.helpers.StringHelper;

public class RoleAlreadyExistsException extends ValidationException {

    private static final long serialVersionUID = -2524718068633033742L;
    private static final String ERROR_CODE = "ROLE_ALREADY_EXISTS_EXCEPTION";
    private static final String ERROR_MESSAGE = "Le role [{0}] existe déjà.";

    public RoleAlreadyExistsException(String roleName){
        super(ERROR_CODE, StringHelper.formatMessage(ERROR_MESSAGE, roleName));
    }
}
