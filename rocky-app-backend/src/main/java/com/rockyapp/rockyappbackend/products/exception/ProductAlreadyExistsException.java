package com.rockyapp.rockyappbackend.products.exception;

import com.rockyapp.rockyappbackend.common.exception.ValidationException;
import com.rockyapp.rockyappbackend.utils.helpers.StringHelper;

public class ProductAlreadyExistsException extends ValidationException {

    private static final long serialVersionUID = 6126127434292555653L;
    private static final String ERROR_CODE = "PRODUCT_ALREADY_EXISTS_EXCEPTION";
    private static final String ERROR_MESSAGE = "Le produit [{0}] existe déjà.";

    public ProductAlreadyExistsException(String roleName){
        super(ERROR_CODE, StringHelper.formatMessage(ERROR_MESSAGE, roleName));
    }
}
