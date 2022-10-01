package com.rockyapp.rockyappbackend.products.exception;

import com.rockyapp.rockyappbackend.common.exception.ValidationException;

public class ProductNotFoundException extends ValidationException {

    private static final long serialVersionUID = 9015152925386491568L;
    private static final String ERROR_MESSAGE = "Produit introuvable.";

    public ProductNotFoundException(){
        super(ERROR_MESSAGE);
    }
}
