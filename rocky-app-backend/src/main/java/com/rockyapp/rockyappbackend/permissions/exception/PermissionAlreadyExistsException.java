package com.rockyapp.rockyappbackend.permissions.exception;

import com.rockyapp.rockyappbackend.common.exception.ValidationException;
import com.rockyapp.rockyappbackend.utils.helpers.StringHelper;

public class PermissionAlreadyExistsException extends ValidationException {

    private static final long serialVersionUID = -1818472920850279520L;

    private static final String ERROR_CODE = "PERMISSION_ALREADY_EXISTS_EXCEPTION";
    private static final String ERROR_MESSAGE = "La permission [{0}] existe déjà.";

    public PermissionAlreadyExistsException(String roleName){
        super(ERROR_CODE, StringHelper.formatMessage(ERROR_MESSAGE, roleName));
    }
}
