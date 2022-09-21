package com.rockyapp.rockyappbackend.customers.exception;

import com.rockyapp.rockyappbackend.common.exception.ValidationException;

public class CustomerNotFoundException extends ValidationException {

    private static final long serialVersionUID = -1623151985808793327L;
    private static final String ERROR_MESSAGE = "Role introuvable.";


    public CustomerNotFoundException(){
        super(ERROR_MESSAGE);
    }
}
