package com.rockyapp.rockyappbackend.volumes.exception;

import com.rockyapp.rockyappbackend.common.exception.ValidationException;

public class VolumeNotFoundException extends ValidationException {

    private static final long serialVersionUID = -857235174931905695L;
    private static final String ERROR_MESSAGE = "Volume introuvable.";

    public VolumeNotFoundException(){
        super(ERROR_MESSAGE);
    }
}
