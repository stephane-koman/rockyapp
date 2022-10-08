package com.rockyapp.rockyappbackend.orders.exception;

import com.rockyapp.rockyappbackend.common.exception.ValidationException;

public class InvoiceNotFoundException extends ValidationException {

    private static final long serialVersionUID = -5224181139603283665L;
    private static final String ERROR_MESSAGE = "Facture introuvable.";

    public InvoiceNotFoundException(){
        super(ERROR_MESSAGE);
    }
}
