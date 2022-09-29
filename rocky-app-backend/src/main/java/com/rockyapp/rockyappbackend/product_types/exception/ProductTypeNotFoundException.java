package com.rockyapp.rockyappbackend.product_types.exception;

public class ProductTypeNotFoundException extends Exception {

    private static final long serialVersionUID = -2689082930212072442L;

    private static final String ERROR_MESSAGE = "Type de produit introuvable.";

    public ProductTypeNotFoundException(){
        super(ERROR_MESSAGE);
    }
}
