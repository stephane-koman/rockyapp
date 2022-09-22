package com.rockyapp.rockyappbackend.customers.exception;

import com.rockyapp.rockyappbackend.common.exception.ValidationException;
import com.rockyapp.rockyappbackend.utils.helpers.StringHelper;

public class CustomerAlreadyExistsException extends ValidationException {

    private static final long serialVersionUID = -2524718068633033742L;
    private static final String ERROR_CODE = "CUSTOMER_ALREADY_EXISTS_EXCEPTION";
    private static final String ERROR_MESSAGE = "Le client [{0}] existe déjà.";

    public CustomerAlreadyExistsException(String customerName){
        super(ERROR_CODE, StringHelper.formatMessage(ERROR_MESSAGE, customerName));
    }
}
