package com.rockyapp.rockyappbackend.product_types.exception;

import com.rockyapp.rockyappbackend.utils.helpers.StringHelper;

public class ProductTypeAlreadyExistsException extends Exception {

    private static final long serialVersionUID = -4095373125481345308L;

    private static final String ERROR_MESSAGE = "Le type de produit % existe déjà.";

    public ProductTypeAlreadyExistsException(String roleName){
        super(StringHelper.replace(ERROR_MESSAGE, roleName));
    }
}
