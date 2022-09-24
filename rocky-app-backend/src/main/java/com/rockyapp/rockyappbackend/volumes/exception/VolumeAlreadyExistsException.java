package com.rockyapp.rockyappbackend.volumes.exception;

import com.rockyapp.rockyappbackend.common.exception.ValidationException;
import com.rockyapp.rockyappbackend.utils.helpers.StringHelper;

public class VolumeAlreadyExistsException extends ValidationException {

    private static final long serialVersionUID = 6414181321758673538L;

    private static final String ERROR_CODE = "VOLUME_ALREADY_EXISTS_EXCEPTION";
    private static final String ERROR_MESSAGE = "Le volume [{0}] existe déjà.";

    public VolumeAlreadyExistsException(String roleName){
        super(ERROR_CODE, StringHelper.formatMessage(ERROR_MESSAGE, roleName));
    }
}
